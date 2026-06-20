package com.hydrosmart.backend.iam.domain.model.repositories;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.iam.domain.model.valueobjects.Email;

import java.util.Optional;

public interface UserDomainRepository {
    Optional<User> findByEmail(Email email);
    Optional<User> findById(Long id);
    User save(User user);
    boolean existsByEmail(Email email);
}
