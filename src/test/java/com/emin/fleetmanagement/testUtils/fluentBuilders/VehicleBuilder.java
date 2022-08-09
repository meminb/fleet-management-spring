package com.emin.fleetmanagement.testUtils.fluentBuilders;

import com.emin.fleetmanagement.model.delivery.Vehicle;

import javax.persistence.Column;
import javax.persistence.Id;

public class VehicleBuilder {


    private String plate="34 TY 34";


    private String model="Mercedes";

    public Vehicle build(){
        return new Vehicle(this.plate,this.model);
    }

    public VehicleBuilder withPlate(String plate) {
        this.plate = plate;
        return this;
    }

    public VehicleBuilder withModel(String model) {
        this.model = model;
        return this;
    }
}
