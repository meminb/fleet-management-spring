package com.emin.fleetmanagement.service.abstracts;

import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;

public interface VehicleService {

    Result save(String plate, String model);

    DataResult<Vehicle> findById(String plate);


}
