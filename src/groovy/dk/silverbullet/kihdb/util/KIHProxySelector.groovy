/**
 * Copyright 2014 Stiftelsen for Software-baserede Sundhedsservices - 4S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.silverbullet.kihdb.util

import grails.util.Holders
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KIHProxySelector extends ProxySelector {
    private Logger log = LoggerFactory.getLogger(this.class.name)

    private final ProxySelector defaultSelector

    private Proxy kihProxy

    private Boolean useProxy
    private String proxyHost
    private String proxyPort
    private String proxyUsername
    private String proxyPassword

    private boolean useGenealProxy = false

    private List<Proxy> noProxy = new ArrayList<Proxy>()
    private List<Proxy> kihProxyList = new ArrayList<Proxy>()
    private List<Proxy> defaultProxyList = new ArrayList<Proxy>()

    public KIHProxySelector() {
        log.debug "Setting up proxy selector"
        defaultSelector = ProxySelector.default

        if (Holders.grailsApplication.config.xds.documentregistry.useProxy) {
            useProxy = Boolean.valueOf(Holders.grailsApplication.config.xds.documentregistry.useProxy)
            log.debug "Use proxy? " + useProxy
        }

        if (useProxy) {
            proxyPort = Holders.grailsApplication.config.xds.documentregistry.proxyPort
            proxyHost = Holders.grailsApplication.config.xds.documentregistry.proxyHost
            proxyUsername = Holders.grailsApplication.config.xds.documentregistry.proxyUsername
            proxyPassword = Holders.grailsApplication.config.xds.documentregistry.proxyPassword

            kihProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, (null == proxyPort) ? 3128 : Integer.valueOf(proxyPort)))
            kihProxyList.add(kihProxy)
            log.debug "Proxy host: ${proxyHost} - proxy port: ${proxyPort}"
        }

        if (Holders.grailsApplication.config.general.useProxy) {
            log.debug "Setting up general proxy "
            useGenealProxy = true
            def port = Holders.grailsApplication.config.general.proxyPort
            def host = Holders.grailsApplication.config.general.proxyHost

            def defaultProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, (null == port) ? 3128 : Integer.valueOf(port)))
            defaultProxyList.add(defaultProxy)
            log.debug "Default proxy host: ${host} - proxy port: ${port}"
        }

        noProxy.add(Proxy.NO_PROXY)
    }


    @Override
    List<Proxy> select(URI uri) {
        List<Proxy> retVal

        log.debug("Trying to reach URL : " + uri);
        if (uri == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }

        if (useProxy && uri && uri.getHost().contains("npi")) {
            log.debug "Returning proxy list for NPI service"
            retVal = kihProxyList


        } else if (useGenealProxy) {
            log.debug "Returning general proxy for ${uri}"
            retVal = defaultProxyList
        } else {
            retVal = noProxy
        }

        return retVal
    }

    @Override
    void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        log.warn("Failed to connect to a proxy when connecting to " + uri.getHost());
        if (uri == null || sa == null || ioe == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        }
        defaultSelector.connectFailed(uri, sa, ioe);
    }
}
