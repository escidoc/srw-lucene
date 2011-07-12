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
package test.org.osuosl.srw.translator.lucene;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.Query;
import org.w3c.dom.Document;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import test.org.osuosl.srw.translator.TranslatorTestsBase;
import test.org.osuosl.srw.translator.TestResolver;
import org.osuosl.srw.lucene.LuceneTranslator;

/**
 * Tests the capabilities of the LuceneTranslator
 *
 * @author peter
 *         Date: Oct 24, 2005
 *         Time: 10:56:46 AM
 */
public class LuceneTranslatorTests extends TestCase {
    public static final String idField = "key";
    public static final String indexPath = "index";


    /**
     * 
     * @binding
     */
    public void testConvert() {
        try {
            CQLParser cqlParser = 
                new CQLParser();
//            CQLNode cqlNode = cqlParser.parse("dc.title=hallo and dc.language=en");
            CQLNode cqlNode = cqlParser.parse("author=karl");
            LuceneTranslator luceneTranslator = new LuceneTranslator();
            Query query = luceneTranslator.makeQuery(cqlNode);
            System.out.println(query.toString());
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
