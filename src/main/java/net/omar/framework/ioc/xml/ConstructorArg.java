package net.omar.framework.ioc.xml;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constructor-arg")
public class ConstructorArg {
    
    @XmlAttribute(name = "index")
    private Integer index;
    
    @XmlAttribute(name = "type")
    private String type;
    
    @XmlAttribute(name = "value")
    private String value;
    
    @XmlAttribute(name = "ref")
    private String ref;
    
    public Integer getIndex() {
        return index;
    }
    
    public void setIndex(Integer index) {
        this.index = index;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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