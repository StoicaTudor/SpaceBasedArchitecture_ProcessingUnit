package com.sba.processingunit.ingress_communication.handlers;

import com.sba.processingunit.data.User;
import com.sba.processingunit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataReaderUserHandler {
    private final UserRepository userRepository;

    public DataReaderUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void replaceAllUsersInCache(List<User> users) {
        userRepository.replaceAllUsers(users);
    }
}
