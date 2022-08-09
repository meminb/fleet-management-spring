package com.emin.fleetmanagement.model.delivery.item;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.utils.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("bag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Bag extends DeliveryItem {


    @OneToMany(mappedBy = "bag", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    List<Package> packages;


    public Bag(String bagBarcode, DeliveryPoint unloadingPoint) {
        super(bagBarcode, unloadingPoint, State.CREATED, null);
        this.packages = Collections.emptyList();

    }


    public Bag(String bagBarcode, List<Package> packages, DeliveryPoint unloadingPoint, State status, Delivery delivery) {
        super(bagBarcode, unloadingPoint, status, delivery);
        this.packages = packages;
    }

    public Bag(String bagBarcode, List<Package> packages, DeliveryPoint unloadingPoint, State status) {
        super(bagBarcode, unloadingPoint, status, null);
        this.packages = packages;
    }

    public void insertPackage(Package pack){
        packages.add(pack);
    }

}
