package dev.carshop.carshop.controller;

import dev.carshop.carshop.dto.LoginRequestDto;
import dev.carshop.carshop.dto.SignupUserDto;
import dev.carshop.carshop.model.UserModel;
import dev.carshop.carshop.service.JwtTokenService;
import dev.carshop.carshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private final JwtTokenService jwtTokenService;

    @Autowired
    private final AuthenticationManager authManager;

    public UserController(JwtTokenService jwtTokenService, AuthenticationManager authManager) {
        this.jwtTokenService = jwtTokenService;
        this.authManager = authManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupUserDto signupUserDto) throws Exception {
        UserModel user = userService.createUser(signupUserDto.getName(), signupUserDto.getEmail(), signupUserDto.getPassword());
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto userLogin) throws Exception {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        return jwtTokenService.generateToken(authentication);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.saveLastLogout(userEmail);
        return new ResponseEntity<>("User logged out.", HttpStatus.OK);
    }
}
