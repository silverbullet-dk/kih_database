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
package dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "deleteMonitoringDatasetRequestMessage"
})
@XmlRootElement(name = "DeleteMonitoringDataset", namespace = "urn:oio:medcom:monitoringdataset:1.0.1")
public class DeleteMonitoringDataset {

    @XmlElement(name = "DeleteMonitoringDatasetRequestMessage", namespace = "urn:oio:medcom:monitoringdataset:1.0.1", required = true)
    protected dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.DeleteMonitoringDatasetRequestMessage deleteMonitoringDatasetRequestMessage;

    public dk.silverbullet.kihdb.client.dgks.generated.monitoringdataset.version1_0_2.DeleteMonitoringDatasetRequestMessage getDeleteGetMonitoringDatasetRequestMessage() {
        return deleteMonitoringDatasetRequestMessage;
    }

    public void setDeleteMonitoringDatasetRequestMessage(DeleteMonitoringDatasetRequestMessage m) {
        this.deleteMonitoringDatasetRequestMessage = m;
    }

}
