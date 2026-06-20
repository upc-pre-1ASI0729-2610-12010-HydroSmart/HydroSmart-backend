package com.hydrosmart.backend.iam.application.internal.commandservices;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.iam.domain.model.repositories.UserDomainRepository;
import com.hydrosmart.backend.iam.domain.model.valueobjects.Email;
import com.hydrosmart.backend.iam.domain.model.valueobjects.HashedPassword;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources.LoginRequestResource;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.resources.LoginResponseResource;
import com.hydrosmart.backend.iam.infrastructure.interfaces.rest.transform.UserResourceAssembler;
import com.hydrosmart.backend.shared.exception.ResourceNotFoundException;
import com.hydrosmart.backend.shared.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserDomainRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseResource authenticate(LoginRequestResource request) {
        Email email = new Email(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash().value())) {
            throw new ResourceNotFoundException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getEmail().value(), user.getRole().value());
        return new LoginResponseResource(token, UserResourceAssembler.toResource(user));
    }

    public User getByEmail(String emailValue) {
        return userRepository.findByEmail(new Email(emailValue))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public HashedPassword hashPassword(String raw) {
        return new HashedPassword(passwordEncoder.encode(raw));
    }
}
