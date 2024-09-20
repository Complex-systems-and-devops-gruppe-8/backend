package org.csdg8.auth.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RefreshAccessTokenRequest {
    public String refreshToken;
    public String accessToken;
}
