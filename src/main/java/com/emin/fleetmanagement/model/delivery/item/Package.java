package com.emin.fleetmanagement.model.delivery.item;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.utils.State;
import lombok.*;

import javax.persistence.*;

@Entity
@DiscriminatorValue("package")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Package extends DeliveryItem{

    @ManyToOne (cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="bag_barcode")
    private Bag bag;

    @Column(name = "weight")
    private int weight;

    public Package(String barcode, DeliveryPoint unloadingPoint,int weight) {
        super(barcode, unloadingPoint, State.CREATED, null);
        this.weight=weight;

    }

    public Package(String barcode, DeliveryPoint unloadingPoint, State status, Delivery delivery, int weight) {
     super(barcode,unloadingPoint,status,delivery);this.weight=weight;
        this.bag=null;
    }

    public Package(String barcode, DeliveryPoint unloadingPoint, State status, int weight) {
        super(barcode,unloadingPoint,status,null);
        this.weight=weight;
        this.bag=null;
    }


}
