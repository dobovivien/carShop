package dev.carshop.carshop.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {

    private String id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime lastLogoutTime;
}
