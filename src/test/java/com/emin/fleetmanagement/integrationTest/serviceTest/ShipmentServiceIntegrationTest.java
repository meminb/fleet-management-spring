package com.emin.fleetmanagement.integrationTest.serviceTest;

import com.emin.fleetmanagement.MessageConstants;
import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.dto.DeliveryDTO;
import com.emin.fleetmanagement.model.dto.DeliveryItemDTO;
import com.emin.fleetmanagement.model.dto.DeliveryItemWithStateDTO;
import com.emin.fleetmanagement.model.dto.ShipmentDTO;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.repository.VehicleRepository;
import com.emin.fleetmanagement.repository.DeliveryItemRepository;
import com.emin.fleetmanagement.service.abstracts.ShipmentService;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryItemBuilder;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryPointBuilder;
import com.emin.fleetmanagement.testUtils.fluentBuilders.VehicleBuilder;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import com.emin.fleetmanagement.utils.State;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.SuccessDataResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShipmentServiceIntegrationTest {


    @Autowired
    ShipmentService shipmentService;

    @Autowired
    DeliveryPointRepository deliveryPointRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    DeliveryItemRepository deliveryItemRepository;


    @Autowired
    ObjectMapper objectMapper;

    Vehicle savedVehicle;
    @BeforeEach
    void saveItems() {
         savedVehicle = vehicleRepository.save(new VehicleBuilder().withPlate("16 TD 16").build());
    }
    @Test
    void givenRelatedEntitiesExist_whenDeliverMethodCalledWithPackageInBranch_thenReturnItemInUnloadedState() throws Exception {

        DeliveryPoint savedDeliveryPoint = deliveryPointRepository.save(new DeliveryPointBuilder().withDeliveryPointType(DeliveryPointType.BRANCH).build());
        DeliveryItem savedItem = deliveryItemRepository.save(new DeliveryItemBuilder().withUnloadingPoint(savedDeliveryPoint).withRandomPackageBarcode().build());

        ShipmentDTO shipmentDTO = new ShipmentDTO(savedVehicle.getPlate(),
                new ArrayList<>(Arrays.asList(new DeliveryDTO(savedDeliveryPoint.getId(),
                        new ArrayList<>(Arrays.asList(new DeliveryItemDTO(savedItem.getBarcode())))))));

        DataResult result = shipmentService.unloadAndSave(shipmentDTO);

        shipmentDTO.getRoute().get(0).setDeliveries(Arrays.asList(new DeliveryItemWithStateDTO(savedItem.getBarcode(), State.UNLOADED.ordinal())));
        DataResult expectedDataResult = new SuccessDataResult(shipmentDTO,
                MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE);

        Assertions.assertEquals(
                objectMapper.writeValueAsString(expectedDataResult),
                objectMapper.writeValueAsString(result));


    }
    @Test
    void givenRelatedEntitiesExist_whenDeliverMethodCalledWithBagInDistributionCenter_thenReturnItemInLoadedState() throws Exception {


        DeliveryPoint savedDeliveryPoint = deliveryPointRepository.save(new DeliveryPointBuilder().
                withDeliveryPointType(DeliveryPointType.DISTRIBUTION_CENTER).build());
        DeliveryItem savedItem = deliveryItemRepository.save(new DeliveryItemBuilder().
                withUnloadingPoint(savedDeliveryPoint).withRandomBagBarcode().withPackages(Collections.emptyList()).build());

        ShipmentDTO shipmentDTO = new ShipmentDTO(savedVehicle.getPlate(),
                new ArrayList<>(Arrays.asList(new DeliveryDTO(savedDeliveryPoint.getId(),
                        new ArrayList<>(Arrays.asList(new DeliveryItemDTO(savedItem.getBarcode())))))));

        DataResult result = shipmentService.unloadAndSave(shipmentDTO);

        shipmentDTO.getRoute().get(0).setDeliveries(Arrays.asList(new DeliveryItemWithStateDTO(savedItem.getBarcode(), State.LOADED.ordinal())));
        DataResult expectedDataResult = new SuccessDataResult(shipmentDTO,
                MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE);

        Assertions.assertEquals(
                objectMapper.writeValueAsString(expectedDataResult),
                objectMapper.writeValueAsString(result));


    }





}
