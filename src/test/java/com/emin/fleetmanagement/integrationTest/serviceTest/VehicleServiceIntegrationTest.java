package com.emin.fleetmanagement.integrationTest.serviceTest;


import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.service.abstracts.VehicleService;
import com.emin.fleetmanagement.utils.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.emin.fleetmanagement.testUtils.TestDummies.MODEL_1;
import static com.emin.fleetmanagement.testUtils.TestDummies.PLATE_1;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VehicleServiceIntegrationTest {



    @Autowired
    VehicleService vehicleService;



    @Test
    void whenVehicleSavedProperly_thenResultIsSuccessResult(){

        Result result = vehicleService.save(PLATE_1,MODEL_1);
        assert result.isSuccess();
    }

    @Test
    void whenVehicleFoundWithGivenId_thenResultIsSuccessDataResult(){

        vehicleService.save(PLATE_1,MODEL_1);

        Result result = vehicleService.findById(PLATE_1);
        assert result.isSuccess();
    }




}
