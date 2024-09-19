package org.csdg8.user.dto;

import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @XmlElement
    public Long id;
    @XmlElement
    public String username;
    @XmlElement
    public Set<String> role;
}
