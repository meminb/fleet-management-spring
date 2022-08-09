package com.emin.fleetmanagement.service.concreates;

import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.repository.DeliveryPointRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryPointService;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import com.emin.fleetmanagement.utils.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.emin.fleetmanagement.MessageConstants.*;

@Service
public class DeliveryPointServiceImp implements DeliveryPointService {


    @Autowired
    DeliveryPointRepository deliveryPointRepository;


    @Override
    public Result save(String name, int deliveryPointType) {

        //If given value of point type doesn't exist in point type enum
        if (deliveryPointType > DeliveryPointType.values().length) {
            return new ErrorResult(INCORRECT_DELIVERY_POINT_TYPE_MESSAGE);
        }

        Result result;

        DeliveryPoint deliveryPoint = new DeliveryPoint(name, DeliveryPointType.values()[deliveryPointType - 1]);
        deliveryPointRepository.save(deliveryPoint);
        result = new SuccessResult(ITEM_SAVED_SUCCESSFULLY_MESSAGE);


        return result;
    }

    @Override
    public DataResult<DeliveryPoint> findById(Long id) {
        Optional<DeliveryPoint> deliveryPoint = deliveryPointRepository.findById(id);
        if (!deliveryPoint.isPresent()) {
            return new ErrorDataResult(DELIVERY_POINT_NOT_FOUND_MESSAGE+" "+id);
        }
        return new SuccessDataResult<>(deliveryPoint.get(), ITEM_FOUND_MESSAGE);

    }
}
