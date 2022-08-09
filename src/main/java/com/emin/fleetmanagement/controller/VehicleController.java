package com.emin.fleetmanagement.controller;

import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.service.abstracts.VehicleService;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {


    @Autowired
    VehicleService vehicleService;

    @PostMapping("create")
    Result createVehicle(@RequestParam String plate, @RequestParam(required = false) String model) {
        return vehicleService.save(plate, model);
    }

    @GetMapping("/{plate}")
    DataResult<Vehicle> findById(@PathVariable String plate)  {
        return vehicleService.findById(plate);
    }








}
