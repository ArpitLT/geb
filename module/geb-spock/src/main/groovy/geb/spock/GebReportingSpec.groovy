/* Copyright 2009 the original author or authors.
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
package geb.spock

import geb.report.ReporterSupport
import org.junit.Rule
import org.junit.rules.TestName
import spock.lang.Shared

class GebReportingSpec extends GebSpec {

        // Ridiculous name to avoid name clashes
    @Rule
    TestName gebReportingSpecTestName
    private int gebReportingPerTestCounter = 1
    @Shared
    private int gebReportingSpecTestCounter = 1

    def setupSpec() {
        testManager = GebTestManager.builder()
                .configurationEnvironmentNameSupplier(this.&getGebConfEnv)
                .configurationScriptResourcePathSupplier(this.&getGebConfScript)
                .configurationSupplier(this.&createConf)
                .browserSupplier(this.&createBrowser)
                .reportLabelCreator(this.&createReportLabel)
                .build()

        reportGroup getClass()
        cleanReportGroupDir()
    }

    def setup() {
        reportGroup getClass()
    }

    def cleanup() {
        testManager.reportEnd()

        ++gebReportingSpecTestCounter
    }

    void reportFailure() {
        testManager.reportFailure()
    }

    void report(String label = "") {
        testManager.report(label)
    }

    String createReportLabel(String label = "") {
        ReporterSupport.toTestReportLabel(gebReportingSpecTestCounter, gebReportingPerTestCounter++, gebReportingSpecTestName?.methodName ?: 'fixture', label)
    }
}
