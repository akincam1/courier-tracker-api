package org.migros.adapter.courier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.migros.adapter.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "courier-location")
public class CourierEntity extends BaseEntity {

    private String courierId;

    private Double lat;

    private Double lng;

    private String storeName;

    private LocalDateTime entranceTime;
}
