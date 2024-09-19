package org.csdg8.user.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    public String username;
    public String password;
}