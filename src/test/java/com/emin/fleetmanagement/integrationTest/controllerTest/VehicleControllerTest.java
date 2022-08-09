package com.emin.fleetmanagement.integrationTest.controllerTest;


import com.emin.fleetmanagement.controller.VehicleController;
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

import static com.emin.fleetmanagement.testUtils.TestConstants.MODEL_KEY;
import static com.emin.fleetmanagement.testUtils.TestConstants.PLATE_KEY;
import static com.emin.fleetmanagement.testUtils.TestDummies.MODEL_1;
import static com.emin.fleetmanagement.testUtils.TestDummies.PLATE_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
public class VehicleControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    VehicleController vehicleController;



    @Test@Order(0)
    public void whenGetNonExistingVehicle_thenReturnErrorResponse() throws Exception {

        mockMvc.perform(get("/api/vehicle/"+PLATE_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));

    }


    @Test
    @Order(1)
    public void whenSaveVehicleProperly_thenReturnSuccessResult() throws Exception {

        mockMvc.perform(post("/api/vehicle/create").contentType(MediaType.APPLICATION_JSON)
                        .param(PLATE_KEY,PLATE_1).param(MODEL_KEY,MODEL_1))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true));
        ;
    }

    @Test@Order(2)
    public void whenGetExistingVehicle_thenReturnSuccessResponse() throws Exception {


        mockMvc.perform(get("/api/vehicle/"+PLATE_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true));
        ;
    }



}
