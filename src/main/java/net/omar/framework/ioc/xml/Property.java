package net.omar.framework.ioc.xml;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property")
public class Property {
    
    @XmlAttribute(name = "name", required = true)
    private String name;
    
    @XmlAttribute(name = "value")
    private String value;
    
    @XmlAttribute(name = "ref")
    private String ref;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getRef() {
        return ref;
    }
    
    public void setRef(String ref) {
        this.ref = ref;
    }
}