package com.emin.fleetmanagement.service.concreates;

import com.emin.fleetmanagement.MessageConstants;
import com.emin.fleetmanagement.model.delivery.Shipment;
import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.model.dto.DeliveryDTO;
import com.emin.fleetmanagement.model.dto.DeliveryItemDTO;
import com.emin.fleetmanagement.model.dto.ShipmentDTO;
import com.emin.fleetmanagement.repository.ShipmentRepository;
import com.emin.fleetmanagement.repository.VehicleRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryService;
import com.emin.fleetmanagement.service.abstracts.ShipmentService;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.ErrorDataResult;
import com.emin.fleetmanagement.utils.result.Result;
import com.emin.fleetmanagement.utils.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class ShipmentServiceImp implements ShipmentService {


    @Autowired
    DeliveryService deliveryService;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public DataResult unloadAndSave(ShipmentDTO shipmentDTO) throws Exception {

        Optional<Vehicle> vehicle = vehicleRepository.findById(shipmentDTO.getPlate());
        if (!vehicle.isPresent()){
            return new ErrorDataResult(MessageConstants.VEHICLE_NOT_FOUND_MESSAGE+" "+shipmentDTO.getPlate());
        }

        Shipment savedShipment = shipmentRepository.save(new Shipment(1l, Date.valueOf(LocalDate.now()),vehicle.get(),new ArrayList<>()));

        List<DeliveryDTO> dtoRoute = new ArrayList<>();
       for (DeliveryDTO deliveryDTO : shipmentDTO.getRoute()) {
           List<String> barcodes = convertBarcodeListFromDeliveryItemDtoList(deliveryDTO.getDeliveries());
            DataResult unloadedDeliveryResult = deliveryService.unloadAndSaveDelivery(deliveryDTO.getDeliveryPoint(), barcodes ,savedShipment.getId());
            if (!unloadedDeliveryResult.isSuccess()){
                return unloadedDeliveryResult;
            }
            DeliveryDTO processedDeliveryDTO= new DeliveryDTO((Delivery) unloadedDeliveryResult.getData()) ;
            dtoRoute.add(processedDeliveryDTO);
        }

        shipmentDTO.setRoute(dtoRoute);

        return new SuccessDataResult(shipmentDTO,MessageConstants.ITEM_SAVED_SUCCESSFULLY_MESSAGE);
    }

    @Override
    public Result saveWithDeliveries(String vehiclePlate, List<Map<String, String>> deliveries) {
        return null;
    }

    @Override
    public DataResult<Delivery> findById(Long id) {
        return null;
    }


    private List<String> convertBarcodeListFromDeliveryItemDtoList(List<DeliveryItemDTO> deliveries) {
        List<String> barcodes = new ArrayList<>();

        for (DeliveryItemDTO deliveryItemDto : deliveries) {
            barcodes.add(deliveryItemDto.getBarcode());
        }
        return barcodes;

    }

}
