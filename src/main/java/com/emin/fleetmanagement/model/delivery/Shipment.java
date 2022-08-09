package com.emin.fleetmanagement.model.delivery;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.Vehicle;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "creation_date", nullable = false)
    private Date date;


    @OneToOne()
    @JoinColumn(name = "vehicle_plate", referencedColumnName = "plate")
    private Vehicle vehicle;


    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Delivery> deliveries;



}
