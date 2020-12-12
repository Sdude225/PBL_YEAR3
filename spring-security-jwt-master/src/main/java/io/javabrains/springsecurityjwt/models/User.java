package io.javabrains.springsecurityjwt.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @MongoId
    private String id;
    private String email;
    private String name;
    private boolean monitor;
    private String linkEmail;

    private float corX = 0f;
    private float corY = 0f;

    private LocalDateTime lastSeen = LocalDateTime.now();

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(String email, String name, boolean monitor, String linkEmail) {
        this.email = email;
        this.name = name;
        this.monitor = monitor;
        this.linkEmail = linkEmail;
    }

    public String getLSeen() {
        return lastSeen.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
