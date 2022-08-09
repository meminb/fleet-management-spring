package com.emin.fleetmanagement.integrationTest.repositoryTest;

import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.testUtils.fluentBuilders.DeliveryPointBuilder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})

public class DeliveryPointRepositoryTest  {


    @Autowired
    DeliveryPointRepository deliveryPointRepository;




    @Test
    public void saveDeliveryPoint(){
        DeliveryPoint expected = new DeliveryPointBuilder().build();
        DeliveryPoint saved =  deliveryPointRepository.save(expected);

        Optional<DeliveryPoint> actual = deliveryPointRepository.findById(saved.getId());

        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(saved);

    }

}
