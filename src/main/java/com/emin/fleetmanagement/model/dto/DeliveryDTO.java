package com.emin.fleetmanagement.model.dto;

import com.emin.fleetmanagement.model.delivery.Delivery;
import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    Long deliveryPoint;

    List<DeliveryItemDTO> deliveries;




    public DeliveryDTO(Delivery delivery) {
        this.deliveryPoint = (long) delivery.getDeliveryPoint().getId();// TODO delivery point type or delivery  point id ?
        this.deliveries = new ArrayList<>();

        for (DeliveryItem item : delivery.getDeliveryItems()) {
            DeliveryItemWithStateDTO current = new DeliveryItemWithStateDTO(item.getBarcode(), item.getState().ordinal()+1);
            this.deliveries.add(current);

        }


    }
}
