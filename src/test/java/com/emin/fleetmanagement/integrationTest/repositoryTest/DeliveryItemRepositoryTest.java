package com.emin.fleetmanagement.integrationTest.repositoryTest;

import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.repository.DeliveryItemRepository;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryItemBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
public class DeliveryItemRepositoryTest {

    @Autowired
    DeliveryItemRepository deliveryItemRepository;

    @Autowired
    DeliveryPointRepository deliveryPointRepository;




    @Test
    void saveBagWithUsingDeliveryItemRepository() throws Exception {

        DeliveryPoint deliveryPoint = deliveryPointRepository.save(TestDummies.DELIVERY_POINT_3);

        Package pack1 = (Package) new DeliveryItemBuilder().withRandomPackageBarcode().withUnloadingPoint(deliveryPoint).build();
        Package pack2 = (Package) new DeliveryItemBuilder().withRandomPackageBarcode().withUnloadingPoint(deliveryPoint).build();
        List<Package> packageList = new ArrayList<>(Arrays.asList(pack1, pack2));
        Bag itemToSave = (Bag) new DeliveryItemBuilder().withRandomBagBarcode().withPackages(packageList).withUnloadingPoint(deliveryPoint).build();

        DeliveryItem savedItem = deliveryItemRepository.save(itemToSave);

        Optional<DeliveryItem> actual = deliveryItemRepository.findById(itemToSave.getBarcode());

        assert actual.isPresent();

        assert ((Bag)(actual.get())).getPackages().size()==2;

        Assertions.assertThat(actual.get()).usingRecursiveComparison()
                .isEqualTo(savedItem);

    }


}
