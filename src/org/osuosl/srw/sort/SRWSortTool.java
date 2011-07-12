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

import java.util.StringTokenizer;

/**
 * @author peter
 *         Date: Oct 25, 2005
 *         Time: 9:10:47 AM
 */
public class SRWSortTool extends SortTool {

    /**
     * Sets SortKeys to an array of sortkeys parsed from the string
     * @param str - comma delimited list containing one or more sort keys
     */
    public void setSortKeys(String str) {
        StringTokenizer keysTokenizer=new StringTokenizer(str);
        SortKey[] temp = new SortKey[keysTokenizer.countTokens()];
        for(int i=0; keysTokenizer.hasMoreTokens(); i++) {
            temp[i] = new SortKeyParser(keysTokenizer.nextToken()).parse();
        }
        setSortKeys(temp);
    }  

    public class SortKeyParser {

        char[] sortKey;
        int offset = 0;

        public SortKeyParser(String key){
            sortKey = key.toCharArray();
        }

        public SortKey parse() {
            SortKey     sortKey = new SortKey();
            String      temp = null;

            sortKey.setXPath(parseSortKeyProperty());
            sortKey.setSchema(parseSortKeyProperty());

            temp = parseSortKeyProperty();
            sortKey.setAscending( !(temp!=null && temp.equals("0")) );

            temp = parseSortKeyProperty();
            sortKey.setCaseSensitive( temp!=null && temp.equals("1") );

            temp = parseSortKeyProperty();
            if (temp != null) {
                if (temp.equalsIgnoreCase("abort")) {
                    sortKey.setMissingValue(SortKey.MISSINGVALUE_ABORT);
                } else if (temp.equalsIgnoreCase("omit")){
                    sortKey.setMissingValue(SortKey.MISSINGVALUE_OMIT);
                } else if (temp.equalsIgnoreCase("lowvalue")) {
                    sortKey.setMissingValue(SortKey.MISSINGVALUE_LOWVALUE);
                } else if (temp.equalsIgnoreCase("highvalue")) {
                    sortKey.setMissingValue(SortKey.MISSINGVALUE_HIGHVALUE);
                } else {
                    sortKey.setMissingValue(SortKey.MISSINGVALUE_CONSTANT);
                    sortKey.setMissingValueConstant(temp);
                }
            }

            return sortKey;
        }

        private String nextToken() {
            if(sortKey[offset]==',') {
                log.info("found a null sort parameter");
                offset++;
                return null;
            }
            if(sortKey[offset]=='"') { // suck up until the next quote
                int start=offset+1;
                while(sortKey[++offset]!='"');
                String token=new String(sortKey,start,offset-start);
                offset+=2; // skip trailing comma, if any
                return token;
            }
            // suck up until the next comma or EOF
            int start=offset;
            while(offset<sortKey.length-1 && sortKey[++offset]!=',');
            String token;
            if(sortKey[offset]==',')
                token=new String(sortKey,start,offset-start);
            else
                token=new String(sortKey,start,offset-start+1);
            offset++; // skip trailing comma, if any
            return token;
        }

        private String parseSortKeyProperty() {
            if(offset>=sortKey.length)
                return null;
            return nextToken();
        }
    }
}
