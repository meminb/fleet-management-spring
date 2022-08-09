package com.emin.fleetmanagement.unitTest.serviceTest;

import com.emin.fleetmanagement.MessageConstants;
import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.repository.VehicleRepository;
import com.emin.fleetmanagement.service.concreates.VehicleServiceImp;
import com.emin.fleetmanagement.utils.result.ErrorDataResult;
import com.emin.fleetmanagement.utils.result.Result;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

public class VehicleServiceUnitTest {

    @InjectMocks
    VehicleServiceImp vehicleServiceImp;

    @Mock
    VehicleRepository vehicleRepository;

    @Test
    public void givenItemNotExist_whenCallingFindByID_thenReturnErrorResponse(){

        Mockito.when(vehicleRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Result result = vehicleServiceImp.findById("non existing plate");
        ErrorDataResult expectedResult= new ErrorDataResult(MessageConstants.VEHICLE_NOT_FOUND_MESSAGE);

        Assertions.assertTrue(result.getMessage().contains(expectedResult.getMessage()));
        Assertions.assertFalse(result.isSuccess());

    }

}
