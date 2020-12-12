package io.javabrains.springsecurityjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
@Value
public class UserDto {
    private String email;
    private String name;
    private boolean monitor;
    private String linkedEmail;
}
