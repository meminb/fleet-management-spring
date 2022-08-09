package com.emin.fleetmanagement.integrationTest.repositoryTest;

import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.repository.DeliveryRepository;
import com.emin.fleetmanagement.repository.DeliveryItemRepository;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

public class DeliveryRepositoryTest  {


    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryItemRepository deliveryItemRepository;


    @Test
    void whenDeliveryCreated_thenItsAccessibleFromRepository() throws Exception {

        List<DeliveryItem> deliveryItemList = new ArrayList<>(Arrays.asList());

        Delivery expectedDelivery = new DeliveryBuilder().withDeliveryItems(deliveryItemList)
                .withDeliveryPoint(TestDummies.DELIVERY_POINT_2).build();
        Delivery delivery = deliveryRepository.save(expectedDelivery);


        Optional<Delivery> actualDelivery = deliveryRepository.findById(delivery.getId());

        assert(actualDelivery.isPresent());
        assertThat(actualDelivery.get()).usingRecursiveComparison().isEqualTo(delivery);

    }





}
