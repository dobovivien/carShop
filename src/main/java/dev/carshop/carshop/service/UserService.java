package dev.carshop.carshop.service;

import dev.carshop.carshop.model.UserModel;
import dev.carshop.carshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    @Autowired
    private UserRepository userRepository;

    public List<UserModel> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserModel createUser(String name, String email, String password) throws Exception {
        if (name == null || email == null || password == null) {
            throw new Exception("Name, email and password fields can't be empty.");
        }
        if (name.length() > 50) {
            throw new Exception("The name can't be more than 50 characters long.");
        }
        Optional<String> matchingEmail = getAllUsers().stream()
                .map(UserModel::getEmail)
                .filter(userEmail -> userEmail.equals(email))
                .findAny();
        if (matchingEmail.isPresent()) {
            throw new Exception("The given email address already exists.");
        }
        if (validate(email, EMAIL_PATTERN)) {
            throw new Exception("The format of the given email is not correct.");
        }
        if (validate(password, PASSWORD_PATTERN)) {
            throw new Exception("The password should be at least 8 characters and contain at least one lower case and upper case letter and one number.");
        }
        String id = UUID.randomUUID().toString();
        UserModel newUser =  new UserModel(id, name, email, password, null);
        userRepository.addNewUser(newUser);
        return newUser;
    }

    public boolean validate(String text, String textPattern) {
        Pattern pattern = Pattern.compile(textPattern);
        Matcher matcher = pattern.matcher(text);
        return !matcher.matches();
    }

    public void saveLastLogout(String userEmail) {
        userRepository.saveLastLogout(userEmail);
    }
}
