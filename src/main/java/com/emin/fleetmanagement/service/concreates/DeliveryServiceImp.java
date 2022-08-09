package com.emin.fleetmanagement.service.concreates;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.repository.DeliveryRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryItemService;
import com.emin.fleetmanagement.service.abstracts.DeliveryPointService;
import com.emin.fleetmanagement.service.abstracts.DeliveryService;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import com.emin.fleetmanagement.utils.State;
import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.ErrorDataResult;
import com.emin.fleetmanagement.utils.result.Result;
import com.emin.fleetmanagement.utils.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.emin.fleetmanagement.MessageConstants.*;

@Service
public class DeliveryServiceImp implements DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    DeliveryItemService deliveryItemService;

    @Autowired
    DeliveryPointService deliveryPointService;

    Logger logger = Logger.getLogger(this.getClass().getName());


    @Override
    public Result save(Long deliveryPointId) {
        return null;
    }

    @Override
    public DataResult<Delivery> unloadAndSaveDelivery(Long deliveryPointId, List<String> itemBarcodes, Long shipmentId) throws Exception {

        DataResult<DeliveryPoint> deliveryPointResult = deliveryPointService.findById(deliveryPointId);
        if (!deliveryPointResult.isSuccess()) {
            return new ErrorDataResult(deliveryPointResult.getMessage());
        }
        Delivery delivery = new Delivery(deliveryPointResult.getData(), new ArrayList<>());

        Delivery loadedDeliver = loadItems(delivery, itemBarcodes);
        Delivery unloadedDelivery = unloadItems(loadedDeliver);

        deliveryRepository.save(unloadedDelivery);

        return new SuccessDataResult<Delivery>(unloadedDelivery, ITEM_SAVED_SUCCESSFULLY_MESSAGE);
    }

    @Override
    public DataResult<Delivery> findById(Long id) {
        return null;
    }


    private Delivery loadItems(Delivery delivery, List<String> itemBarcodes) throws Exception {

        List<DeliveryItem> deliveryItemList = new ArrayList<>();
        for (String barcode : itemBarcodes) {
            DataResult<?> updatedItemResult = deliveryItemService.setStatus(barcode, State.LOADED.ordinal());
            if (!updatedItemResult.isSuccess()) {
                logger.log(Level.WARNING, updatedItemResult.getMessage());
                continue;
            }
            deliveryItemList.add((DeliveryItem) updatedItemResult.getData());
        }
        delivery.setDeliveryItems(deliveryItemList);

        return delivery;
    }

    private Delivery unloadItems(Delivery delivery) throws Exception {
        for (int i = 0; i < delivery.getDeliveryItems().size(); i++) {
            DeliveryItem item = delivery.getDeliveryItems().get(i);
            if (!Objects.equals(item.getUnloadingPoint().getId(), delivery.getDeliveryPoint().getId())) { // TODO delivery point type or delivery  point id ?

                logger.log(Level.INFO, INCORRECT_DELIVERY_POINT+
                        " Item: "+item.getBarcode()+", Current delivery point Id: " +delivery.getDeliveryPoint().getId()+
                        ", Delivery point to unload: " + item.getUnloadingPoint().getId());
                continue;
            }else if(!checkItemQualifiedToUnloadAtThePoint(item)){
                continue;
            }
            DataResult<?> result = deliveryItemService.setStatus(item.getBarcode(), State.UNLOADED.ordinal());

            delivery.getDeliveryItems().set(i, (DeliveryItem) result.getData());
        }
        return delivery;
    }

    private boolean checkItemQualifiedToUnloadAtThePoint(DeliveryItem item) {

        if (item.getUnloadingPoint().getDeliveryPointType() == DeliveryPointType.BRANCH) {//Only package-type shipments can be unloaded
            if (item instanceof Package && ((Package) item).getBag() == null) {
                return true;
            }
            logger.log(Level.INFO, BRANCH_MESSAGE+" Item: "+item.getBarcode()+
                    ", Delivery point Id: "+item.getUnloadingPoint().getId());
            return false;

        } else if (item.getUnloadingPoint().getDeliveryPointType() == DeliveryPointType.TRANSFER_CENTER) {//Bags and packages in bags may be unloaded
            if ((item instanceof Package && ((Package) item).getBag() != null) || item instanceof Bag) {
                return true;
            }
            logger.log(Level.INFO, TRANSFER_CENTER_MESSAGE+" "+item.getBarcode());
            return false;
        } else if (item.getUnloadingPoint().getDeliveryPointType() == DeliveryPointType.DISTRIBUTION_CENTER) {//Everything can be unloaded
            return true;
        }

        logger.log(Level.SEVERE, INCORRECT_DELIVERY_POINT_TYPE_MESSAGE);
        return false;
    }


}
