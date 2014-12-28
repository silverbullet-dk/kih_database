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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for DataSegmentListType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="DataSegmentListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataSegment" maxOccurs="unbounded" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="GeneralPractitioner"/>
 *               &lt;enumeration value="Relative"/>
 *               &lt;enumeration value="Consent"/>
 *               &lt;enumeration value="KramPredictor"/>
 *               &lt;enumeration value="DiaryNoteCollection"/>
 *               &lt;enumeration value="MedicalInvestigationCollection"/>
 *               &lt;enumeration value="ContactPersonCollection"/>
 *               &lt;enumeration value="DiagnosisOfRelevanceCollection"/>
 *               &lt;enumeration value="CurrentDrugEffectuationCollection"/>
 *               &lt;enumeration value="LaboratoryReportCollection"/>
 *               &lt;enumeration value="HealthAndPreventionProfile"/>
 *               &lt;enumeration value="HealthProfessionalNoteCollection"/>
 *               &lt;enumeration value="AppointmentCollection"/>
 *               &lt;enumeration value="SelfMonitoredSampleCollection"/>
 *               &lt;enumeration value="PersonalGoalCollection"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataSegmentListType", namespace = "urn:oio:medcom:chronicdataset:all:1.0.0", propOrder = {
        "dataSegment"
})
public class DataSegmentListType {

    @XmlElement(name = "DataSegment")
    protected List<String> dataSegment;

    /**
     * Gets the value of the dataSegment property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataSegment property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataSegment().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getDataSegment() {
        if (dataSegment == null) {
            dataSegment = new ArrayList<String>();
        }
        return this.dataSegment;
    }

}
