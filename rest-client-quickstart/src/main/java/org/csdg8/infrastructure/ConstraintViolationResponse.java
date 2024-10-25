package org.csdg8.infrastructure;

import java.util.List;

public class ConstraintViolationResponse {

    public String title;
    public String description;
    public List<ConstraintViolationDetail> violations;
}
