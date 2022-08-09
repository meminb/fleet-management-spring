package com.emin.fleetmanagement.unitTest.serviceTest;


import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.repository.DeliveryRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryItemService;
import com.emin.fleetmanagement.service.abstracts.DeliveryPointService;
import com.emin.fleetmanagement.service.concreates.DeliveryServiceImp;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryItemBuilder;
import com.emin.fleetmanagement.utils.State;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.SuccessDataResult;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.emin.fleetmanagement.testUtils.TestDummies.BAG_BARCODE_1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

public class DeliveryServiceUnitTest {


    @InjectMocks
    DeliveryServiceImp deliveryService;

    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    DeliveryItemService deliveryItemService;

    @Mock
    DeliveryPointService deliveryPointService;


    @Test
    public void whenDeliveryCreatedProperly_thenReturnSuccessResultAndExpectItemsUpdatedProperly() throws Exception {

        Mockito.when(deliveryPointService.findById(TestDummies.DELIVERY_POINT_ID_2)).thenReturn(new SuccessDataResult<>(TestDummies.DELIVERY_POINT_2));

        List<String> barcodes = new ArrayList<>(Arrays.asList(BAG_BARCODE_1, TestDummies.PACKAGE_BARCODE_3));
        mockForSuccessfullySave(TestDummies.BAG_BARCODE_1,TestDummies.PACKAGE_BARCODE_3,TestDummies.DELIVERY_POINT_2);

        DataResult<Delivery> actualResult = deliveryService.unloadAndSaveDelivery(TestDummies.DELIVERY_POINT_ID_2, barcodes,null);

        Assertions.assertTrue(actualResult.isSuccess());
        Assertions.assertTrue(actualResult.isSuccess());
        assertAll("All status should bu unloaded.",
                () -> assertEquals(State.UNLOADED, actualResult.getData().getDeliveryItems().get(0).getState()),
                () -> assertEquals(State.UNLOADED, actualResult.getData().getDeliveryItems().get(1).getState()));
    }

    @Test
    public void whenSomeItemsInsideDeliveryAreInappropriateForUnloading_thenReturnSuccessResultAndExpectItemsUpdatedProperly() throws Exception {

        Mockito.when(deliveryPointService.findById(TestDummies.DELIVERY_POINT_ID_1)).thenReturn(new SuccessDataResult<>(TestDummies.DELIVERY_POINT_1));

        List<String> barcodes = new ArrayList<>(Arrays.asList(BAG_BARCODE_1, TestDummies.PACKAGE_BARCODE_3));
        mockForHalfSuccessfullySave(TestDummies.BAG_BARCODE_1,TestDummies.PACKAGE_BARCODE_3,TestDummies.DELIVERY_POINT_1);

        DataResult<Delivery> actualResult = deliveryService.unloadAndSaveDelivery(TestDummies.DELIVERY_POINT_ID_1, barcodes,null);

        Assertions.assertTrue(actualResult.isSuccess());
        Assertions.assertTrue(actualResult.isSuccess());
        assertAll("Package status should be UNLOADED when Bag's status still LOADED.",
                () -> assertEquals(State.LOADED, actualResult.getData().getDeliveryItems().get(0).getState()),
                () -> assertEquals(State.UNLOADED, actualResult.getData().getDeliveryItems().get(1).getState()));
    }



    private void mockForHalfSuccessfullySave(String barcode1, String barcode2,DeliveryPoint unloadingPoint) throws Exception {
        mockSetStatusMethod(barcode1,unloadingPoint,TestDummies.LOADED_STATUS,TestDummies.UNLOADED_STATUS); // expected to not reach here
        mockSetStatusMethod(barcode2,unloadingPoint,TestDummies.LOADED_STATUS,TestDummies.UNLOADED_STATUS);

    }


    private void mockForSuccessfullySave(String barcode1, String barcode2, DeliveryPoint unloadingPoint) throws Exception {
        mockSetStatusMethod(barcode1,unloadingPoint,TestDummies.LOADED_STATUS,TestDummies.UNLOADED_STATUS);
        mockSetStatusMethod(barcode2,unloadingPoint,TestDummies.LOADED_STATUS,TestDummies.UNLOADED_STATUS);

    }


    private void mockSetStatusMethod(String barcode, DeliveryPoint unloadingPoint, State afterLoaded, State afterUnloaded) throws Exception {

        if(barcode.charAt(0) == 'C'){
            // private load method
            DataResult stubResponse1 = new SuccessDataResult(new DeliveryItemBuilder().withStatus(afterLoaded).
                    withBarcode(barcode).withUnloadingPoint(unloadingPoint).build());
            Mockito.when(deliveryItemService.setStatus(barcode, TestDummies.LOADED_STATUS.ordinal())).
                    thenReturn(stubResponse1);
            // private unload method
            DataResult stubResponse2 = new SuccessDataResult(new DeliveryItemBuilder().withStatus(afterUnloaded).
                    withBarcode(barcode).withUnloadingPoint(unloadingPoint).build());
            Mockito.when(deliveryItemService.setStatus(barcode, TestDummies.UNLOADED_STATUS.ordinal())).
                    thenReturn(stubResponse2);
        } else if ((barcode.charAt(0) == 'P')) {
            DataResult stubResponse1 = new SuccessDataResult(new DeliveryItemBuilder().withStatus(afterLoaded).
                    withBarcode(barcode).withUnloadingPoint(unloadingPoint).build());
            Mockito.when(deliveryItemService.setStatus(barcode, TestDummies.LOADED_STATUS.ordinal())).
                    thenReturn(stubResponse1);
            // private unload method
            DataResult stubResponse2 = new SuccessDataResult(new DeliveryItemBuilder().withStatus(afterUnloaded).
                    withBarcode(barcode).withUnloadingPoint(unloadingPoint).build());
            Mockito.when(deliveryItemService.setStatus(barcode, TestDummies.UNLOADED_STATUS.ordinal())).
                    thenReturn(stubResponse2);
        }


    }


}
