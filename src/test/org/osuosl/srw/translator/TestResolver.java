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

package test.org.osuosl.srw.translator;

import org.osuosl.srw.RecordResolver;
import org.osuosl.srw.IdentifiableRecord;
import ORG.oclc.os.SRW.Record;
import ORG.oclc.os.SRW.SRWDiagnostic;
import gov.loc.www.zing.srw.ExtraDataType;

import java.util.Properties;

/**
 * @author peter
 *         Date: Apr 13, 2006
 *         Time: 10:35:00 AM
 */
public class TestResolver implements RecordResolver {

    public Record resolve(Object Id, ExtraDataType extraDataType) {
        return new IdentifiableRecord((String) Id, null, null);
    }

    public String transform(Record record, String schema) throws SRWDiagnostic {
        // unused
        return null;
    }

    public boolean containsSchema(String schema) {
        //unused
        return false;
    }

    public StringBuffer getSchemaInfo() {
        //unused
        return null;
    }

    public void init(Properties properties) {
        //unused
    }

}
