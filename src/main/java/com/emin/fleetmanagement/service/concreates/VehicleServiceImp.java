package com.emin.fleetmanagement.service.concreates;

import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.repository.VehicleRepository;
import com.emin.fleetmanagement.service.abstracts.VehicleService;
import com.emin.fleetmanagement.utils.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.emin.fleetmanagement.MessageConstants.*;

@Service
public class VehicleServiceImp implements VehicleService
{
    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Result save(String plate, String model) {

        Vehicle vehicle = new Vehicle(plate,model);
        vehicleRepository.save(vehicle);

        return new SuccessResult(ITEM_SAVED_SUCCESSFULLY_MESSAGE);
    }

    @Override
    public DataResult<Vehicle> findById(String plate) {

        Optional<Vehicle> vehicle = vehicleRepository.findById(plate);

        if (!vehicle.isPresent()){
            return new ErrorDataResult<Vehicle>(VEHICLE_NOT_FOUND_MESSAGE + plate);
        }

        return new SuccessDataResult<Vehicle>(vehicle.get(),ITEM_FOUND_MESSAGE);
    }
}
