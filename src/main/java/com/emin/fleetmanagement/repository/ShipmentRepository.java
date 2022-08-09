package com.emin.fleetmanagement.repository;

import com.emin.fleetmanagement.model.delivery.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
