package com.david.monitoring.users;

import com.david.monitoring.entities.User;
import com.david.monitoring.users.dto.CreateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public User createUser(CreateUserRequest request, String passwordHash) {

        List<String> errors = new ArrayList<>();

        if (request.username() == null || !USERNAME_PATTERN.matcher(request.username()).matches()) {
            errors.add("Username must be 3-50 alphanumeric characters or underscores");
        }

        if (request.email() == null || !request.email().contains("@")) {
            errors.add("Email is not valid");
        }

        if (request.password() == null || request.password().length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        if (userRepository.existsByUsername(request.username())) {
            errors.add("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            errors.add("Email already exists");
        }

        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }

        User user = new User(
                request.username(),
                request.email(),
                passwordHash
        );

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
