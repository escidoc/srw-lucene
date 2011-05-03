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

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.z3950.zing.cql.CQLParser;
import org.z3950.zing.cql.CQLNode;

import java.util.Arrays;

import org.osuosl.srw.*;
import ORG.oclc.os.SRW.RecordIterator;

/**
 * This suite is intended to be a common benchmark for what is possible with CQL.
 * However, this is by no means an exhaustive list of tests.  Given that the set of sample
 * data is the same the expected results should be the same regardless of SRWDatabase
 * implementation.
 * <p/>
 * <b>Please Note:</b><br>
 * The various CQL options are not required for any implementation, so failure on any
 * tests does not indicate a broken implementation.  This suite should be used to determine
 * what query features an SRWDatabase has and for testing the implementation of new features.
 *
 * @author peter
 *         Date: Oct 31, 2005
 *         Time: 10:15:08 AM
 */
public abstract class TranslatorTestsBase  extends TestCase {

    static private Log log= LogFactory.getLog(TranslatorTestsBase.class);

    public CQLTranslator translator;
    public static final String idField = "key";
    public static final String indexPath = "index";
    CQLParser parser;
    public RecordResolver resolver;

    protected void setUp() throws Exception {
        parser = new CQLParser();
        super.setUp();
    }

    protected void tearDown() throws Exception {
        translator = null;
        parser = null;
        super.tearDown();
    }

    public static Test suite()
    {
        return new TestSuite(TranslatorTestsBase.class);
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void testEquals() {

        query("title=the",              new int[]{1, 2, 5});
        query("title=\"the Sith\"",     new int[]{2});
        query("title=\"Revenge Sith\"", new int[]{});
        query("float=-5",               new int[]{0});
        query("float=\"-0.1\"",         new int[]{1});
        query("float=1.00003",          new int[]{2});
        query("float=150",              new int[]{5});
    }

    public void testNotEqualsSingleTerm() {
        query("title<>the",              new int[]{0, 3, 4});
        query("title<>\"the Sith\"",     new int[]{0, 1, 3, 4, 5});
        query("title<>\"Revenge Sith\"", new int[]{0, 1, 2, 3, 4, 5});
        query("float<>-5",               new int[]{1, 2, 3, 4, 5});
        query("float<>\"-0.1\"",         new int[]{0, 2, 3, 4, 5});
        query("float<>1.00003",          new int[]{0, 1, 3, 4, 5});
        query("float<>150",              new int[]{0, 1, 2, 3, 4});
    }

    public void testNotEqualsMultipleTerm() {
        query("title<>Jedi and title=the",    new int[]{1, 2});
        query("title=the and title<>Jedi",    new int[]{1, 2});
        query("title=\"of the\" and title<>\"the Clones\"",     new int[]{2, 5});
        query("title<>\"the Clones\" and title=\"of the\"",     new int[]{2, 5});
        query("title=the and title<>\"the Clones\"",     new int[]{2, 5});
        query("title<>\"the Clones\" and title=the",     new int[]{2, 5});

    }

    public void testGreaterThanNumber() {
        query("float>-5",           new int[]{1, 2, 4, 5});
        query("float>-0.1",         new int[]{2, 4, 5});
        query("float>1.00003",      new int[]{4, 5});
        query("float>12",           new int[]{5});
    }

    public void testGreaterThanString() {
        query("title>of",           new int[]{1, 2, 5});
        query("title>Jedi",         new int[]{2, 0, 5, 3, 4, 1});
        query("title>Revenge",      new int[]{2, 3, 4, 1, 5, 0});
        query("title>The",          new int[]{1, 2, 3, 5});
        query("title>W",            new int[]{1, 2, 3, 5});
    }

    public void testGreaterThanOrEqualsNumber() {
        query("float>=-5",          new int[]{0, 1, 2, 4, 5});
        query("float>=-0.1",        new int[]{1, 2, 4, 5});
        query("float>=1.00003",     new int[]{2, 4, 5});
        query("float>=12",          new int[]{4, 5});
    }

    public void testGreaterThanOrEqualsString() {
        query("title>=of",           new int[]{1, 2, 5});
        query("title>=\"of the\"",   new int[]{1, 2, 5});
    }

    public void testLesserThanNumber() {
        query("float<-5",           new int[]{});
        query("float<-0.1",         new int[]{0});
        query("float<1.00003",      new int[]{0, 1});
        query("float<12",           new int[]{0, 1, 2});
    }

    public void testLesserThanString() {
        query("title<the",          new int[]{0, 1, 2, 3, 4, 5});
        query("title<Empire",       new int[]{1, 4});
        query("title<Menace",       new int[]{1, 4, 5});
    }

    public void testLesserThanOrEqualsNumber() {
        query("float<=-5",          new int[]{0});
        query("float<=-0.1",        new int[]{0, 1});
        query("float<=1.00003",     new int[]{0, 1, 2});
        query("float<=12",          new int[]{0, 1, 2, 4});
    }

    public void testLesserThanOrEqualsString() {
        query("title<=the",         new int[]{0, 1, 2, 3, 4, 5});
        query("title<=Empire",      new int[]{1, 4});
        query("title<=Jedi",        new int[]{1, 4, 5});
        query("title<=Menace",      new int[]{0, 1, 4, 5});
    }

    public void testAnd() {
        query("title=the and title=Sith", new int[]{2});
        query("title=the and title=of and title=Jedi", new int[]{5});
    }

    public void testOr(){
        query("title=The or title=Sith", new int[]{2, 0, 4});
        query("title=of or title=Back", new int[]{4, 1, 2, 5});
        query("string=aaa or title=Back or string=ZZZ", new int[]{5, 0, 1, 4});
    }

    /**
     * Any is an implicit or
     */
    public void testAny(){
        query("title any \"the Sith\"",     new int[]{1, 2, 5});
        query("title any \"Revenge Sith\"", new int[]{2});
    }

    /**
     * All is an implicit and
     */
    public void testAll(){
        query("title all \"the Sith\"",     new int[]{2});
        query("title all \"Revenge Sith\"", new int[]{2});
    }


    public void testNot(){
        query("title=the not title=Jedi",   new int[]{1, 2});
        query("title=the not title=Jedi not title=Sith",   new int[]{1});
    }

    public void testProx(){
        query("dc.title = \"cat\" prox/distance=1/unit=word dc.title = \"in\"",  new int[]{});
        query("dc.title = \"Empire\" prox/word=2 dc.title = \"Back\"",  new int[]{4});
        query("dc.title = \"The\" prox/distance=1 dc.title = \"Back\"",     new int[]{});
        query("dc.title = \"The\" prox/distance=3 dc.title = \"Back\"",     new int[]{4});
    }

    public void testExact() {
        query("title exact \"Attack of the Clones\"", new int[]{1});
        query("title exact \"of the Clones\"",        new int[]{});
    }

    public void testOrderOfOperations() {
        query("string=AAA or (title=The and title=Empire)", new int[]{4, 2});
        query("string=AAA or (title=The and float=12)", new int[]{4, 2});
        query("string=AAA or (title=The not float=12)", new int[]{2, 0});
        query("(string=AAA or string=ZZZ) and (title=The not float=12)", new int[]{0});
    }

    /**
     * Validates a query given the expected results.
     *
     * The query will be checked for:
     *
     * <ol>CQLParser's ability to parse query.  Doesn't validate results just that a CQL tree was returned
     * <ol>Contents of result set.
     *
     * @param query - CQL Query
     * @param expectedIndex - int[] containing an expected result set
     */
    private void query(String query, int[] expectedIndex) {
        try {
            log.info("====================================================================================");

            /**
             * parse query
             */
            CQLNode node = parser.parse(query);
            assertNotNull("Unable to parse CQL into CQL objects", node);
            log.info("CQL Query : ["+ query +"]");

            /**
             * execute query
             */
            ResolvingQueryResult results = (ResolvingQueryResult) translator.search(node, null);
            results.setResolver(resolver);
            assertNotNull("Null result set", results);

            /**
             * iterate through the results retrieving the full records
             * create an int[] containing the array of identifiers
             *
             * todo implement this instead of the following section of code
             */

             /**
             * Convert result set to an String[] of identifiers so that they can be
             * compared to the expect results.
             *
             */
            String[] resultsStr = new String[(int) results.getNumberOfRecords()];
            RecordIterator iter = results.newRecordIterator(0,resultsStr.length,null);
            for (int i = 0; i < resultsStr.length; i++) {
                IdentifiableRecord record = (IdentifiableRecord) iter.nextRecord();
                if (record == null) {
                    resultsStr[i] =  null;
                } else {
                    resultsStr[i] = record.getIdentifier();
                }
            }

            /**
             * convert expected results to String[]
             */
            String[] expectedIndexStr = new String[expectedIndex.length];
            for (int i = 0; i < expectedIndex.length; i++) {
                expectedIndexStr[i] = expectedIndex[i] + "";
            }

            /**
             * Sort both input and output so when they are compared
             * you can tell if there is a missing record no matter what
             * order either list is in.
             */
            Arrays.sort(resultsStr);
            Arrays.sort(expectedIndexStr);

            /**
             * output expected and actual results
             */
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < expectedIndexStr.length; i++) {
                temp.append(expectedIndexStr[i]);
                if (i+1 != expectedIndexStr.length) {
                    temp.append(", ");
                }
            }
            log.info("Expected Result set : " + temp.toString());
            temp = new StringBuffer();
            for (int i = 0; i < resultsStr.length; i++) {
                temp.append(resultsStr[i]);
                if (i+1 != resultsStr.length) {
                    temp.append(", ");
                }
            }
            log.info("Actual Result set   : " + temp.toString());



            /**
             * check the size of the result set
             */
            assertEquals("Incorrect number of results", resultsStr.length, expectedIndex.length);

            /**
             *  check contents of results - this check determines if the correct
             *  SET of documents was returned.
             *
             *  for this test both arrays must be copied and sorted
             */
            for (int i = 0; i < expectedIndex.length; i++) {
                String id = expectedIndexStr[i];
                assertEquals("Missing Value ", id, resultsStr[i]);
            }

        } catch (Exception e) {

            if (e instanceof SRWDiagnostic && ((SRWDiagnostic)e).code == 19) {
                throw new AssertionFailedError("Unsupported Relation");
            }

            throw new RuntimeException(e);
        }
    }

}
