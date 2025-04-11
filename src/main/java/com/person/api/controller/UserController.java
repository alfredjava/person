package com.person.api.controller;

import com.person.api.dto.request.UserRequest;
import com.person.api.dto.response.UserResponse;
import com.person.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> register(@RequestBody @Valid UserRequest request) {
        return userService.register(request)
                .map(userResponse -> ResponseEntity.status(HttpStatus.CREATED).body(userResponse))
                .doOnError(error -> Mono.error(new Exception("Error creating user")))
                .doOnError(error -> {
                        log.error("Error controller userSave: {}", error.getMessage());
                    Mono.error(new Exception("Error controller userSave"));
                });
    }

}

