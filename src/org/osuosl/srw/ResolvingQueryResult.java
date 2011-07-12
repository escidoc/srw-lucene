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

package org.osuosl.srw;

import ORG.oclc.os.SRW.QueryResult;
import ORG.oclc.os.SRW.RecordIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osuosl.srw.RecordResolver;
import gov.loc.www.zing.srw.ExtraDataType;

/**
 * @author peter
 *         Date: Apr 12, 2006
 *         Time: 1:52:04 PM
 */
public class ResolvingQueryResult extends QueryResult {

    static Log log= LogFactory.getLog(ResolvingQueryResult.class);
    /**
     *  Identifiers
     */
    private String[] identifiers;
    public String[] getIdentifiers(){return identifiers;}
    public void setIdentifiers(String[] inp){
        identifiers = inp;
    }

    /**
     *  ExtraDataType
     */
    private ExtraDataType extraDataType;
    public ExtraDataType getExtraDataType(){return extraDataType;}
    public void setExtraDataType(ExtraDataType inp){
        extraDataType = inp;
    }

    /**
     *  Resolver
     */
    private RecordResolver resolver;
    public RecordResolver getResolver(){return resolver;}
    public void setResolver(RecordResolver inp){
        resolver = inp;
    }

    public ResolvingQueryResult() {}

    public ResolvingQueryResult(String[] identifiers) {
        this.identifiers = identifiers;
    }

    public long getNumberOfRecords() {

        if(identifiers == null)
            return 0;

        return identifiers.length;
    }

    public RecordIterator newRecordIterator(long index, int numRecs, String schemaId) throws InstantiationException {

        // create new array with subset of identifiers
        String[] subset = new String[numRecs];
        System.arraycopy(identifiers, (int)index, subset, 0, numRecs);

        return new ResolvingRecordIterator(
                subset,
                schemaId,
                extraDataType,
                resolver
        );
    }

}
