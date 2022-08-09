package com.emin.fleetmanagement.model.delivery;

import com.emin.fleetmanagement.utils.DeliveryPointType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity@Getter
public class DeliveryPoint  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryPointType deliveryPointType;


    public DeliveryPoint(String name, DeliveryPointType type) {
        this.name = name;this.deliveryPointType = type;
    }
}
