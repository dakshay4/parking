package com.dakshay.data;

import com.dakshay.enums.VehicleType;
import com.dakshay.models.Floor;
import com.dakshay.models.ParkingLot;
import com.dakshay.models.ParkingTransaction;
import com.dakshay.models.Slot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParkingTransactionDao extends Dao<ParkingTransaction> {

    private static ParkingTransactionDao INSTANCE = null;
    @Getter
    private final List<ParkingTransaction> parkingTransactions = new LinkedList<>();

    private ParkingTransactionDao(){}

    public static ParkingTransactionDao getInstance() {
        if(INSTANCE==null) {
            INSTANCE = new ParkingTransactionDao();
        }
        return INSTANCE;
    }


    @Override
    public ParkingTransaction get(String id) {
        return getActiveTransactions().stream().filter(parkingTransaction -> id.equals(parkingTransaction.getId())).findFirst().orElse(null);
    }

    @Override
    public void save(ParkingTransaction parkingTransaction) {
        parkingTransactions.add(parkingTransaction);
    }

    public ParkingTransaction unParkVehicle(String id) {
        ParkingTransaction parkingTransaction = this.get(id);
        if(parkingTransaction!=null) parkingTransaction.setExitTime(System.currentTimeMillis());
        return parkingTransaction;
    }

    public BookedSlotDTO bookSlot(VehicleType vehicleType) {
        List<BookedSlotDTO> availableSlots = getFreeSlots(vehicleType);
        return !availableSlots.isEmpty() ? availableSlots.get(0) : null;
    }


    public List<BookedSlotDTO> getFreeSlots(VehicleType vehicleType) {
        List<BookedSlotDTO> availableSlots = new ArrayList<>();
        List<ParkingLot> parkingLots = ParkingLotDao.getInstance().getParkingLots();
        Set<BookedSlotDTO> bookings = getActiveTransactions().stream().map(ParkingTransaction::getBookedSlotDTO).collect(Collectors.toSet());
        for (ParkingLot parkingLot : parkingLots) {
            for (Floor floor : parkingLot.getFloors()) {
                for (Slot slot : floor.getSlots()) {
                    if(!vehicleType.equals(slot.getVehicleType())) continue;
                    BookedSlotDTO bookedSlotDTO = new BookedSlotDTO(parkingLot, floor, slot);
                    if(!bookings.contains(bookedSlotDTO)) availableSlots.add(bookedSlotDTO);
                }
            }
        }
        return availableSlots;
    }

    public List<BookedSlotDTO> getOccupiedSlots() {
        return getActiveTransactions().stream().map(ParkingTransaction::getBookedSlotDTO).collect(Collectors.toList());
    }

    public List<ParkingTransaction> getActiveTransactions() {
        return parkingTransactions.stream()
                .filter(transaction -> transaction.getExitTime() == null).toList();
    }
}
