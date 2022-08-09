package com.emin.fleetmanagement.end2end;


import com.emin.fleetmanagement.MessageConstants;
import com.emin.fleetmanagement.config.DatabaseConfig;
import com.emin.fleetmanagement.controller.DeliveryItemController;
import com.emin.fleetmanagement.controller.DeliveryPointController;
import com.emin.fleetmanagement.controller.ShipmentController;
import com.emin.fleetmanagement.controller.VehicleController;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.testUtils.fluentBuilders.VehicleBuilder;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.emin.fleetmanagement.testUtils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = {DatabaseConfig.Initializer.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ScenarioInExplanation {


    @Autowired
    ShipmentController shipmentController;

    @Autowired
    DeliveryItemController deliveryItemController;

    @Autowired
    DeliveryPointController deliveryPointController;

    @Autowired
    VehicleController vehicleController;

    @Autowired
    MockMvc mockMvc;


    @Order(0)
    @Test
    void whenCreateVehicleEndpointCalled_thenCreateVehicleAndReturnSuccessResult() throws Exception {

        Vehicle vehicle = new VehicleBuilder().withPlate("34 TL 34").withModel("Mercedes").build();

        mockMvc.perform(post("/api/vehicle/create").contentType(MediaType.APPLICATION_JSON)
                        .param(PLATE_KEY, vehicle.getPlate()).param(MODEL_KEY, vehicle.getModel()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true)).
                andExpect(jsonPath(MESSAGE_KEY).value(MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE));


    }


    @Order(1)
    @ParameterizedTest
    @EnumSource(DeliveryPointType.class)
    void whenCreateDeliveryPointEndpointCalled_thenCreateDeliveryPointAndReturnSuccessResult(DeliveryPointType deliveryPointType) throws Exception {
        mockMvc.perform(post("/api/delivery-point/create").contentType(MediaType.APPLICATION_JSON)
                        .param(DELIVERY_POINT_TYPE_KEY, String.valueOf(deliveryPointType.ordinal() + 1)).param(NAME_KEY, "TestName"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true)).
                andExpect(jsonPath(MESSAGE_KEY).value(MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE));

    }

    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {"C725798,1","C725799,2", "C725800,3"})
    void whenCreateBagEndpointCalled_thenCreateBagAndReturnSuccessResult(String bagBarcode, String unloadingPoint) throws Exception {
        mockMvc.perform(post("/api/item/create-bag").contentType(MediaType.APPLICATION_JSON)
                        .param(BARCODE_KEY, bagBarcode).param(UNLOADING_POINT_KEY, unloadingPoint))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true)).
                andExpect(jsonPath(MESSAGE_KEY).value(MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE));
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource(value = {
            "P7988000121,1,5 ", "P7988000122,1,5", "P7988000123,1,9",
            "P8988000120,2,33", "P8988000121,2,17", "P8988000122,2,26",
            "P8988000123,2,35", "P8988000124,2,1", "P8988000125,2,200 ",
            "P8988000126,2,50 ", "P9988000126,3,15 ", "P9988000127,3,16 ",
            "P9988000128,3,55 ", "P9988000129,3,28 ", "P9988000130,3,17"})
    void whenCreatePackageEndpointCalled_thenCreatePackageAndReturnSuccessResult(String packageBarcode, String unloadingPoint, String volumetricWeight) throws Exception {
        mockMvc.perform(post("/api/item/create-package").contentType(MediaType.APPLICATION_JSON)
                        .param(BARCODE_KEY, packageBarcode).param(UNLOADING_POINT_KEY, unloadingPoint)
                        .param(WEIGHT_KEY, volumetricWeight))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true)).
                andExpect(jsonPath(MESSAGE_KEY).value(MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE));

    }

    @Order(4)
    @ParameterizedTest
    @CsvSource(value = {
            "P8988000122,C725799",
            "P8988000126,C725799",
            "P9988000128,C725800",
            "P9988000129,C725800",})
    void givenBagAndPackagesExist_whenAssignPackagesToBagEndpointCalled_thenAssignThemAndReturnSuccessResult(String packageBarcode, String bagBarcode) throws Exception {
        mockMvc.perform(post("/api/item/assign").contentType(MediaType.APPLICATION_JSON)
                        .param("packageBarcode", packageBarcode).param("bagBarcode", bagBarcode))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(true)).
                andExpect(jsonPath(MESSAGE_KEY).value(MessageConstants.ITEM_UPDATED_SUCCESSFULLY_MESSAGE));

    }


    @Order(5)
    @Test
    void givenRelatedEntitiesExist_whenDeliverShipmentsEndpointCalled_thenCreateAndSaveDeliveredShipmentAndReturnSuccessResult() throws Exception {
        String req = "{\n" +
                "  \"plate\": \"34 TL 34\",\n" +
                "  \"route\": [\n" +
                "    {\n" +
                "      \"deliveryPoint\": 1,\n" +
                "      \"deliveries\": [\n" +
                "        {\"barcode\": \"P7988000121\"},\n" +
                "        {\"barcode\": \"P7988000122\"},\n" +
                "        {\"barcode\": \"P7988000123\"},\n" +
                "        {\"barcode\": \"P8988000121\"},\n" +
                "        {\"barcode\": \"C725799\"},\n" +
                "        {\"barcode\": \"C725798\"}\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"deliveryPoint\": 2,\n" +
                "      \"deliveries\": [\n" +
                "        {\"barcode\": \"P8988000123\"},\n" +
                "        {\"barcode\": \"P8988000124\"},\n" +
                "        {\"barcode\": \"P8988000125\"},\n" +
                "        {\"barcode\": \"C725799\"}\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"deliveryPoint\": 3,\n" +
                "      \"deliveries\": [\n" +
                "        {\"barcode\": \"P9988000126\"},\n" +
                "        {\"barcode\": \"P9988000127\"},\n" +
                "        {\"barcode\": \"P9988000128\"},\n" +
                "        {\"barcode\": \"P9988000129\"},\n" +
                "        {\"barcode\": \"P9988000130\"}\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String expected =
                        "{\"success\":true," +
                        "\"message\":\"Item saved successfully." +
                        "\",\"data\":" +
                                "{\n" +
                                "  \"plate\": \"34 TL 34\",\n" +
                                "  \"route\": [\n" +
                                "    {\n" +
                                "      \"deliveryPoint\": 1,\n" +
                                "      \"deliveries\": [\n" +
                                "        {\"barcode\": \"P7988000121\", \"state\":4},\n" +
                                "        {\"barcode\": \"P7988000122\", \"state\":4},\n" +
                                "        {\"barcode\": \"P7988000123\", \"state\":4},\n" +
                                "        {\"barcode\": \"P8988000121\", \"state\":3},\n" +
                                "        {\"barcode\": \"C725799\", \"state\":3},\n" +
                                "        {\"barcode\": \"C725798\", \"state\":3}\n" +
                                "      ]\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"deliveryPoint\": 2,\n" +
                                "      \"deliveries\": [\n" +
                                "        {\"barcode\": \"P8988000123\", \"state\":4},\n" +
                                "        {\"barcode\": \"P8988000124\", \"state\":4},\n" +
                                "        {\"barcode\": \"P8988000125\", \"state\":4},\n" +
                                "        {\"barcode\": \"C725799\", \"state\":4}\n" +
                                "      ]\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"deliveryPoint\": 3,\n" +
                                "      \"deliveries\": [\n" +
                                "        {\"barcode\": \"P9988000126\", \"state\":3},\n" +
                                "        {\"barcode\": \"P9988000127\", \"state\":3},\n" +
                                "        {\"barcode\": \"P9988000128\", \"state\":4},\n" +
                                "        {\"barcode\": \"P9988000129\", \"state\":4},\n" +
                                "        {\"barcode\": \"P9988000130\", \"state\":3}\n" +
                                "      ]\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"+
                                "}";

        MvcResult result = mockMvc.perform(post("/api/shipment/deliver").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals(expected.replaceAll("\n","").replaceAll(" ",""), content.replaceAll("\n","").replaceAll(" ",""));

    }


}
