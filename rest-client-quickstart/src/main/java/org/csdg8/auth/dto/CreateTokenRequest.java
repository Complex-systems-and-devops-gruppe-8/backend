package org.csdg8.auth.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenRequest {
    public String username;
    public String password;
}
