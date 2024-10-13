package org.csdg8.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenRequest {

    @NotBlank
    @Size(min = 3, max = 30)
    public String username;

    @NotBlank
    @Size(min = 8, max = 50)
    public String password;
}
