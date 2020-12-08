package com.tum.tracker.controllers;


import com.tum.tracker.controllers.model.UserDto;
import com.tum.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @RequestMapping("/home")
    public String loadHome() {
        return "Welcome!";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void addUser(@RequestBody UserDto userDto) {
        service.signUpUser(userDto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return new ResponseEntity<>(service.getUser(email), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/id{email}")
    public void deleteUser(@PathVariable String email) {
        service.deleteUser(email);
    }
}
