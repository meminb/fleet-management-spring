package com.emin.fleetmanagement.repository;

import com.emin.fleetmanagement.model.delivery.item.DeliveryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem,String> {
}
