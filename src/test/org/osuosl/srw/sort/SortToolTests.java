/*
 * OCKHAM P2PREGISTRY Copyright 2006 Oregon State University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.osuosl.srw.sort;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

import java.io.FileReader;
import java.io.File;
import java.net.URL;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.osuosl.srw.sort.SortTool;
import org.osuosl.srw.sort.SortKey;
import org.osuosl.srw.sort.SortEntry;
import org.osuosl.srw.sort.AbortMissingValueException;
import org.osuosl.srw.SRWDiagnostic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ORG.oclc.os.SRW.Record;

/**
 * @author peter
 *         Date: Oct 24, 2005
 *         Time: 10:56:46 AM
 */
public class SortToolTests extends TestCase {
    static Log log= LogFactory.getLog(SortToolTests.class);

    public static final String SORTKEY_STRING = "/record/string";
    public static final String SORTKEY_FLOAT = "/record/float";

    String[] unsortedRecords;

    protected void setUp() throws Exception {

        //read the datafiles

        String[] urls = {
                "test/org/osuosl/srw/sort/TestData0.xml"
                ,"test/org/osuosl/srw/sort/TestData1.xml"
                ,"test/org/osuosl/srw/sort/TestData2.xml"
                ,"test/org/osuosl/srw/sort/TestData3.xml"
                ,"test/org/osuosl/srw/sort/TestData4.xml"
                ,"test/org/osuosl/srw/sort/TestData5.xml"
        };

        unsortedRecords = (String[]) Array.newInstance(String.class, urls.length);

        for (int i = 0; i < urls.length; i++) {
            FileReader in = null;
            try {
                String urlString = urls[i];
              //  log.info("finding url for ["+urlString+"]");
                URL url = SortToolTests.class.getClassLoader().getResource(urlString);
              //  log.info("Getting file ["+url+"]");
                StringBuffer temp = new StringBuffer();
                File file = new File(url.toURI());
                in = new FileReader(file);
                int c;
                while ((c = in.read()) != -1){
                    temp.append((char)c);
                }
                unsortedRecords[i] = temp.toString();

            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }

        super.setUp();
    }

    protected void tearDown() throws Exception {
        unsortedRecords=null;
        super.tearDown();
    }

    public static Test suite()
    {
        return new TestSuite(SortToolTests.class);
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void testStringsAscending() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testStringsDescending()  {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setAscending(false);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);

        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testFloatsAscending() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testFloatsDescending() throws SRWDiagnostic {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setAscending(false);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingAbortString() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_ABORT);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            for (int i = 0; i < unsortedRecords.length; i++) {
                String unsortedRecord = unsortedRecords[i];
                Record record = new Record(unsortedRecord, null);
                sorttool.addEntry(""+i, record);
            }
            throw new AssertionFailedError("Abort exception was not thrown");
        } catch (SRWDiagnostic e) {
            assertEquals("Abort exception not thrown as expected", SRWDiagnostic.SortEndedDueToMissingValue, e.code );
        }
    }

    public void testMissingAbortFloat()  {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_ABORT);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            for (int i = 0; i < unsortedRecords.length; i++) {
                String unsortedRecord = unsortedRecords[i];
                Record record = new Record(unsortedRecord, null);
                sorttool.addEntry(""+i, record);
            }
            throw new AssertionFailedError("Abort exception was not thrown");
        } catch (SRWDiagnostic e) {
            assertEquals("Abort exception not thrown as expected", SRWDiagnostic.SortEndedDueToMissingValue, e.code );
        }
    }

    public void testMissingHighString() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_HIGHVALUE);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingHighFloat() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_HIGHVALUE);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingLowFloat() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_LOWVALUE);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(5)).getIdentifier());

        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingLowString() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_LOWVALUE);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(5)).getIdentifier());

        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingOmitString() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_OMIT);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(4)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingOmitFloat() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_OMIT);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(3)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingConstantFloat() throws SRWDiagnostic {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_CONSTANT);
        sortkey.setMissingValueConstant("4.5");
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(5)).getIdentifier());

        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testMissingConstantString() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setMissingValue(SortKey.MISSINGVALUE_CONSTANT);
        sortkey.setMissingValueConstant("aba");
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(5)).getIdentifier());

        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testStringsCaseSensitive() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_STRING);
        sortkey.setCaseSensitive(true);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);

        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testFloatsCaseSensitive() {
        SortTool sorttool = new SortTool();
        SortKey sortkey = new SortKey(SORTKEY_FLOAT);
        sortkey.setCaseSensitive(true);
        SortKey[] sortKeys = {sortkey};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testStringsFloats() {
        SortTool sorttool = new SortTool();
        SortKey sortkey1 = new SortKey(SORTKEY_STRING);
        SortKey sortkey2 = new SortKey(SORTKEY_FLOAT);
        SortKey[] sortKeys = {sortkey1, sortkey2};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    public void testFloatsStrings() {
        SortTool sorttool = new SortTool();
        SortKey sortkey1 = new SortKey(SORTKEY_FLOAT);
        sortkey1.setMissingValue(SortKey.MISSINGVALUE_LOWVALUE);
        SortKey sortkey2 = new SortKey(SORTKEY_STRING);
        sortkey2.setMissingValue(SortKey.MISSINGVALUE_LOWVALUE);
        SortKey[] sortKeys = {sortkey1, sortkey2};
        sorttool.setSortKeys(sortKeys);
        log.info("SortTool: " + sorttool);
        try {
            addEntries(sorttool);
            ArrayList sortedlist = sorttool.getEntries();
            assertEquals("Value out of order", "3", ((SortEntry)sortedlist.get(0)).getIdentifier());
            assertEquals("Value out of order", "2", ((SortEntry)sortedlist.get(1)).getIdentifier());
            assertEquals("Value out of order", "4", ((SortEntry)sortedlist.get(2)).getIdentifier());
            assertEquals("Value out of order", "1", ((SortEntry)sortedlist.get(3)).getIdentifier());
            assertEquals("Value out of order", "5", ((SortEntry)sortedlist.get(4)).getIdentifier());
            assertEquals("Value out of order", "0", ((SortEntry)sortedlist.get(5)).getIdentifier());
        } catch (SRWDiagnostic e) {
            throw new AssertionFailedError(e.toString());
        }
    }

    private void addEntries(SortTool sorttool) throws SRWDiagnostic {
        for (int i = 0; i< unsortedRecords.length; i++) {
            String unsortedRecord = unsortedRecords[i];
            Record record = new Record(unsortedRecord, null);
            sorttool.addEntry(""+i, record);
        }
        sorttool.sort();
        dumplist(sorttool);
    }


    public void dumplist(SortTool tool){
        ArrayList entries = tool.getEntries();
        String list = "";

        for (int i = 0; i < entries.size(); i++) {
            SortEntry entry = (SortEntry) entries.get(i);
            Object[] values = entry.getValues();

            list = list + entry.getIdentifier() + "{";
            for (int j = 0; j < values.length; j++) {
                list = list + values[j] + ", ";
            }
            list = list + "}, ";
        }

        log.info("List of entries is ["+ list +"]");
    }

}
