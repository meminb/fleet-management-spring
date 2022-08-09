package com.emin.fleetmanagement.testUtils;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.utils.DeliveryPointType;
import com.emin.fleetmanagement.utils.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDummies {


    public static final State CREATED_STATUS= State.CREATED;
   public static final State LOADED_STATUS= State.LOADED;
   public static final State UNLOADED_STATUS= State.UNLOADED;
   public static final State LOADED_TO_BAG_STATUS= State.LOADED_INTO_BAG;

   public static final String BAG_BARCODE_1 = "C1001T";
   public static final String PACKAGE_IN_BAG_BARCODE_1 = "P1001T";
   public static final String PACKAGE_IN_BAG_BARCODE_2 = "P1002T";
   public static final String PACKAGE_BARCODE_3 = "P1003T";
   public static final Long DELIVERY_POINT_ID_1=1l;
   public static final Long DELIVERY_POINT_ID_2=2l;
   public static final Long DELIVERY_POINT_ID_3=3l;
   public static final String DELIVERY_POINT_NAME ="ızmir";
   public static final int WEIGHT =100;
   public static final String MODEL_1 = "Mercedes";



   public static final String PLATE_1 = "16 TD 16";




   public static final DeliveryPoint DELIVERY_POINT_1 = new DeliveryPoint(DELIVERY_POINT_ID_1,DELIVERY_POINT_NAME, DeliveryPointType.BRANCH);
   public static final DeliveryPoint DELIVERY_POINT_2 = new DeliveryPoint(DELIVERY_POINT_ID_2,DELIVERY_POINT_NAME, DeliveryPointType.DISTRIBUTION_CENTER);
   public static final DeliveryPoint DELIVERY_POINT_3 = new DeliveryPoint(DELIVERY_POINT_ID_3,DELIVERY_POINT_NAME, DeliveryPointType.TRANSFER_CENTER);
   public static final Package PACKAGE_IN_BAG_1 = new Package(PACKAGE_IN_BAG_BARCODE_1, DELIVERY_POINT_1, CREATED_STATUS, WEIGHT);
   public static final Package PACKAGE_IN_BAG_2 = new Package(PACKAGE_IN_BAG_BARCODE_2, DELIVERY_POINT_2, LOADED_STATUS, WEIGHT);
   public static final Package PACKAGE_3 = new Package(PACKAGE_BARCODE_3, DELIVERY_POINT_3, UNLOADED_STATUS, WEIGHT);
   public static final List<Package> PACKAGE_LIST = new ArrayList<>(Arrays.asList(PACKAGE_IN_BAG_1, PACKAGE_IN_BAG_2));

   public static final Bag BAG_1 = new Bag(BAG_BARCODE_1,PACKAGE_LIST,DELIVERY_POINT_3,CREATED_STATUS);

   public static final Delivery DELIVERY_1 = new Delivery(DELIVERY_POINT_2,new ArrayList<>(Arrays.asList(BAG_1,PACKAGE_3)));


}
