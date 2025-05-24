package net.omar.framework.ioc.xml;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Beans {
    
    @XmlElement(name = "bean")
    private List<Bean> beans;
    
    public List<Bean> getBeans() {
        return beans;
    }
    
    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }
}