package com.person.api.repository;

import com.person.api.entity.Phone;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PhoneRepository extends ReactiveCrudRepository<Phone, UUID> {
    Flux<Phone> findAllByUserId(UUID userId);
}