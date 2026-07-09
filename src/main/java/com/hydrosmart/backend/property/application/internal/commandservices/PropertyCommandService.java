package com.hydrosmart.backend.property.application.internal.commandservices;

import com.hydrosmart.backend.iam.domain.model.aggregates.User;
import com.hydrosmart.backend.iam.domain.model.repositories.UserDomainRepository;
import com.hydrosmart.backend.iam.domain.model.valueobjects.Email;
import com.hydrosmart.backend.iam.domain.model.valueobjects.HashedPassword;
import com.hydrosmart.backend.iam.domain.model.valueobjects.UserRoleVO;
import com.hydrosmart.backend.property.domain.model.aggregates.Building;
import com.hydrosmart.backend.property.domain.model.aggregates.Unit;
import com.hydrosmart.backend.property.domain.model.repositories.BuildingDomainRepository;
import com.hydrosmart.backend.property.domain.model.repositories.UnitDomainRepository;
import com.hydrosmart.backend.property.infrastructure.interfaces.rest.resources.AssignTenantRequestResource;
import com.hydrosmart.backend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyCommandService {

    private final BuildingDomainRepository buildingRepository;
    private final UnitDomainRepository unitRepository;
    private final UserDomainRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Building getBuilding(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado: " + id));
    }

    public List<Unit> getUnitsByBuilding(Long buildingId) {
        return unitRepository.findByBuildingId(buildingId);
    }

    public Unit getUnit(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada: " + id));
    }

    public User getTenantOfUnit(Long unitId) {
        Unit unit = getUnit(unitId);
        if (unit.isVacant()) {
            throw new ResourceNotFoundException("Esta unidad no tiene inquilino asignado");
        }
        return userRepository.findById(unit.getTenantUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Inquilino no encontrado"));
    }

    public String resolveTenantName(Long tenantUserId) {
        if (tenantUserId == null) return "";
        return userRepository.findById(tenantUserId)
                .map(User::fullName)
                .orElse("");
    }

    /** Resuelve la unidad asignada a un tenant a partir de su email (tenant-safe). */
    public Optional<Unit> getUnitForTenantEmail(String email) {
        return userRepository.findByEmail(new Email(email))
                .flatMap(u -> unitRepository.findByTenantUserId(u.getId()));
    }

    /** Devuelve el id de usuario a partir de su email. */
    public Optional<Long> getUserIdByEmail(String email) {
        return userRepository.findByEmail(new Email(email)).map(User::getId);
    }

    @Transactional
    public Unit assignTenant(Long unitId, AssignTenantRequestResource request) {
        Unit unit = getUnit(unitId);
        if (!unit.isVacant()) {
            throw new IllegalStateException("Unidad ya ocupada");
        }
        Email emailVO = new Email(request.email());
        if (userRepository.existsByEmail(emailVO)) {
            throw new IllegalStateException("El email ya está registrado");
        }

        User tenant = User.create(
                request.name(),
                request.lastName(),
                emailVO,
                new HashedPassword(passwordEncoder.encode(request.password())),
                request.phone(),
                new UserRoleVO("TENANT")
        );
        tenant = userRepository.save(tenant);

        return unitRepository.save(unit.withTenant(tenant.getId()));
    }

    @Transactional
    public Unit removeTenant(Long unitId) {
        Unit unit = getUnit(unitId);
        return unitRepository.save(unit.withoutTenant());
    }
}
