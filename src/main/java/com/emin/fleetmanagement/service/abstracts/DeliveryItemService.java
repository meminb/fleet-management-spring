package com.emin.fleetmanagement.service.abstracts;

import com.emin.fleetmanagement.utils.result.DataResult;
import com.emin.fleetmanagement.utils.result.Result;

public interface DeliveryItemService extends Statusable{

    Result savePackage(String barcode, Long unloadingPointId, int weight);

    Result saveBag(String barcode, Long unloadingPointId);

    DataResult findByBarcode(String barcode);

    Result assignPackageToBag(String packageBarcode, String bagBarcode);


}
