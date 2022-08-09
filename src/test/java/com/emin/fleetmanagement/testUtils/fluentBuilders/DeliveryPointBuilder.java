package com.emin.fleetmanagement.testUtils.fluentBuilders;

import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.utils.DeliveryPointType;

import javax.persistence.*;
import java.util.Random;

public class DeliveryPointBuilder {


    private Long id=(long) new Random().nextInt(100);

    private String name = "Izmir";

    private DeliveryPointType deliveryPointType= DeliveryPointType.TRANSFER_CENTER;

    public DeliveryPoint build(){

        return new DeliveryPoint(this.id,this.name,this.deliveryPointType);
    }

    public DeliveryPointBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DeliveryPointBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DeliveryPointBuilder withDeliveryPointType(DeliveryPointType deliveryPointType) {
        this.deliveryPointType = deliveryPointType;
        return this;
    }




}
