package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class RefreshAccessTokenRequest {

    @NotBlank
    @XmlElement
    public String refreshToken;

    @NotBlank
    @XmlElement
    public String accessToken;
}
