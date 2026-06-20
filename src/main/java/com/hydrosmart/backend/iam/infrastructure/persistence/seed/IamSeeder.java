package com.hydrosmart.backend.iam.infrastructure.persistence.seed;

import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.entities.UserJpaEntity;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.entities.UserRole;
import com.hydrosmart.backend.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Seeder del bounded context IAM.
 * Se ejecuta PRIMERO (Order 1) porque los demás bounded contexts
 * necesitan usuarios ya creados (admin y tenants) para poder
 * vincular sus propios datos (edificios, unidades, etc).
 *
 * Cada bounded context tiene su propio seeder con @Order para evitar
 * que todo el equipo edite un único DataSeeder.java compartido.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class IamSeeder implements ApplicationRunner {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("[IAM] Users already seeded, skipping.");
            return;
        }
        log.info("[IAM] Seeding users...");

        String adminHash = passwordEncoder.encode("admin123");
        String tenantHash = passwordEncoder.encode("tenant123");

        userRepository.save(UserJpaEntity.builder()
                .name("Carlos").lastName("Mendoza").email("admin@begonias.com.pe")
                .passwordHash(adminHash).phone("+51 987 654 321").role(UserRole.BUILDING_ADMIN)
                .street("Av. Javier Prado 1234").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-01-15T00:00:00")).build());

        userRepository.save(UserJpaEntity.builder()
                .name("Arianna").lastName("Flores").email("arianna.flores@begonias.com.pe")
                .passwordHash(tenantHash).phone("+51 912 345 678").role(UserRole.TENANT)
                .street("Av. Javier Prado 1234, Dpto 1A").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-03-10T00:00:00")).build());

        userRepository.save(UserJpaEntity.builder()
                .name("Juan").lastName("Pérez").email("juan.perez@begonias.com.pe")
                .passwordHash(tenantHash).phone("+51 913 456 789").role(UserRole.TENANT)
                .street("Av. Javier Prado 1234, Dpto 1B").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-03-15T00:00:00")).build());

        userRepository.save(UserJpaEntity.builder()
                .name("María").lastName("García").email("maria.garcia@begonias.com.pe")
                .passwordHash(tenantHash).phone("+51 914 567 890").role(UserRole.TENANT)
                .street("Av. Javier Prado 1234, Dpto 2A").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-04-01T00:00:00")).build());

        userRepository.save(UserJpaEntity.builder()
                .name("Luis").lastName("Torres").email("luis.torres@begonias.com.pe")
                .passwordHash(tenantHash).phone("+51 915 678 901").role(UserRole.TENANT)
                .street("Av. Javier Prado 1234, Dpto 2B").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-04-10T00:00:00")).build());

        userRepository.save(UserJpaEntity.builder()
                .name("Sofía").lastName("Quispe").email("sofia.quispe@begonias.com.pe")
                .passwordHash(tenantHash).phone("+51 916 789 012").role(UserRole.TENANT)
                .street("Av. Javier Prado 1234, Dpto 3A").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-05-01T00:00:00")).build());

        userRepository.save(UserJpaEntity.builder()
                .name("Diego").lastName("Ramírez").email("diego.ramirez@begonias.com.pe")
                .passwordHash(tenantHash).phone("+51 917 890 123").role(UserRole.TENANT)
                .street("Av. Javier Prado 1234").city("Lima").district("San Isidro").country("Perú")
                .isActive(true).createdAt(ts("2024-05-20T00:00:00")).build());

        log.info("[IAM] Users seeded successfully (7 users).");
    }

    private Instant ts(String iso) {
        return LocalDateTime.parse(iso).toInstant(ZoneOffset.UTC);
    }
}
