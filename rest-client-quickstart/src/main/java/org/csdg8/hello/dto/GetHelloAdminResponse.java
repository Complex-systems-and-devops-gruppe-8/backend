package org.csdg8.hello.dto;

import com.google.code.siren4j.annotations.Siren4JEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Siren4JEntity(entityClass = "hello-admin", uri = "/hello/admin")
public class GetHelloAdminResponse {
    private String message;
}
