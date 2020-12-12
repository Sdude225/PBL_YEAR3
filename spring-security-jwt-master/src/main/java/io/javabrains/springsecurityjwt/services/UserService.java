package io.javabrains.springsecurityjwt.services;


import io.javabrains.springsecurityjwt.dao.UserRepository;
import io.javabrains.springsecurityjwt.models.User;
import io.javabrains.springsecurityjwt.models.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
//    private final EmailSenderService emailSenderService;


    public void signUpUser(UserDto userDto) {
        if(!userDto.isMonitor()) {
            User link = getUser(userDto.getLinkedEmail());
            repository.deleteUserByEmail(link.getEmail());

            link.setLinkEmail(userDto.getEmail());
            repository.save(link);
        }
        User user = new User(userDto.getEmail(), userDto.getName(), userDto.isMonitor(), userDto.getLinkedEmail());
        repository.save(user);
    }


    public User getUser(String email) {
        return repository.findByEmail(email).get();
    }

    public void deleteUser(String email) {
        repository.deleteUserByEmail(email);
    }

    public void updateCor(String email, Float[] cooordiantes) {
        User user = getUser(email);
        if(user.isMonitor()) return;
        user.setCorX(cooordiantes[0]);
        user.setCorY(cooordiantes[1]);
        user.setLastSeen(LocalDateTime.now());
        repository.deleteUserByEmail(user.getEmail());
        repository.save(user);
    }

    public User getCor(String email) {
        User user = getUser(email);
        if(!user.isMonitor()) return null;
        return getUser(user.getLinkEmail());
    }
}
