package com.emin.fleetmanagement.model.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity@Table(name = "vehicles")
@Data
@AllArgsConstructor
@NoArgsConstructor@Getter
public class Vehicle {

    @Id
    @Column(name = "plate", nullable = false)
    private String plate;


    @Column(name = "model")
    private String model;


}