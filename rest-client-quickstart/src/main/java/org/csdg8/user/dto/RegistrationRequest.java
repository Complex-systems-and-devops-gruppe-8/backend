package org.csdg8.user.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest { //TODO rename to CreateUserRequest
    //TODO add constraints
    public String username;
    public String password;
}