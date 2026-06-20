package com.hydrosmart.backend.iam.infrastructure.persistence.jpa.adapters;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.iam.domain.model.repositories.UserDomainRepository;
import com.hydrosmart.backend.iam.domain.model.valueobjects.Email;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.mappers.UserPersistenceMapper;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserDomainRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value()).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserPersistenceMapper.toDomain(jpaRepository.save(UserPersistenceMapper.toJpaEntity(user)));
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }
}
