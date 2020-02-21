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

import geb.Browser
import geb.Configuration
import geb.ConfigurationLoader
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

class GebSpec extends Specification {

    String gebConfEnv = null
    String gebConfScript = null

    @Shared
    GebTestManager testManager

    def setupSpec() {
        testManager = GebTestManager.builder()
                .configurationEnvironmentNameSupplier(this.&getGebConfEnv)
                .configurationScriptResourcePathSupplier(this.&getGebConfScript)
                .configurationSupplier(this.&createConf)
                .browserSupplier(this.&createBrowser)
                .build()
    }

    Configuration createConf() {
        def environment = testManager.configurationEnvironmentName
        def scriptPath = testManager.configurationScriptResourcePath
        def classLoader = new GroovyClassLoader(getClass().classLoader)
        new ConfigurationLoader(environment, System.properties, classLoader).getConf(scriptPath)
    }

    Browser createBrowser() {
        new Browser(testManager.configuration)
    }

    Browser getBrowser() {
        testManager.browser
    }

    void resetBrowser() {
        testManager.resetBrowser()
    }

    def methodMissing(String name, args) {
        getBrowser()."$name"(*args)
    }

    def propertyMissing(String name) {
        getBrowser()."$name"
    }

    def propertyMissing(String name, value) {
        getBrowser()."$name" = value
    }

    def cleanup() {
        if (!isSpecStepwise()) {
            resetBrowser()
        }
    }

    def cleanupSpec() {
        if (isSpecStepwise()) {
            resetBrowser()
        }
    }

    private isSpecStepwise() {
        this.class.getAnnotation(Stepwise) != null
    }
}