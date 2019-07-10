package com.shariq.rediscachedemo.controller;

import com.shariq.rediscachedemo.model.User;
import com.shariq.rediscachedemo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#userId", unless = "#result.followers < 12000")
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID {}.", userId);
        Optional<User> user = userRepository.findById(Long.valueOf(userId));

        return user.orElseGet(() -> new User("User not Found", 0));

    }

    @GetMapping(value = "/all")
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
