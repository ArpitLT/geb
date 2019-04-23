/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geb.test

import geb.Configuration
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.openqa.selenium.remote.server.WebDriverServlet

class CallbackAndWebDriverServer extends CallbackHttpServer {

    CallbackAndWebDriverServer(Configuration configuration) {
        super(configuration)
    }

    String getApplicationUrl() {
        "$protocol://localhost:$port/application/"
    }

    URL getWebdriverUrl() {
        new URL("$protocol://localhost:$port/webdriver")
    }

    protected addServlets(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new CallbackServlet(this)), "/application/*")

        context.setAttribute(WebDriverServlet.SESSION_TIMEOUT_PARAMETER, 10000)
        context.addServlet(WebDriverServlet, "/webdriver/*")
    }
}
