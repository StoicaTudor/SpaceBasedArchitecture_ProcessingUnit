package com.sba.processingunit.repository;

import com.sba.processingunit.data.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User getUserByID(String id);

    User updateUser(User user);

    User createUser(User user);

    String deleteUser(String id);

    void replaceAllUsers(List<User> users);
}
