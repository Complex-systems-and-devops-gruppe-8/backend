package org.csdg8.auth.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenResponse {
    public String accessToken;
    public String refreshToken;
}
