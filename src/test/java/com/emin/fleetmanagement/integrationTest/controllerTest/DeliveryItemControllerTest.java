package com.emin.fleetmanagement.integrationTest.controllerTest;


import com.emin.fleetmanagement.controller.DeliveryItemController;
import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.repository.DeliveryItemRepository;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.emin.fleetmanagement.testUtils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
public class DeliveryItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DeliveryItemController deliveryItemController;


    @Autowired
    DeliveryPointRepository deliveryPointRepository;

    @Autowired
    DeliveryItemRepository deliveryItemRepository;







    @Test
    public void whenSaveBagWithNonExistingDeliveryPoint_thenReturnErrorResponse() throws Exception {

        mockMvc.perform(post("/api/item/create-bag").contentType(MediaType.APPLICATION_JSON).param(BARCODE_KEY, TestDummies.BAG_BARCODE_1)
                        .param(UNLOADING_POINT_KEY, "99999"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
    }


    @Test
    public void whenSaveBagProperly_thenReturnSuccessResponse() throws Exception {

        DeliveryPoint deliveryPoint = new DeliveryPoint(TestDummies.DELIVERY_POINT_ID_1, "Izmir",     DeliveryPointType.BRANCH);
        deliveryPointRepository.save(deliveryPoint);

        mockMvc.perform(post("/api/item/create-bag").contentType(MediaType.APPLICATION_JSON).param(BARCODE_KEY, TestDummies.BAG_BARCODE_1)
                        .param(UNLOADING_POINT_KEY, TestDummies.DELIVERY_POINT_ID_1.toString()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("success").value(true));

    }

    @Test
    public void whenGetBagProperly_thenReturnSuccessResponse() throws Exception {

        DeliveryPoint deliveryPoint = new DeliveryPoint(TestDummies.DELIVERY_POINT_ID_1, "Izmir",     DeliveryPointType.BRANCH);
        deliveryPointRepository.save(deliveryPoint);

        Bag bag = new Bag(TestDummies.BAG_BARCODE_1, deliveryPoint);
        deliveryItemRepository.save(bag);

        mockMvc.perform(get("/api/item/" + TestDummies.BAG_BARCODE_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("success").value(true));

    }

    @Test
    public void whenGetNonExistingBag_thenReturnErrorResponse() throws Exception {

        mockMvc.perform(get("/api/item/" + "nonExistingBarcode").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("success").value(false));

    }


    @Test
    public void whenSavePackageWithNonExistingDeliveryPoint_thenReturnErrorResponse() throws Exception {

        mockMvc.perform(post("/api/item/create-package").contentType(MediaType.APPLICATION_JSON).param(BARCODE_KEY,TestDummies.PACKAGE_BARCODE_3)
                        .param(UNLOADING_POINT_KEY, "999999").param(WEIGHT_KEY, String.valueOf(TestDummies.WEIGHT)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
    }


    @Test
    public void whenSavePackageProperly_thenReturnSuccessResponse() throws Exception {


        DeliveryPoint deliveryPoint = new DeliveryPoint(1l, "Izmir", DeliveryPointType.BRANCH);
        deliveryPointRepository.save(deliveryPoint);

        mockMvc.perform(post("/api/item/create-package").contentType(MediaType.APPLICATION_JSON)
                        .param(BARCODE_KEY, TestDummies.PACKAGE_BARCODE_3)
                        .param(UNLOADING_POINT_KEY, TestDummies.DELIVERY_POINT_ID_1.toString())
                        .param(WEIGHT_KEY, String.valueOf(TestDummies.WEIGHT)))

                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("success").value(true));

    }

    @Test
    public void whenGetPackageProperly_thenReturnSuccessResponse() throws Exception {

        mockMvc.perform(get("/api/item/" + TestDummies.PACKAGE_BARCODE_3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("success").value(true));

    }

    @Test
    @Order(4)
    public void whenGetNonExistingPackage_thenReturnErrorResponse() throws Exception {

        mockMvc.perform(get("/api/item/" + "nonExistingBarcode").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("success").value(false));

    }


}
