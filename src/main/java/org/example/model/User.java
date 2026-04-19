package org.example.model;
import lombok.*;
@Getter
@Setter
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    private String id;
    private String login;
    private String password;
    private String role;

    public User copy() {
        return User.builder()
                .id(id)
                .login(login)
                .password(password)
                .role(role)
                .build();
    }
}
