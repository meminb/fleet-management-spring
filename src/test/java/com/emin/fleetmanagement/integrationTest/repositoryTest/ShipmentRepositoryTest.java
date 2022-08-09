package com.emin.fleetmanagement.integrationTest.repositoryTest;

import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.Shipment;
import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.repository.ShipmentRepository;
import com.emin.fleetmanagement.repository.VehicleRepository;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryItemBuilder;
import com.emin.fleetmanagement.testUtils.fluentBuilders.VehicleBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

public class ShipmentRepositoryTest extends DatabaseConfig {


    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    VehicleRepository vehicleRepository;


    @Test
    void whenShipmentCreated_thenItsAccessibleFromRepository() throws Exception {

        DeliveryPoint deliveryPoint = TestDummies.DELIVERY_POINT_1;

        Package pack1 = (Package) new DeliveryItemBuilder().withRandomPackageBarcode().withUnloadingPoint(deliveryPoint).build();
        Package pack2 = (Package) new DeliveryItemBuilder().withRandomPackageBarcode().withUnloadingPoint(deliveryPoint).build();
        List<Package> packageList = new ArrayList<>(Arrays.asList(pack1, pack2));
        Bag bag = (Bag) new DeliveryItemBuilder().withRandomBagBarcode().withPackages(packageList).build();

        List<DeliveryItem> deliveryItemList = new ArrayList<>(Arrays.asList(pack2, bag));

        Vehicle vehicle = new VehicleBuilder().build();
        vehicleRepository.save(vehicle);

        Shipment shipment = new Shipment(1L, Date.valueOf(LocalDate.now()), vehicle, Collections.emptyList());


        Delivery delivery1 = new Delivery(1l, deliveryPoint, deliveryItemList, shipment);
        Delivery delivery2 = new Delivery(2l, deliveryPoint, Collections.emptyList(), shipment);

        Shipment createdShipment = shipmentRepository.save(shipment);
        Optional<Shipment> actual = shipmentRepository.findById(createdShipment.getId());

        assert actual.isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(shipment);


    }


}
