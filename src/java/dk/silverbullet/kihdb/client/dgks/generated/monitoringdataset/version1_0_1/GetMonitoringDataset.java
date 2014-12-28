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
package dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_1;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getMonitoringDatasetRequestMessage"
})
@XmlRootElement(name = "GetMonitoringDataset", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class GetMonitoringDataset {

    @XmlElement(name = "GetMonitoringDatasetRequestMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected GetMonitoringDatasetRequestMessage getMonitoringDatasetRequestMessage;

    public GetMonitoringDatasetRequestMessage getGetMonitoringDatasetRequestMessage() {
        return getMonitoringDatasetRequestMessage;
    }

    public void setGetMonitoringDatasetRequestMessage(GetMonitoringDatasetRequestMessage m) {
        this.getMonitoringDatasetRequestMessage = m;
    }

}
