package com.emin.fleetmanagement.repository;

import com.emin.fleetmanagement.model.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
