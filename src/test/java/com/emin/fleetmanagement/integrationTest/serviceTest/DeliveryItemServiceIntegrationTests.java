package com.emin.fleetmanagement.integrationTest.serviceTest;


import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryItemService;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.utils.State;
import com.emin.fleetmanagement.utils.result.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.emin.fleetmanagement.MessageConstants.*;
import static com.emin.fleetmanagement.testUtils.TestDummies.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class DeliveryItemServiceIntegrationTests {

    @Autowired
    DeliveryItemService deliveryItemService;

    @Autowired
    DeliveryPointRepository deliveryPointRepository;


    @BeforeEach
    void saveItems() {

        deliveryPointRepository.save(DELIVERY_POINT_1);
        deliveryItemService.savePackage(PACKAGE_IN_BAG_BARCODE_1, DELIVERY_POINT_ID_1, WEIGHT);
        deliveryItemService.saveBag(BAG_BARCODE_1, DELIVERY_POINT_ID_1);
    }


    @Test
    void givenPackageAndBagExistInDb_whenAssigningCorrectly_thenBothEntityShouldBeUpdatedAndReturnSuccessResponse() {

        Result result = deliveryItemService.assignPackageToBag(PACKAGE_IN_BAG_BARCODE_1, BAG_BARCODE_1);
        Result expected = new SuccessResult(ITEM_UPDATED_SUCCESSFULLY_MESSAGE);

        DataResult<Package> updatedPack =deliveryItemService.findByBarcode(PACKAGE_IN_BAG_BARCODE_1);
        DataResult<Bag> updatedBag =deliveryItemService.findByBarcode(BAG_BARCODE_1);

        Assertions.assertTrue(updatedBag.getData().getPackages().contains(updatedPack.getData()));
        Assertions.assertEquals(updatedPack.getData().getBag(),updatedBag.getData());

        Assertions.assertEquals(expected.getMessage(), result.getMessage());
        Assertions.assertInstanceOf(SuccessResult.class, result);

    }


    @Test
    void givenPackageAndBagExistInDb_whenAssigningIncorrectly_thenReturnErrorResponse() {

        Result result = deliveryItemService.assignPackageToBag(BAG_BARCODE_1, PACKAGE_IN_BAG_BARCODE_1);
        Result expected = new ErrorResult(MISMATCHED_BAG_PACKAGE_BARCODE_MESSAGE);

        Assertions.assertEquals(expected.getMessage(), result.getMessage());
        Assertions.assertInstanceOf(ErrorResult.class, result);

    }

    @Test
    void givenPackageOrBagNotExistInDb_whenAssigningBetweenEachOther_thenReturnErrorResponse() {

        Result result = deliveryItemService.assignPackageToBag(BAG_BARCODE_1, PACKAGE_BARCODE_3);

        Result expected = new ErrorResult(DELIVERY_ITEM_NOT_FOUND_MESSAGE);

        Assertions.assertTrue( result.getMessage().contains(expected.getMessage()));

    }

    @Test
    void whenItemDoesNotExistWitGivenBarcodeInSetStatus_thenReturnErrorResult() throws Exception {
        String wrongBarcode = "W1000000";
        int status = 1;
        Result result = deliveryItemService.setStatus(wrongBarcode, status);
        Assertions.assertInstanceOf(ErrorDataResult.class, result);
    }

    @Test
    void whenBagLoadedToBag_thenReturnErrorResult() throws Exception {
        State status = State.LOADED_INTO_BAG;
        int statusValue = State.valueOf(status.toString()).ordinal();
        Result result = deliveryItemService.setStatus(TestDummies.BAG_BARCODE_1, statusValue);

        Assertions.assertInstanceOf(ErrorDataResult.class, result);

    }


    @Test
    void whenSuccessfullyUpdateBagStatusToLoaded_thenReturnSuccessResult() throws Exception {


        int newStatusValue = State.valueOf(UNLOADED_STATUS.toString()).ordinal();
        Result result = deliveryItemService.setStatus(TestDummies.BAG_BARCODE_1, newStatusValue);
        System.out.println(result.getMessage());
        assert result.isSuccess();

        DataResult actual = deliveryItemService.findByBarcode(TestDummies.BAG_BARCODE_1);

        Assertions.assertEquals(((Bag) (actual.getData())).getState(), State.values()[newStatusValue]);

        for (Package pack : ((Bag) (actual.getData())).getPackages()) {
            Assertions.assertEquals(pack.getState(), UNLOADED_STATUS);
        }

    }

    @Test
    void whenSuccessfullyUpdateBagStatusToUnloaded_thenReturnSuccessResult() throws Exception {

        int newStatusValue = State.valueOf(UNLOADED_STATUS.toString()).ordinal();
        Result statusResult = deliveryItemService.setStatus(TestDummies.BAG_BARCODE_1, newStatusValue);

        assert statusResult.isSuccess();

        DataResult<DeliveryItem> findItemResult = deliveryItemService.findByBarcode(TestDummies.BAG_BARCODE_1);

        assert (findItemResult.isSuccess());
        Assertions.assertEquals(findItemResult.getData().getState(), UNLOADED_STATUS);


    }


    @Test
    void whenSuccessfullyUpdatePackageStatus_thenReturnSuccessResult() throws Exception {



        int newStatusValue = State.valueOf(UNLOADED_STATUS.toString()).ordinal();

        Result saveResult = deliveryItemService.savePackage(PACKAGE_IN_BAG_BARCODE_1, DELIVERY_POINT_ID_1, 100);

        Result statusResult = deliveryItemService.setStatus(PACKAGE_IN_BAG_BARCODE_1, newStatusValue);
        DataResult<DeliveryItem> itemDataResult = deliveryItemService.findByBarcode(PACKAGE_IN_BAG_BARCODE_1);

        assert saveResult.isSuccess();
        assert statusResult.isSuccess();
        assert itemDataResult.isSuccess();
        Assertions.assertInstanceOf(Package.class, itemDataResult.getData());
        Assertions.assertEquals(itemDataResult.getData().getState(), UNLOADED_STATUS);

    }

}
