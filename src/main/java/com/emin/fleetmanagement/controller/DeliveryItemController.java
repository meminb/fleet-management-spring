package com.emin.fleetmanagement.controller;


import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.service.abstracts.DeliveryItemService;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item")
public class DeliveryItemController {


    @Autowired
    DeliveryItemService deliveryItemService;


    @GetMapping("/{barcode}")
    @ResponseBody
    DataResult<DeliveryItem> findById(@PathVariable String barcode)  {
        return deliveryItemService.findByBarcode(barcode);
    }

    @PostMapping("create-package")
    Result createPackage(@RequestParam String barcode, @RequestParam Long unloadingPointId,@RequestParam int weight) {
        return deliveryItemService.savePackage(barcode,unloadingPointId,weight);


    }
    @PostMapping("create-bag")
    Result createBag(@RequestParam String barcode, @RequestParam Long unloadingPointId ) {

        return deliveryItemService.saveBag(barcode,unloadingPointId);


    }
    @PostMapping("assign")
    Result createBag( @RequestParam String packageBarcode,@RequestParam String bagBarcode ) {

        return deliveryItemService.assignPackageToBag(packageBarcode,bagBarcode);


    }



}
