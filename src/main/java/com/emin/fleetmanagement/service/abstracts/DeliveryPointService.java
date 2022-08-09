package com.emin.fleetmanagement.service.abstracts;

import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;

public interface DeliveryPointService {

    Result save(String name, int deliveryPointType);

    DataResult<DeliveryPoint> findById(Long id);

}
