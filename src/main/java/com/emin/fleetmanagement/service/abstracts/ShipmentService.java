package com.emin.fleetmanagement.service.abstracts;


import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.dto.ShipmentDTO;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;

import java.util.List;
import java.util.Map;

public interface ShipmentService {


    DataResult unloadAndSave(ShipmentDTO shipmentDTO) throws Exception;

    Result saveWithDeliveries(String vehiclePlate, List<Map<String, String>> deliveries);

    DataResult<Delivery> findById(Long id);





}
