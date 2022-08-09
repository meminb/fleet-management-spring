package com.emin.fleetmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDTO {

    String plate;

    List<DeliveryDTO> route;



}
