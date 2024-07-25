package com.dakshay.models;


import com.dakshay.data.BookedSlotDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParkingTransaction {


    private String id;
    private Vehicle vehicle;
    private Long entryTime;
    private Long exitTime;
    private BookedSlotDTO bookedSlotDTO;



}
