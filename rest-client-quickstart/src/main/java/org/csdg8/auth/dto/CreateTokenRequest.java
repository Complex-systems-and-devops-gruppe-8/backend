package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenRequest {

    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
