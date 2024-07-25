package com.dakshay.data;

import com.dakshay.enums.SlotStatus;
import com.dakshay.enums.VehicleType;
import com.dakshay.models.Floor;
import com.dakshay.models.ParkingLot;
import com.dakshay.models.Slot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotDao extends Dao<ParkingLot> {

    private static ParkingLotDao INSTANCE = null;
    @Getter
    private final List<ParkingLot> parkingLots = new ArrayList<>();
    @Getter
    private final Map<VehicleType, Integer> slotAvailable = new ConcurrentHashMap<>();

    private ParkingLotDao(){}

    public static ParkingLotDao getInstance() {
        if(INSTANCE==null)
            INSTANCE = new ParkingLotDao();
        return INSTANCE;
    }

    public void save(ParkingLot parkingLot) {
        parkingLots.add(parkingLot);
        parkingLot.getFloors().forEach(floor-> floor.getSlots().forEach(slot->{
            int count = slotAvailable.getOrDefault(slot.getVehicleType(),0);
            slotAvailable.put(slot.getVehicleType(), count+1);
        }));
    }

    @Override
    public List<ParkingLot> getActiveTransactions() {
        return new ArrayList<>();
    }

    public Integer getTotalSlots(VehicleType vehicleType) {
       return parkingLots.stream().mapToInt(parkingLot -> parkingLot.getFloors()
                .stream().mapToInt(floor-> Math.toIntExact(floor.getSlots().stream().filter(e -> vehicleType.equals(e.getVehicleType())).count())).sum()).sum();
    }

    @Override
    public ParkingLot get(String id) {
        return parkingLots.stream().filter(parkingLot -> id.equals(parkingLot.getId())).findFirst().orElse(null);
    }
}
