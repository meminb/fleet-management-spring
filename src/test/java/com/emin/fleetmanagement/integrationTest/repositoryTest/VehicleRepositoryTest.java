package com.emin.fleetmanagement.integrationTest.repositoryTest;

import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.repository.VehicleRepository;
import com.emin.fleetmanagement.testUtils.fluentBuilders.VehicleBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

class VehicleRepositoryTest {


    @Autowired
    private VehicleRepository vehicleRepository;

    private PostgreSQLContainer postgreSQLContainer;



    @Test
    public void saveVehicleTest()  {

        Vehicle vehicle = new VehicleBuilder().build();
        vehicleRepository.save(vehicle);

        List<Vehicle> vehicleList = vehicleRepository.findAllById(Collections.singleton(vehicle.getPlate()));

        for (Vehicle vehc : vehicleList) {
            Assertions.assertEquals(vehicle,vehc);
        }

    }


}