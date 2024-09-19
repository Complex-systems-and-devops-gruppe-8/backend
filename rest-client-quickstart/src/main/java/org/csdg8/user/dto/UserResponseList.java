package org.csdg8.user.dto;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseList {
    @XmlElement(name = "user")
    public List<UserResponse> users;
}
