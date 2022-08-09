package com.emin.fleetmanagement.service.concreates;

import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.repository.DeliveryItemRepository;
import com.emin.fleetmanagement.service.abstracts.DeliveryItemService;
import com.emin.fleetmanagement.service.abstracts.DeliveryPointService;
import com.emin.fleetmanagement.utils.State;
import com.emin.fleetmanagement.utils.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.emin.fleetmanagement.MessageConstants.*;

@Service
public class DeliveryItemServiceImp implements DeliveryItemService {


    @Autowired
    DeliveryItemRepository deliveryItemRepository;

    @Autowired
    DeliveryPointService deliveryPointService;


    @Override
    public Result savePackage(String barcode, Long unloadingPointId, int weight) {

        DataResult<DeliveryPoint> unloadingPoint = deliveryPointService.findById(unloadingPointId);

        if(!unloadingPoint.isSuccess()){
            return new ErrorResult(DELIVERY_POINT_NOT_FOUND_MESSAGE+" "+ unloadingPointId);
        }

        Package pack = new Package(barcode,unloadingPoint.getData(),weight);
        deliveryItemRepository.save(pack);
        return new SuccessResult(ITEM_SAVED_SUCCESSFULLY_MESSAGE);

    }

    @Override
    public Result saveBag(String barcode, Long unloadingPointId) {

        DataResult<DeliveryPoint> unloadingPoint = deliveryPointService.findById(unloadingPointId);

        if(!unloadingPoint.isSuccess()){
            return new ErrorResult(DELIVERY_POINT_NOT_FOUND_MESSAGE+" "+ unloadingPointId);
        }
        Bag bag = new Bag(barcode,unloadingPoint.getData());
         deliveryItemRepository.save(bag);
        return new SuccessResult(ITEM_SAVED_SUCCESSFULLY_MESSAGE);

    }

    @Override
    public DataResult findByBarcode(String barcode) {
        Optional<DeliveryItem> deliveryItem = deliveryItemRepository.findById(barcode);
        if(deliveryItem.isPresent()){
            return new SuccessDataResult(deliveryItem.get(),ITEM_FOUND_MESSAGE);
        }
        return new ErrorDataResult(DELIVERY_ITEM_NOT_FOUND_MESSAGE+" "+ barcode);
    }

    @Override
    public Result assignPackageToBag(String packageBarcode, String bagBarcode) {
        Optional<DeliveryItem> pack = deliveryItemRepository.findById(packageBarcode);
        Optional<DeliveryItem> bag =deliveryItemRepository.findById(bagBarcode);


        if(!pack.isPresent() || !bag.isPresent()){
            return  new ErrorResult(DELIVERY_ITEM_NOT_FOUND_MESSAGE+ " "+packageBarcode+ "or" + bagBarcode);
        }else if (!(pack.get() instanceof Package) || !(bag.get() instanceof Bag)) {
            return  new ErrorResult(MISMATCHED_BAG_PACKAGE_BARCODE_MESSAGE);
        }


        ((Bag)bag.get()).insertPackage((Package)(pack.get()));
        ((Package)pack.get()).setBag((Bag)bag.get());

        deliveryItemRepository.save(bag.get());
        deliveryItemRepository.save(pack.get());

        return  new SuccessResult(ITEM_UPDATED_SUCCESSFULLY_MESSAGE);

    }

    @Override
    public DataResult<DeliveryItem> setStatus(String itemBarcode, int statusValue) throws Exception {

        State newStatus = State.values()[statusValue];
        Optional<DeliveryItem> item = deliveryItemRepository.findById(itemBarcode);

        DeliveryItem updatedItem = null;

        if (!item.isPresent()) {
            return new ErrorDataResult(DELIVERY_ITEM_NOT_FOUND_MESSAGE+" "+itemBarcode);
        } else if (item.get() instanceof Bag) {

            if (newStatus == State.LOADED_INTO_BAG) {
                return new ErrorDataResult(BAGS_CANNOT_BE_LOADED_TO_BAGS_MESSAGE+" "+itemBarcode);
            }

            updatedItem = setBagStatus(newStatus, (Bag) item.get());
            deliveryItemRepository.save(updatedItem);

        } else if (item.get() instanceof Package) {

            updatedItem = setPackageStatus(newStatus, (Package) item.get());
            deliveryItemRepository.save(updatedItem);


        }
        return new SuccessDataResult(updatedItem,ITEM_UPDATED_SUCCESSFULLY_MESSAGE);
    }


    private Bag setBagStatus(State status, Bag bag) {

        if (status == State.UNLOADED) {

            for (Package pack : bag.getPackages()) {
                pack.setState(status);
            }
        }
        bag.setState(status);
        return bag;
    }


    private Package setPackageStatus(State status, Package pack) {
        pack.setState(status);
        return pack;
    }
}
