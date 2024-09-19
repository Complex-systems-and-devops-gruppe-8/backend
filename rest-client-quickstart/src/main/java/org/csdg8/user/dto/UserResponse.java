package org.csdg8.user.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    public Long id;
    public String username;
    public Set<String> role;
}
