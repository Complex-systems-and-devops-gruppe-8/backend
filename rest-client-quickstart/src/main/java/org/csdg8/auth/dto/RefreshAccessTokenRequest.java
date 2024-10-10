package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class RefreshAccessTokenRequest {

    @NotBlank
    @Size(min = 36, max = 36)
    @XmlElement
    public String refreshToken;

    @NotBlank
    @Size(max = 1000)
    @XmlElement
    public String accessToken;
}
