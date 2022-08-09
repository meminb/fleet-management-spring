package com.emin.fleetmanagement.controller;


import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.service.abstracts.DeliveryPointService;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery-point")
public class DeliveryPointController {


    @Autowired
    DeliveryPointService deliveryPointService;


    @PostMapping("create")
    Result createPoint(@RequestParam String name, @RequestParam(required = true) int deliveryPointType) {
        return deliveryPointService.save(name, deliveryPointType);
    }

    @GetMapping("/{id}")
    @ResponseBody
    DataResult<DeliveryPoint> findById(@PathVariable Long id)  {
        return deliveryPointService.findById(id);
    }

}
