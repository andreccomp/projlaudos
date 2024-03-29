
package eu.dataaccess.footballpool;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOftCoaches complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOftCoaches">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tCoaches" type="{http://footballpool.dataaccess.eu}tCoaches" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOftCoaches", propOrder = {
    "tCoaches"
})
public class ArrayOftCoaches {

    @XmlElement(nillable = true)
    protected List<TCoaches> tCoaches;

    /**
     * Gets the value of the tCoaches property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tCoaches property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTCoaches().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TCoaches }
     * 
     * 
     */
    public List<TCoaches> getTCoaches() {
        if (tCoaches == null) {
            tCoaches = new ArrayList<TCoaches>();
        }
        return this.tCoaches;
    }

}
