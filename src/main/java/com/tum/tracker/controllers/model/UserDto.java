package com.tum.tracker.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
@Value
public class UserDto {
    private String email;
    private String name;
}