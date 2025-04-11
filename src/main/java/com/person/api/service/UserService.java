package com.person.api.service;

import com.person.api.dto.request.UserRequest;
import com.person.api.dto.response.UserResponse;
import com.person.api.entity.Phone;
import com.person.api.entity.User;
import com.person.api.repository.PhoneRepository;
import com.person.api.repository.UserRepository;
import com.person.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final JwtUtil jwtUtil;
    private final Environment env;

    public Mono<UserResponse> register(UserRequest request) {
        Pattern emailPattern = Pattern.compile(env.getProperty("user.email.pattern"));
        Pattern passwordPattern = Pattern.compile(env.getProperty("user.password.pattern"));

        if (!emailPattern.matcher(request.getEmail()).matches()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, env.getProperty("user.email.message")));
        }

        if (!passwordPattern.matcher(request.getPassword()).matches()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, env.getProperty("user.password.message")));
        }

        return userRepository.existsByEmail(request.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya registrado"));
                    }

                    UUID id = UUID.randomUUID();
                    String token = jwtUtil.generateToken(request.getEmail());
                    Instant now = Instant.now();

                    User user = User.builder()
                            .id(id)
                            .name(request.getName())
                            .email(request.getEmail())
                            .password(request.getPassword())
                            .created(java.time.LocalDateTime.now())
                            .modified(java.time.LocalDateTime.now())
                            .lastLogin(java.time.LocalDateTime.now())
                            .token(token)
                            .isActive(true)
                            .build();

                    return userRepository.save(user)
                            .flatMap(savedUser -> {
                                List<Phone> phones = request.getPhones().stream().map(p ->
                                        Phone.builder()
                                                .id(UUID.randomUUID())
                                                .number(p.getNumber())
                                                .cityCode(p.getCitycode())
                                                .contryCode(p.getContrycode())
                                                .userId(savedUser.getId())
                                                .build()
                                ).toList();

                                return phoneRepository.saveAll(phones).then(Mono.just(savedUser));
                            })
                            .map(saved -> UserResponse.builder()
                                    .id(saved.getId())
                                    .created(saved.getCreated())
                                    .modified(saved.getModified())
                                    .lastLogin(saved.getLastLogin())
                                    .token(saved.getToken())
                                    .isActive(saved.isActive())
                                    .build());
                });
    }
}

