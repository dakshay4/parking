package com.dakshay.data;

import com.dakshay.models.Floor;
import com.dakshay.models.ParkingLot;
import com.dakshay.models.Slot;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookedSlotDTO {

    private final ParkingLot parkingLot;
    private final Floor floor;
    private final Slot slot;
}
