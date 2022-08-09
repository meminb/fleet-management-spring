package com.emin.fleetmanagement.model.delivery;

import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deliver_point_id", referencedColumnName = "id")
    private DeliveryPoint deliveryPoint;



    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeliveryItem> deliveryItems;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="shipment_id")
    private Shipment shipment;
    public Delivery( DeliveryPoint deliveryPoint, List<DeliveryItem> deliveryItemList) {
        this.deliveryPoint=deliveryPoint;
        this.deliveryItems=deliveryItemList;
    }
    public Delivery(Long deliveryId, DeliveryPoint deliveryPoint, List<DeliveryItem> deliveryItemList) {
        this.id = deliveryId;
        this.deliveryPoint=deliveryPoint;
        this.deliveryItems=deliveryItemList;
    }
}
