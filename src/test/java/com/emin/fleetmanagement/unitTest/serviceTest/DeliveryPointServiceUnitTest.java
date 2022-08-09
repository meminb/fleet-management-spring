package com.emin.fleetmanagement.unitTest.serviceTest;

import com.emin.fleetmanagement.MessageConstants;
import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.service.concreates.DeliveryPointServiceImp;
import com.emin.fleetmanagement.testUtils.TestDummies;
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
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)

public class DeliveryPointServiceUnitTest {

    @InjectMocks
    DeliveryPointServiceImp deliveryPointServiceImp;


    @Mock
    DeliveryPointRepository deliveryPointRepository;

    @Test
    public void givenDeliveryPointTypeDoesNoExist_whenSaveCalledProperly_thenReturnErrorResponse() {

        int incorrectTypeOrdinal = 999;
        Result actualResult = deliveryPointServiceImp.save(TestDummies.DELIVERY_POINT_NAME, incorrectTypeOrdinal);

        assert !actualResult.isSuccess();
        Assertions.assertEquals(MessageConstants.INCORRECT_DELIVERY_POINT_TYPE_MESSAGE, actualResult.getMessage());

    }

    @Test
    public void givenItemNotExist_whenCallingFindByID_thenReturnErrorResponse() {

        Mockito.when(deliveryPointRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Result result = deliveryPointServiceImp.findById(999l);
        ErrorDataResult expectedResult = new ErrorDataResult(MessageConstants.DELIVERY_POINT_NOT_FOUND_MESSAGE);

        Assertions.assertTrue(result.getMessage().contains(expectedResult.getMessage()));
        Assertions.assertFalse(result.isSuccess());

    }


}
