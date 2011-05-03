/*
 * SortTool.java
 *
 * Created on August 3, 2004, 2:17 PM
 */

package org.osuosl.srw.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.StringReader;
import java.lang.reflect.Array;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.osuosl.srw.RecordResolver;
import org.osuosl.srw.SRWDiagnostic;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import ORG.oclc.os.SRW.Record;
import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * Tool for sorting XML.
 *
 * The tool should be initialized with a set of XPath expresions which return the values to sort on.
 *
 * To use this tool:
 *
 * 1) add sortkeys
 * 2) add documents via addEntry(record, identifier)
 *
 * as records are added the sortValues are extracted and stored.  modifying the sortkeys requires
 * that all documents are added again.   
 *
 * @author  Peter
 */
public class SortTool {

    static Log log=LogFactory.getLog(SortTool.class);

    /**
     *  SortKeys
     */
    SortKey[] sortKeys;
    public SortKey[] getSortKeys(){return sortKeys;}
    public void setSortKeys(SortKey[] inp){
        sortKeys = inp;
    }

    /**
     *  Entries
     */
    private ArrayList entries;
    public ArrayList getEntries(){return entries;}
    public void setEntries(ArrayList inp){
        entries = inp;
    }

    /**
     *  Resolver
     */
    private RecordResolver resolver;
    public RecordResolver getResolver(){return resolver;}
    public void setResolver(RecordResolver inp){
        resolver = inp;
    }

    public SortTool() {
        entries = new ArrayList();
    }


    /**
     * Add an entry to the list to be sorted.  The  will have XPath expressions
     * applied to it to extract the values to sort on.
     *
     * The values will have the following rules applied:<br>
     * <li>If possible values will be converted to a Double.
     * <li>Empty strings will be converted to nulls.
     * <li>If missingValue == constant then missing values will be set to missingValueConstant.
     * <li>If missingValue == omit then missing values will not be added.
     * <li>If missingValue == abort then missing values will cause an AbortMissingValueException to be thrown.
     *
     * @param identifier - identifier of record this entry represents
     * @param record - record document to be sorted.
     */
    public void addEntry(String identifier, Record record) throws SRWDiagnostic {
        Comparable[] values = null;
        values = (Comparable[]) Array.newInstance(Comparable.class, sortKeys.length);
        Document               domDoc;
        DocumentBuilder        db=null;
        DocumentBuilderFactory dbf=null;

        try {

            dbf=DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            db=dbf.newDocumentBuilder();

            /**
             * Iterate through the xpaths adding the results to values
             */
            for (int i = 0; i < sortKeys.length; i++) {
                SortKey sortKey = sortKeys[i];
                String value = null;
                String xml = null;

                /**
                 * convert schema if needed.
                 */
                String schema = sortKey.getSchema();
                if (sortKey.getSchema() != null && !schema.equals(record.getRecordSchemaID())){
                    xml = resolver.transform(record, schema);
                } else {
                    xml = record.getRecord();
                }

                /**
                 * Convert to DOM
                 */
                domDoc=db.parse(new InputSource(new StringReader(xml)));

                /**
                 * Use XPath to extract the value from the dom
                 */
                value = extractValue(domDoc, sortKey.xPath);

                /**
                 * process missing value if value is null
                 */
                if ( value == null || value.trim().length() == 0) {
                    if (sortKey.missingValue == SortKey.MISSINGVALUE_ABORT) {
                        throw new SRWDiagnostic(SRWDiagnostic.SortEndedDueToMissingValue, "Record " + identifier + " is missing value " + sortKey.xPath);                        
                    } else if (sortKey.missingValue == SortKey.MISSINGVALUE_OMIT) {
                        return;
                    } else if (sortKey.missingValue == SortKey.MISSINGVALUE_CONSTANT) {
                        values[i] = sortKey.missingValueConstant;
                    } else {
                        values[i] = null;
                    }
                } else {
                    values[i] = value;
                }

                /**
                 * convert to double if possible so numbers
                 * are sorted as numbers
                 */
                if (values[i] != null) {
                    try {
                        values[i] = new Double((String) values[i]);
                    } catch (NumberFormatException e) {
                        //discard, means it wasnt a number
                    }
                }
            }

        } catch (Exception e) {
            throw new SRWDiagnostic(SRWDiagnostic.GeneralSystemError, e.getMessage());
        }

        //entry complete, add to list
        SortEntry entry = new SortEntry(identifier, values, this);
        entries.add(entry);
    }

    public String[] sort(){
        // get array
        SortEntry[] sorted = (SortEntry[]) entries.toArray( new SortEntry[0] );
        // sort
        Arrays.sort(sorted);
        // create new array to hold results
        String[] sortedIdentifiers = new String[sorted.length];

        // update arraylist
        for (int i = 0; i < sorted.length; i++) {
            entries.set(i,sorted[i]);
            sortedIdentifiers[i] = sorted[i].getIdentifier();
        }
        return sortedIdentifiers;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SortTool keys: ");
        for (int i = 0; i < sortKeys.length; i++) {
            SortKey sortKey = sortKeys[i];
            sb.append(sortKey);
            sb.append(", ");
        }
        return sb.toString();
    }

    public String extractValue(Document doc, String xpath) throws TransformerException {
        // perform the search
        NodeList nodeList = XPathAPI. selectNodeList(doc, xpath);

        /**
         * if any matches are found iterate through the matches
         * added them to every Field that they are mapped to
         */
        if (nodeList != null) {

            int nodeListSize = nodeList.getLength();
            for (int z=0; z<nodeListSize; z++) {

                //get the next node
                Node currentNode = nodeList.item(z);

                // get the text node if this isnt it
                if (currentNode.getNodeType() != Node.TEXT_NODE) {
                    currentNode = currentNode.getFirstChild();
                }

                /**
                 * check to see if the node is null, if it
                 * were an empty tag there would be a null
                 * in place of the text node
                 */
                if (currentNode != null) {
                    return currentNode.getNodeValue();
                }
            }
        }
        return null;
    }

}
