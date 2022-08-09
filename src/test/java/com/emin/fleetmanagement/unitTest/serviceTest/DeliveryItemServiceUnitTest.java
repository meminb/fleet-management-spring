package com.emin.fleetmanagement.unitTest.serviceTest;


import com.emin.fleetmanagement.MessageConstants;
import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.repository.DeliveryItemRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryItemService;
import com.emin.fleetmanagement.service.abstracts.DeliveryPointService;
import com.emin.fleetmanagement.service.concreates.DeliveryItemServiceImp;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.utils.result.ErrorDataResult;
import com.emin.fleetmanagement.utils.result.Result;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

public class DeliveryItemServiceUnitTest {




    @Autowired
    DeliveryItemService deliveryItemService;
    @InjectMocks
    DeliveryItemServiceImp deliveryItemServiceImp;
    @Mock
    DeliveryItemRepository deliveryItemRepository;
    @Mock
    DeliveryPointService deliveryPointService;

    @Test
    public void givenMismatchedBarcodesForItems_whenSetStatus_thenReturnErrorResult() throws Exception {

        Mockito.when(deliveryItemRepository.findById(Mockito.any())).thenReturn(Optional.of(TestDummies.BAG_1));

        Result result = deliveryItemServiceImp.setStatus(TestDummies.BAG_BARCODE_1,TestDummies.LOADED_TO_BAG_STATUS.ordinal());


        Assertions.assertTrue(result.getMessage().contains(MessageConstants.BAGS_CANNOT_BE_LOADED_TO_BAGS_MESSAGE));
        Assertions.assertFalse(result.isSuccess());

    }


    @Test
    public void givenCorrectBarcodesForItems_whenSetStatus_thenReturnSuccessResult() throws Exception {

        Mockito.when(deliveryItemRepository.findById(Mockito.any())).thenReturn(Optional.of(TestDummies.PACKAGE_3));

        Result result = deliveryItemServiceImp.setStatus(TestDummies.PACKAGE_BARCODE_3,TestDummies.LOADED_TO_BAG_STATUS.ordinal());


        Assertions.assertEquals(MessageConstants.ITEM_UPDATED_SUCCESSFULLY_MESSAGE,result.getMessage());
        Assertions.assertTrue(result.isSuccess());

    }



    @Test
    public void whenAssignBagToBag_thenReturnErrorResponse(){

        Mockito.when(deliveryItemRepository.findById(Mockito.any())).thenReturn(Optional.of(TestDummies.BAG_1));
        Mockito.when(deliveryItemRepository.save(Mockito.any())).thenReturn(new DeliveryItem());

        Result result = deliveryItemServiceImp.assignPackageToBag(TestDummies.BAG_BARCODE_1,TestDummies.BAG_BARCODE_1);

        Assertions.assertEquals(MessageConstants.MISMATCHED_BAG_PACKAGE_BARCODE_MESSAGE,result.getMessage());
        Assertions.assertFalse(result.isSuccess());

    }


    @Test
    public void givenNoItemFound_whenFindByBarcode_thenReturnErrorResponse(){
        Result result = deliveryItemServiceImp.findByBarcode(TestDummies.BAG_BARCODE_1);
        ErrorDataResult expectedResult= new ErrorDataResult(MessageConstants.DELIVERY_ITEM_NOT_FOUND_MESSAGE);

        assertThat(result.getMessage(),containsString(expectedResult.getMessage()));
        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    public void givenNoDeliveryPointFound_whenSaveBag_thenReturnErrorResponse(){
        Mockito.when(deliveryPointService.findById(Mockito.any())).thenReturn(new ErrorDataResult<>());

        Result result = deliveryItemServiceImp.saveBag(TestDummies.BAG_BARCODE_1,TestDummies.DELIVERY_POINT_ID_1);
        ErrorDataResult expectedResult= new ErrorDataResult(MessageConstants.DELIVERY_POINT_NOT_FOUND_MESSAGE);

        assertThat(result.getMessage(),containsString(expectedResult.getMessage()));
        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    public void givenNoDeliveryPointFound_whenSavePackage_thenReturnErrorResponse(){
        Mockito.when(deliveryPointService.findById(Mockito.any())).thenReturn(new ErrorDataResult<>());

        Result result = deliveryItemServiceImp.savePackage(TestDummies.PACKAGE_BARCODE_3,TestDummies.DELIVERY_POINT_ID_1,TestDummies.WEIGHT);
        ErrorDataResult expectedResult= new ErrorDataResult(MessageConstants.DELIVERY_POINT_NOT_FOUND_MESSAGE);

        assertThat(result.getMessage(),containsString(expectedResult.getMessage()));
        Assertions.assertFalse(result.isSuccess());
    }

}
