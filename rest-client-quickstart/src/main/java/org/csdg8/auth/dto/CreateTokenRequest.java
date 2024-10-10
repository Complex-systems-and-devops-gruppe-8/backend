package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    @XmlElement
    public String username;

    @NotBlank
    @Size(min=8, max=50)
    @XmlElement
    public String password;
}
