package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RefreshAccessTokenRequest {

    @NotBlank
    @Size(min = 36, max = 36)
    public String refreshToken;

    @NotBlank
    @Size(max = 1000)
    public String accessToken;
}
