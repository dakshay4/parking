package com.dakshay.models;


import lombok.Data;

import java.util.List;

@Data
public class ParkingLot {


    private final String id;
    private final List<Floor> floors;

    public ParkingLot(String id, List<Floor> floors) {
        this.id = id;
        this.floors = floors;
    }
}
