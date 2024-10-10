package org.csdg8.infrastructure;

import jakarta.xml.bind.annotation.XmlElement;

public class ConstraintViolationDetail {

    @XmlElement
    public String field;

    @XmlElement
    public String message;
}
