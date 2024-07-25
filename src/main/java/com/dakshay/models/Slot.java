package com.dakshay.models;

import com.dakshay.enums.SlotStatus;
import com.dakshay.enums.VehicleType;
import lombok.Data;

@Data
public class Slot {

    private final String name;
    private final VehicleType vehicleType;

    public Slot(String name, VehicleType vehicleType) {
        this.name = name;
        this.vehicleType = vehicleType;
    }
}
