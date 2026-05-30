package org.example.model;

import lombok.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor

public class VehicleCategoryConfig {
    private String category;
    private Map<String, String> attributes = new HashMap<>();
    @Builder
    public VehicleCategoryConfig(String category,Map<String, String> attributes) {
        this.category = category;
        this.attributes = attributes == null ? new HashMap<>() : new HashMap<>(attributes);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
    public void addAttribute(String name, String type) {attributes.put(name,type);}
    public void removeAttribute(String name) {attributes.remove(name);}
    public VehicleCategoryConfig copy() {
        return VehicleCategoryConfig.builder()
                .category(category)
                .attributes(new HashMap<>(attributes))
                .build();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();;
        sb.append(category).append(" attributes:\n").append(" ");
        attributes.forEach((key,value) -> {
            sb.append(key).append(": ").append(value).append("\n ");
        });
        return sb.toString();
    }
}
