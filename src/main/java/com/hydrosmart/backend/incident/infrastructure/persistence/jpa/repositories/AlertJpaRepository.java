package com.hydrosmart.backend.incident.infrastructure.persistence.jpa.repositories;

import com.hydrosmart.backend.incident.infrastructure.persistence.jpa.entities.AlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertJpaRepository extends JpaRepository<AlertJpaEntity, Long> {

    List<AlertJpaEntity> findBySensorIdIn(List<Long> sensorIds);

    @Query("SELECT a FROM AlertJpaEntity a WHERE a.sensor.id IN :sensorIds AND a.status IN ('active','acknowledged')")
    List<AlertJpaEntity> findActiveAlertsBySensorIds(@Param("sensorIds") List<Long> sensorIds);

    List<AlertJpaEntity> findBySensorIdInAndStatus(List<Long> sensorIds, String status);

    long countBySensorIdAndStatusNot(Long sensorId, String status);
}
