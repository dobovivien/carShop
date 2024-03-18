package dev.carshop.carshop.repository;

import dev.carshop.carshop.model.UserModel;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    List<UserModel> userList = new ArrayList<>();

    @PostConstruct
    public void initUsers() {
        UserModel user1 = new UserModel("1", "Hester Stokes", "hosterstrokes@test.com", "abLhCKpGGQ", null);
        UserModel user2 = new UserModel("2", "Monica Munoz", "monicamunoz@test.com", "vrTHt5HPqt", null);
        UserModel user3 = new UserModel("3", "Federico Ray", "federicoray@test.com", "57NKkmE32L", null);
        UserModel user4 = new UserModel("4", "Leroy Richards", "leroyrichards@test.com", "mpmUnAmyzp", null);
        UserModel user5 = new UserModel("5", "Letitia Nicholson", "letitianicholson@test.com", "75dMMNANah", null);
        UserModel user6 = new UserModel("6", "Huey Washington", "hueywashington@test.com", "n6qYjVYYGs", null);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);
    }

    public List<UserModel> getAllUsers() {
        return userList;
    }

    public void addNewUser(UserModel newUser) {
        userList.add(newUser);
    }

    public void saveLastLogout(String userEmail) {
        userList.stream()
                .filter(user -> user.getEmail().equals(userEmail))
                .forEach(user -> user.setLastLogoutTime(LocalDateTime.now()));
    }
}
