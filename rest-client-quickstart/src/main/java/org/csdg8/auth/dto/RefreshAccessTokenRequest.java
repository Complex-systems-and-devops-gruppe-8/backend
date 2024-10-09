package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RefreshAccessTokenRequest {
    
    @NotBlank
    public String refreshToken;

    @NotBlank
    public String accessToken;
}
