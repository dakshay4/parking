package com.dakshay.models;

import com.dakshay.enums.VehicleType;
import lombok.Data;

@Data
public class Vehicle {

    private final String regNo;
    private final String name;
    private final VehicleType vehicleType;
}
