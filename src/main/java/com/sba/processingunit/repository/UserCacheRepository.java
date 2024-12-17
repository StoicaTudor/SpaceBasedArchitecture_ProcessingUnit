package com.sba.processingunit.repository;

import com.hazelcast.map.IMap;
import com.sba.processingunit.cache.CacheType;
import com.sba.processingunit.cache.HazelcastClientGenerator;
import com.sba.processingunit.data.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class UserCacheRepository implements UserRepository {
    @Override
    public List<User> getAllUsers() {
        return HazelcastClientGenerator.getClient()
                .getMap(CacheType.USER.value())
                .values()
                .stream()
                .map(User.class::cast)
                .toList();
    }

    @Override
    public User getUserByID(String id) {
        return (User) HazelcastClientGenerator.getClient()
                .getMap(CacheType.USER.value())
                .get(id);
    }

    @Override
    public User updateUser(User user) {
        HazelcastClientGenerator.getClient()
                .getMap(CacheType.USER.value())
                .replace(user.id(), user);
        return user;
    }

    @Override
    public void replaceAllUsers(List<User> users) {
        IMap<String, User> userMap = HazelcastClientGenerator.getClient().getMap(CacheType.USER.value());
        userMap.clear();
        userMap.putAll(users.stream().collect(Collectors.toMap(User::id, Function.identity())));
    }

    @Override
    public User createUser(User user) {
        HazelcastClientGenerator.getClient()
                .getMap(CacheType.USER.value())
                .put(user.id(), user);
        return user;
    }

    @Override
    public String deleteUser(String id) {
        HazelcastClientGenerator.getClient()
                .getMap(CacheType.USER.value())
                .delete(id);
        return id;
    }
}
