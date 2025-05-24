package net.omar.framework.ioc.xml;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bean")
public class Bean {
    
    @XmlAttribute(name = "id", required = true)
    private String id;
    
    @XmlAttribute(name = "class", required = true)
    private String className;
    
    @XmlAttribute(name = "scope")
    private String scope = "singleton";
    
    @XmlElement(name = "property")
    private List<Property> properties;
    
    @XmlElement(name = "constructor-arg")
    private List<ConstructorArg> constructorArgs;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public List<Property> getProperties() {
        return properties;
    }
    
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
    
    public List<ConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }
    
    public void setConstructorArgs(List<ConstructorArg> constructorArgs) {
        this.constructorArgs = constructorArgs;
    }
}