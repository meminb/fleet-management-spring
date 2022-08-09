package com.emin.fleetmanagement.testUtils.fluentBuilders;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.Bag;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.model.delivery.item.Package;
import com.emin.fleetmanagement.testUtils.TestDummies;
import com.emin.fleetmanagement.utils.State;

import java.util.List;
import java.util.Random;

public class DeliveryItemBuilder {


    private String barcode = TestDummies.BAG_BARCODE_1;

    private DeliveryPoint unloadingPoint = TestDummies.DELIVERY_POINT_2;

    private State status = TestDummies.CREATED_STATUS;

    private Delivery delivery = TestDummies.DELIVERY_1;
    private List<Package> packages = TestDummies.PACKAGE_LIST;

    private Bag bag = TestDummies.BAG_1;

    private int weight = TestDummies.WEIGHT;


    public DeliveryItem build() throws Exception {
        if(this.barcode.charAt(0)=='C'){
            return new Bag(this.barcode, this.packages, this.unloadingPoint, this.status, this.delivery);
        } else if (this.barcode.charAt(0)=='P') {
            return new Package(barcode, this.unloadingPoint, this.status, this.delivery, this.weight);
        }
        throw new Exception("Unqualified delivery item barcode.");

    }


    public DeliveryItemBuilder withRandomBagBarcode() {
        Random r = new Random();
        this.barcode = "C"+r.nextInt(990000)+10000;
        return this;
    }

    public DeliveryItemBuilder withRandomPackageBarcode() {
        Random r = new Random();
        this.barcode = "P"+(r.nextInt(90)+10)+(r.nextInt(90)+10)+"000"+(r.nextInt(90)+10);
        return this;
    }
    public DeliveryItemBuilder withBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public DeliveryItemBuilder withUnloadingPoint(DeliveryPoint unloadingPoint) {
        this.unloadingPoint = unloadingPoint;
        return this;
    }

    public DeliveryItemBuilder withStatus(State status) {
        this.status = status;
        return this;
    }

    public DeliveryItemBuilder withDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public DeliveryItemBuilder withPackages(List<Package> packages) {
        this.packages = packages;
        return this;
    }

    public DeliveryItemBuilder withBag(Bag bag) {
        this.bag = bag;
        return this;
    }

    public DeliveryItemBuilder withWeight(int weight) {
        this.weight = weight;
        return this;
    }


}
