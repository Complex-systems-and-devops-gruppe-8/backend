package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenRequest {
    @NotBlank
    @XmlElement
    public String username;

    @NotBlank
    @XmlElement
    public String password;
}
