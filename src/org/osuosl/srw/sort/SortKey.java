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
 
package org.osuosl.srw.sort;

/**
 * @author peter
 *         Date: Oct 24, 2005
 *         Time: 3:21:34 PM
 */
public class SortKey {

    public static final int MISSINGVALUE_ABORT = 1;
    public static final int MISSINGVALUE_OMIT = 2;
    public static final int MISSINGVALUE_LOWVALUE = 3;
    public static final int MISSINGVALUE_HIGHVALUE = 4;
    public static final int MISSINGVALUE_CONSTANT = 5;

    /**
     *  XPath
     */
    String xPath;
    public String getXPath(){return xPath;}
    public void setXPath(String inp){
        xPath = inp;
    }

    /**
     *  Schema
     */
    String schema;
    public String getSchema(){return schema;}
    public void setSchema(String inp){
        schema = inp;
    }

    /**
     *  Ascending
     */
    boolean ascending;
    public boolean getAscending(){return ascending;}
    public void setAscending(boolean inp){
        ascending = inp;
    }

    /**
     *  caseSensitive
     */
    boolean caseSensitive;
    public boolean getCaseSensitive(){return caseSensitive;}
    public void setCaseSensitive(boolean inp){
        caseSensitive = inp;
    }

    /**
     *  MissingValue
     */
    int missingValue;
    public int getMissingValue(){return missingValue;}
    public void setMissingValue(int inp){
        missingValue = inp;
    }

    /**
     *  MissingValueConstant
     */
    String missingValueConstant;
    public String getMissingValueConstant(){return missingValueConstant;}
    public void setMissingValueConstant(String inp){
        missingValueConstant = inp;
    }    

    public SortKey() {}

    public SortKey(String xPath) {
        this.ascending = true;
        this.caseSensitive = false;
        this.missingValue = MISSINGVALUE_HIGHVALUE;
        this.missingValueConstant = null;
        this.xPath = xPath;
    }

    public SortKey(String xPath, String schema, boolean ascending, boolean caseSensitive, int missingValue, String missingValueConstant) {
        this.xPath = xPath;
        this.schema = schema;
        this.ascending = ascending;
        this.caseSensitive = caseSensitive;
        this.missingValue = missingValue;
        this.missingValueConstant = missingValueConstant;
    }


    public String toString() {
        return "SortKey{" +
                "xPath='" + xPath + '\'' +
                ", schema='" + schema + '\'' +
                ", ascending=" + ascending +
                ", caseSensitive=" + caseSensitive +
                ", missingValue=" + missingValue +
                ", missingValueConstant='" + missingValueConstant + '\'' +
                '}';
    }
}
