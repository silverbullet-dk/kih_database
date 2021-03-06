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
package dk.silverbullet.kihdb.client.dgks.chronicdataset.version1;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.URL;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2012-12-25T12:28:08.580+01:00
 * Generated source version: 2.6.2
 */
@WebServiceClient(name = "ChronicDatasetService",
        wsdlLocation = "src/wsdl/dgks/wsdl/1.0.0/ChronicDatasetService.wsdl",
        targetNamespace = "urn:oio:medcom:chronicdataset:all:1.0.0")
public class ChronicDatasetService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:oio:medcom:chronicdataset:all:1.0.0", "ChronicDatasetService");
    public final static QName ChronicDatasetPort = new QName("urn:oio:medcom:chronicdataset:all:1.0.0", "ChronicDatasetPort");

    static {
        URL url = ChronicDatasetService.class.getResource("src/wsdl/dgks/wsdl/1.0.0/ChronicDatasetService.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(ChronicDatasetService.class.getName())
                    .log(java.util.logging.Level.INFO,
                            "Can not initialize the default wsdl from {0}", "src/wsdl/dgks/wsdl/1.0.0/ChronicDatasetService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ChronicDatasetService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ChronicDatasetService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ChronicDatasetService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * @return returns ChronicDatasetPortType
     */
    @WebEndpoint(name = "ChronicDatasetPort")
    public ChronicDatasetPortType getChronicDatasetPort() {
        return super.getPort(ChronicDatasetPort, ChronicDatasetPortType.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns ChronicDatasetPortType
     */
    @WebEndpoint(name = "ChronicDatasetPort")
    public ChronicDatasetPortType getChronicDatasetPort(WebServiceFeature... features) {
        return super.getPort(ChronicDatasetPort, ChronicDatasetPortType.class, features);
    }

}
