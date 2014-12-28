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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for FormattedTextType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="FormattedTextType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Space"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Break"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Right"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Center"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Bold"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Italic"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}Underline"/>
 *         &lt;element ref="{urn:oio:medcom:chronicdataset:1.0.0}FixedFont"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormattedTextType", propOrder = {
        "content"
})
public class FormattedTextType {

    @XmlElementRefs({
            @XmlElementRef(name = "Break", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = Break.class),
            @XmlElementRef(name = "Italic", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
            @XmlElementRef(name = "Right", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
            @XmlElementRef(name = "Bold", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
            @XmlElementRef(name = "Underline", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
            @XmlElementRef(name = "Center", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
            @XmlElementRef(name = "FixedFont", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = JAXBElement.class),
            @XmlElementRef(name = "Space", namespace = "urn:oio:medcom:chronicdataset:1.0.0", type = Space.class)
    })
    @XmlMixed
    protected List<Object> content;

    /**
     * Gets the value of the content property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Break }
     * {@link JAXBElement }{@code <}{@link BreakableTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link BreakableTextType }{@code >}
     * {@link String }
     * {@link JAXBElement }{@code <}{@link BreakableTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link BreakableTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link BreakableTextType }{@code >}
     * {@link JAXBElement }{@code <}{@link SimpleFormattedTextType }{@code >}
     * {@link Space }
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

}
