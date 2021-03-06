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

package dk.silverbullet.kihdb.service.dgks.generated.chronicdataset.version1;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.6.2
 * 2012-12-27T09:24:43.152+01:00
 * Generated source version: 2.6.2
 */

@WebFault(name = "Fault", targetNamespace = "urn:oio:medcom:chronicdataset:1.0.0")
public class FaultMessage extends Exception {

    private FaultType fault;

    public FaultMessage() {
        super();
    }

    public FaultMessage(String message) {
        super(message);
    }

    public FaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public FaultMessage(String message, FaultType fault) {
        super(message);
        this.fault = fault;
    }

    public FaultMessage(String message, FaultType fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public FaultType getFaultInfo() {
        return this.fault;
    }
}
