package com.emin.fleetmanagement.controller;

import com.emin.fleetmanagement.model.dto.ShipmentDTO;
import com.emin.fleetmanagement.service.abstracts.ShipmentService;
import com.emin.fleetmanagement.utils.result.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    @Autowired
    ShipmentService shipmentService;

    @PostMapping("deliver")
    DataResult deliverShipments(@RequestBody() ShipmentDTO shipmentDTO) throws Exception {

        return shipmentService.unloadAndSave(shipmentDTO);
    }





}
