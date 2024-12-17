package com.sba.processingunit.data;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public record User(@Id String id, String name, double balance) implements Serializable {
    public User setID(String id) {
        return new User(id, name, balance);
    }
}
