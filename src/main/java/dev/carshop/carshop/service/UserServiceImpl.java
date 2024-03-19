package dev.carshop.carshop.service;

import dev.carshop.carshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> user = userRepository.getAllUsers().stream()
                .map(userDetails -> User.withUsername(userDetails.getEmail())
                        .password("{noop}" + userDetails.getPassword())
                        .authorities("read", "write", "delete")
                        .build())
                .filter(userDetails -> userDetails.getUsername().equals(username))
                .findAny();

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
