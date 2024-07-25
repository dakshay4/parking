package com.dakshay.services;

import com.dakshay.data.BookedSlotDTO;
import com.dakshay.data.ParkingLotDao;
import com.dakshay.data.ParkingTransactionDao;
import com.dakshay.enums.VehicleType;
import com.dakshay.models.Floor;
import com.dakshay.models.ParkingLot;
import com.dakshay.models.ParkingTransaction;
import com.dakshay.models.Slot;
import com.dakshay.models.Vehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ParkingLotService {

    public static void main(String[] args) {
        List<String> commands = readFile();
        for (String command : commands) {
            String[] params = command.split(" ");
            if("create_parking_lot".equals(params[0])) {
                String parkingLotId = params[1];
                int levels =  Integer.parseInt(params[2]);
                int slots = Integer.parseInt(params[3]);
                ParkingLot parkingLot = createParkingLot(parkingLotId, levels, slots);
                ParkingLotDao.getInstance().save(parkingLot);
            } else if("park_vehicle".equals(params[0])) {
                String vehicleType = params[1];
                String regNo =  params[2];
                String name = params[3];
                Vehicle vehicle = new Vehicle(regNo, name, VehicleType.valueOf(vehicleType));
                BookedSlotDTO bookedSlotDTO = ParkingTransactionDao.getInstance().bookSlot(VehicleType.valueOf(vehicleType));
                if(bookedSlotDTO==null) {
                    System.out.println("Parking Lot Full");
                    continue;
                }
                String ticketId = bookedSlotDTO.getParkingLot().getId()+"_"+bookedSlotDTO.getFloor().getId()+"_"+bookedSlotDTO.getSlot().getName();
                ParkingTransaction parkingTransaction = new ParkingTransaction(ticketId, vehicle, System.currentTimeMillis(), null, bookedSlotDTO);
                ParkingTransactionDao.getInstance().save(parkingTransaction);
                System.out.println("Parked vehicle. Ticket ID: " + parkingTransaction.getId());
            } else if("unpark_vehicle".equals(params[0])) {
                String ticketId = params[1];
                ParkingTransaction parkingTransaction =  ParkingTransactionDao.getInstance().unParkVehicle(ticketId);
                if(parkingTransaction != null) System.out.println("Unparked vehicle with Registration Number: " + parkingTransaction.getVehicle().getRegNo()
                + " color: " + parkingTransaction.getVehicle().getName());
                    else System.out.println("Invalid Ticket");
            }else if("display".equals(params[0]) && "free_count".equals(params[1]) ) {
                String vehicleType = params[2];
                List<BookedSlotDTO> freeSLots = ParkingTransactionDao.getInstance().getFreeSlots(VehicleType.valueOf(vehicleType));
                Map<String, Map<String, List<String>>> availabilityChart = new HashMap<>();
                freeSLots.forEach(freeslot->{
                    Map<String, List<String>> vehicleWise = availabilityChart.getOrDefault(freeslot.getSlot().getVehicleType().name(), new HashMap<>());
                    List<String> floorWise = vehicleWise.getOrDefault(freeslot.getFloor().getId(), new ArrayList<>());
                    floorWise.add(freeslot.getSlot().getName());
                    vehicleWise.put(freeslot.getFloor().getId(), floorWise);
                    availabilityChart.put(freeslot.getSlot().getVehicleType().name(), vehicleWise);
                });
                availabilityChart.forEach((vehicletype, floors) ->{
                    floors.forEach((floor,slot)->{
                        System.out.println("No. of free slots for " + vehicletype + " " + " on Floor " + floor + " : " + slot.size());
                    });
                });
             }
            else if("display".equals(params[0]) && "free_slots".equals(params[1]) ) {
                String vehicleType = params[2];
                List<BookedSlotDTO> freeSLots = ParkingTransactionDao.getInstance().getFreeSlots(VehicleType.valueOf(vehicleType));
                Map<String, Map<String, List<String>>> availabilityChart = new HashMap<>();
                freeSLots.forEach(freeslot->{
                    Map<String, List<String>> vehicleWise = availabilityChart.getOrDefault(freeslot.getSlot().getVehicleType().name(), new HashMap<>());
                    List<String> floorWise = vehicleWise.getOrDefault(freeslot.getFloor().getId(), new ArrayList<>());
                    floorWise.add(freeslot.getSlot().getName());
                    vehicleWise.put(freeslot.getFloor().getId(), floorWise);
                    availabilityChart.put(freeslot.getSlot().getVehicleType().name(), vehicleWise);
                });
                availabilityChart.forEach((vehicletype, floors) ->{
                    floors.forEach((floor,slot)->{
                        System.out.println("Free slots for " + vehicletype + " " + " on Floor " + floor + " : " + slot);
                    });
                });
            } else if("display".equals(params[0]) && "occupied_slots".equals(params[1]) ) {
                String vehicleType = params[2];
                List<BookedSlotDTO> occupiedSlots = ParkingTransactionDao.getInstance().getOccupiedSlots();
                Map<String, Map<String, List<String>>> occupiedChart = new HashMap<>();
                occupiedSlots.forEach(occupiedSlot->{
                    Map<String, List<String>> vehicleWise = occupiedChart.getOrDefault(occupiedSlot.getSlot().getVehicleType().name(), new HashMap<>());
                    List<String> floorWise = vehicleWise.getOrDefault(occupiedSlot.getFloor().getId(), new ArrayList<>());
                    floorWise.add(occupiedSlot.getSlot().getName());
                    vehicleWise.put(occupiedSlot.getFloor().getId(), floorWise);
                    occupiedChart.put(occupiedSlot.getSlot().getVehicleType().name(), vehicleWise);
                });
                occupiedChart.forEach((vehicletype, floors) ->{
                    floors.forEach((floor,slot)->{
                        System.out.println("Occupied slots for " + vehicletype + " " + " on Floor " + floor + " : " + slot);
                    });
                });

            }
        }


    }
    private static ParkingLot createParkingLot(String parkingLotId, int levels, int noOfSlots) {
        List<Slot> slots = new ArrayList<>();
        List<Floor> floors = new ArrayList<>();
        for(int i=1;i<= noOfSlots;i++) {
            VehicleType vehicleType;
            if(i==1) vehicleType = VehicleType.TRUCK;
            else if(i==2 || i==3) vehicleType = VehicleType.BIKE;
            else vehicleType = VehicleType.CAR;
            slots.add(new Slot(String.valueOf(i), vehicleType));

        }
        for(int i=1;i<= levels;i++) {
            floors.add(new Floor(String.valueOf(i), levels, slots));
        }
        return new ParkingLot(parkingLotId, floors);
    }


    private static List<String> readFile() {
        List<String> input = new ArrayList<>();
        InputStream inputStream =  ParkingLotService.class.getResourceAsStream("/input.txt");
        try {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    input.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
