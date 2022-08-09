package com.emin.fleetmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
public class DeliveryItemWithStateDTO extends  DeliveryItemDTO{

    int state;

    public DeliveryItemWithStateDTO(String barcode, int status) {
        super(barcode);
        this.state = status;

    }
}
