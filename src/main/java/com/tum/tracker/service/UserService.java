package com.tum.tracker.service;

import com.tum.tracker.controllers.model.UserDto;
import com.tum.tracker.dao.UserRepository;
import com.tum.tracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
//    private final EmailSenderService emailSenderService;


    public void signUpUser(UserDto userDto) {
        User user = new User(userDto.getEmail(), userDto.getName());
        repository.save(user);
    }

    public User getUser(String email) {
        return repository.findByEmail(email).get();
    }

    public void deleteUser(String email) {
        repository.deleteUserByEmail(email);
    }
}
