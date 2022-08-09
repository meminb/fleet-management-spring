package com.emin.fleetmanagement.service.abstracts;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;

import java.util.List;

public interface DeliveryService {

    Result save(Long deliveryPointId);
    DataResult unloadAndSaveDelivery(Long deliveryPointId, List<String> itemBarcodes, Long shipmentId) throws Exception;

    DataResult<Delivery> findById(Long id);


}
