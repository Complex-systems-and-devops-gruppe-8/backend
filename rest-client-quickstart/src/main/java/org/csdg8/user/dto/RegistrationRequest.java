package org.csdg8.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @XmlElement
    @NotBlank
    public String username;
    
    @XmlElement
    @NotBlank
    public String password;
}