package com.lucascamilo.fit_strike_api.domain.port;

import com.lucascamilo.fit_strike_api.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    void deleteById(String id);
}
