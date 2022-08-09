package com.emin.fleetmanagement.model.delivery.item;


import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.DeliveryPoint;
import com.emin.fleetmanagement.utils.State;
import lombok.*;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity(name = "items")
@Table(name = "delivery_items")
@DiscriminatorColumn(name = "item_type",
        discriminatorType = DiscriminatorType.STRING)
@AllArgsConstructor
@NoArgsConstructor@Getter
@Setter
public class DeliveryItem {
    @Id
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @OneToOne()
    @JoinColumn(name = "unloading_point", referencedColumnName = "id")
    private DeliveryPoint unloadingPoint;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne()
    @JoinColumn(name="delivery_id")
    private Delivery delivery;



    public int getStatusAsValue(){
        return  State.valueOf(this.state.toString()).ordinal();
    }



}
