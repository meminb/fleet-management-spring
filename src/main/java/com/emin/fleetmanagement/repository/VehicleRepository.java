package com.emin.fleetmanagement.repository;

import com.emin.fleetmanagement.model.delivery.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VehicleRepository extends JpaRepository<Vehicle,String> {
}
