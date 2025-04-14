package com.person.api.service;

import com.person.api.dto.request.UserRequest;
import com.person.api.dto.response.UserResponse;
import com.person.api.model.Phone;
import com.person.api.model.User;
import com.person.api.repository.PhoneRepository;
import com.person.api.repository.UserRepository;
import com.person.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final JwtUtil jwtUtil;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

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
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, env.getProperty("user.email.exist.message")));
                    }

                    LocalDateTime now = java.time.LocalDateTime.now();

                    User user = User.builder()
                            //.id(id)
                            .name(request.getName())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .created(now)
                            .modified(now)
                            .lastLogin(now)
                            .token(jwtUtil.generateToken(request.getEmail()))
                            .isActive(true)
                            .build();

                    return userRepository.save(user)
                            .flatMap(savedUser -> {
                                List<Phone> phones = request.getPhones().stream().map(p ->
                                        Phone.builder()
                                                //.id(UUID.randomUUID())
                                                .number(p.getNumber())
                                                .cityCode(p.getCityCode())
                                                .countryCode(p.getCountryCode())
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

    public Mono<String> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo no registrado")))
                .flatMap(user -> {
                    if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                        return Mono.just(jwtUtil.generateToken(user.getEmail()));
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta"));
                    }
                });
    }
}

