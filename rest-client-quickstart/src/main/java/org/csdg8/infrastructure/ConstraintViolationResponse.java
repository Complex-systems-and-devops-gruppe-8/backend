package org.csdg8.infrastructure;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "constraintViolationResponse")
public class ConstraintViolationResponse {

    @XmlElement
    public String title;

    @XmlElement
    public String description;

    @XmlElement(name = "violation")
    public List<ConstraintViolationDetail> violations;
}
