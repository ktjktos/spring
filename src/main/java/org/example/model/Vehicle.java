package org.example.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@NoArgsConstructor

@Entity
@Table(name="vehicle")
public class Vehicle {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name="type_of_vehicle")
    private String typeOfVehicle;
    private String brand;
    private String model;
    @Column(name="production_year")
    private Integer year;
    private String plate;
    private Integer price;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String,Object> attributes = new HashMap<>();

    @Builder
    public Vehicle(String id, String typeOfVehicle, String brand, String model, Integer year, String plate, Integer price, Map<String,Object> attributes) {
        this.id = id;
        this.typeOfVehicle = typeOfVehicle;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.plate = plate;
        this.price = price;
        this.attributes = attributes == null ? new HashMap<>() : new HashMap<>(attributes);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    public void addAttribute(String key, Object value) {
        attributes.put(key,value);
    }
    public void removeAttribute(String key) {
        attributes.remove(key);
    }
    public Vehicle copy() {
        return Vehicle.builder()
                .id(id)
                .typeOfVehicle(typeOfVehicle)
                .brand(brand)
                .model(model)
                .year(year)
                .plate(plate)
                .price(price)
                .attributes(new HashMap<>(attributes))
                .build();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(id).append("| ").append(brand).append(" ").append(model).append(" ").append(year).append(" price: ").append(price).append(" PLN");
//        if (!attributes.isEmpty()) {
//            sb.append(" | Attributes: ").append(attributes);
//        }
        sb.append(brand).append(" ").append(model).append(" ").append(year);
        return sb.toString();
    }

    public void clearAttributes() {
        attributes.clear();
    }
}
