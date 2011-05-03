/*
 * SortEntry.java
 *
 * Created on August 3, 2004, 9:13 PM
 */

package org.osuosl.srw.sort;

/**
 *
 * @author  levan
 */
public class SortEntry implements Comparable {

    /**
     *  Identifier - Identifier of record being sorted.
     */
    private String identifier;
    public String getIdentifier(){return identifier;}
    public void setIdentifier(String inp){
        identifier = inp;
    }

    /**
     *  Values - values that this entry is being sorted on.
     *           no matching keys because they should always
     *           be added in the correct priority by SortTool
     *           and then compared in this same order.
     */
    private Comparable[] values;
    public Comparable[] getValues(){return values;}
    public void setValues(Comparable[] inp){
        values = inp;
    }

    /**
     *  Parent - The SortTool that created this record.  This
     *           cyclic relation is needed for checking sort
     *           options such as "Ascending" and "MissingValue"
     */
    private SortTool parent;
    public SortTool getParent(){return parent;}
    public void setParent(SortTool inp){
        parent = inp;
    }


    public SortEntry(String identifier, Comparable[] values, SortTool parent) {
        this.identifier = identifier;
        this.values = values;
        this.parent = parent;
    }

    /**
     * Compare this to another object.
     *
     * This function makes the assumption that empty strings were converted to nulls and nulls
     * are only present if missingvalue == highvalue || missing value == lowvalue.
     *
     * For any other missingValue the logic should be handled in SortTool.addEntry()
     * where it can be dealt with more efficiently.
     *
     * @param o - object to compare to
     * @return -1 (less), 0 (equal), 1 (more)
     * @see Comparable#compareTo(Object o)
     * @see SortTool#addEntry(String identifier, ORG.oclc.os.SRW.Record record)
     */
    public int compareTo(Object o) {

        final SortEntry sortEntry = (SortEntry) o;
        int result = 0;
        Comparable[] otherValues = sortEntry.getValues();

        Comparable thisValue;
        Comparable otherValue;

        /**
         * loop through comparing values until a difference is
         * found (result != 0) or there are no more values to
         * compare.
         */
        for (int i = 0; i < values.length && result == 0; i++) {
            thisValue = values[i];
            otherValue = otherValues[i];
            SortKey sortKey = parent.sortKeys[i];

            if (thisValue == null && otherValue == null){
                /**
                 * both missing, regardless of how its handled they are equal.
                 * result == 0 already so do nothing.
                 */
            } else if (otherValue == null) {
                // one value is missing, result can be determined by if missingvalue is highvalue or lowvalue
                if (sortKey.missingValue == SortKey.MISSINGVALUE_HIGHVALUE) {
                    result = -1;
                } else {
                    result = 1;
                }
            } else if (thisValue == null) {
                // one value is missing, result can be determined by if missingvalue is highvalue or lowvalue
                if (sortKey.missingValue == SortKey.MISSINGVALUE_HIGHVALUE) {
                    result = 1;
                } else {
                    result = -1;
                }
            } else {
                // both values present, compare.
                if (sortKey.caseSensitive) {
                    result = thisValue.compareTo(otherValue);
                } else {
                    if (thisValue instanceof String && otherValue instanceof String) {
                        result = ((String)thisValue).toLowerCase().compareTo(((String)otherValue).toLowerCase());
                    } else {
                        result = thisValue.compareTo(otherValue);
                    }
                }
            }

            // reverse case for descending
            if (!sortKey.ascending) {
                result = result * -1;
            }
        }

        return result;
    }

    public String toString() {
        StringBuffer sb=new StringBuffer("org.osuosl.srw.sort.SortEntry: key=").append(identifier);
        if (values != null){
            for (int i = 0; i < values.length; i++) {
                Comparable value = values[i];
                sb.append(", key ").append(i).append("=").append(value);
            }
        }

        return sb.toString();
    }
}
