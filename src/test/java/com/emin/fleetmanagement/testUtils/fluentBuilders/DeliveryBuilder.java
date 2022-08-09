package com.emin.fleetmanagement.testUtils.fluentBuilders;

import com.emin.fleetmanagement.model.delivery.Shipment;
import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.emin.fleetmanagement.testUtils.TestDummies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeliveryBuilder {


    private Long id = (long) new Random().nextInt(100);


    private DeliveryPoint deliveryPoint = TestDummies.DELIVERY_POINT_2;

    private List<DeliveryItem> deliveryItems = new ArrayList<>(Arrays.asList(TestDummies.PACKAGE_3, TestDummies.BAG_1));


    private Shipment shipment = null;

    public Delivery build() {
        return new Delivery(id, deliveryPoint, deliveryItems, shipment);

    }


    public DeliveryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DeliveryBuilder withDeliveryPoint(DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
        return this;
    }

    public DeliveryBuilder withDeliveryItems(List<DeliveryItem> deliveryItems) {
        this.deliveryItems = deliveryItems;
        return this;
    }

    public DeliveryBuilder withShipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }


}
