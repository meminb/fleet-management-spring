package com.emin.fleetmanagement.integrationTest.controllerTest;


import com.emin.fleetmanagement.controller.DeliveryPointController;
import com.emin.fleetmanagement.config.DatabaseConfig;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.emin.fleetmanagement.testUtils.TestConstants.DELIVERY_POINT_TYPE_KEY;
import static com.emin.fleetmanagement.testUtils.TestConstants.NAME_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
public class DeliveryPointControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DeliveryPointController deliveryPointController;



    @Test@Order(0)
    public void whenGetNonExistingPoint_thenReturnErrorResponse() throws Exception {

        mockMvc.perform(get("/api/delivery-point/"+999999).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
        ;
    }
    @Test
    @Order(1)
    public void whenSavePointProperly_thenReturnSuccessResult() throws Exception {

        String nameValue = "Izmir";
        String typeValue = "1";

        mockMvc.perform(post("/api/delivery-point/create").contentType(MediaType.APPLICATION_JSON)
                        .param(NAME_KEY,nameValue).param(DELIVERY_POINT_TYPE_KEY,typeValue))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true));
    }

}
