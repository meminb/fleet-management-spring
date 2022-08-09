package com.emin.fleetmanagement.testUtils.fluentBuilders;

import com.emin.fleetmanagement.model.delivery.Shipment;
import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.Vehicle;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentBuilder {


    private Long id = 1l;


    private Date date = Date.valueOf(LocalDate.now());


    private List<Delivery> deliveries = new ArrayList<>();

    private Vehicle vehicle = null;


    public Shipment build() {
        return new Shipment(this.id, this.date, this.vehicle, this.deliveries);
    }


    public ShipmentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShipmentBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public ShipmentBuilder withVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public ShipmentBuilder withGeneratedVehicle() {
        this.vehicle = new VehicleBuilder().build();
        return this;
    }

    public ShipmentBuilder withDeliveries(List<Delivery> deliveries) {
        this.deliveries.addAll(deliveries);
        return this;
    }
    public ShipmentBuilder withNumberOfRandomDeliveries(int numberOfDeliveries) {
        for (int i = 0; i < numberOfDeliveries; i++) {
            this.deliveries.add(new DeliveryBuilder().build());
        }
        return this;
    }

}
