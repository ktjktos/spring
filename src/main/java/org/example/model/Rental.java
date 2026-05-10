package org.example.model;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Rental {
    private String id;
    private String vehicleId;
    private String userId;
    private Date rentDateTime;
    private Date returnDateTime;

    public Rental copy() {
        return Rental.builder()
                .id(id)
                .vehicleId(vehicleId)
                .userId(userId)
                .rentDateTime(rentDateTime)
                .returnDateTime(returnDateTime)
                .build();
    }
}
