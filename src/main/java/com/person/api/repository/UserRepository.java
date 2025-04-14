package com.person.api.repository;

import com.person.api.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

    Mono<Boolean> existsByEmail(String email);
    Mono<User> findByEmail(String email);
}



