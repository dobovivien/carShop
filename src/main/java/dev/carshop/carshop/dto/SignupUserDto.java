package dev.carshop.carshop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupUserDto {

    String name;

    String email;

    String password;
}
