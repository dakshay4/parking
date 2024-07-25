package com.dakshay.models;

import lombok.Data;

import java.util.List;


@Data
public class Floor {

    private String id;
    private int totalCapacity;
    private List<Slot> slots;

    public Floor(String id, int totalCapacity, List<Slot> slots) {
        this.id = id;
        this.totalCapacity = totalCapacity;
        this.slots = slots;
    }
}
