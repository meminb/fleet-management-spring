package com.emin.fleetmanagement.repository;

import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Long> {
}
