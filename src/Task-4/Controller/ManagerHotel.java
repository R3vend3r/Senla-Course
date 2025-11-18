package Controller;

import enums.RoomCondition;
import enums.SortType;
import interfaceClass.*;
import java.util.*;
import java.util.stream.Collectors;

import model.*;
import service.*;

public class ManagerHotel {
    private final RoomService roomService;
    private final AmenityService amenityService;
    private final ClientService clientService;
    private final OrderService orderService;

    public ManagerHotel() {
        IRoomRepository roomRepository = new RoomRepository();
        IAmenityRepository amenityRepository = new AmenityRepository();
        IClientRepository clientRepository = new ClientRepository();
        IOrderRepository orderRepository = new OrderRepository();

        this.roomService = new RoomService(roomRepository);
        this.amenityService = new AmenityService(amenityRepository);
        this.clientService = new ClientService(clientRepository);
        this.orderService = new OrderService(orderRepository);
    }

    public void settleClient(Client client, Room room, Date checkOutDate) {
        validateSettlementParameters(client, room, checkOutDate);
        validateRoomAvailability(room);
        executeClientSettlement(client, room, checkOutDate);
    }

    private void validateSettlementParameters(Client client, Room room, Date checkOutDate) {
        Objects.requireNonNull(client, "Client cannot be null");
        Objects.requireNonNull(room, "Room cannot be null");
        Objects.requireNonNull(checkOutDate, "Check-out date cannot be null");
    }

    private void validateRoomAvailability(Room room) {
        if (!roomService.findRoom(room.getNumberRoom())
                .map(Room::isAvailable)
                .orElse(false)) {
            throw new IllegalStateException("Room " + room.getNumberRoom() + " is not available");
        }
    }

    private void executeClientSettlement(Client client, Room room, Date checkOutDate) {
        orderService.createRoomBooking(client, room, new Date(), checkOutDate);
        roomService.assignClientToRoom(room.getNumberRoom(), client.getClientId(), checkOutDate);
        roomService.markRoomOccupied(room);
        clientService.assignRoomToClient(client.getClientId(), room.getNumberRoom());
    }

    public void evictClient(int roomNumber) {
        completeRoomBooking(roomNumber);
        clearRoomAssignment(roomNumber);
    }

    private void completeRoomBooking(int roomNumber) {
        orderService.completeRoomBooking(roomNumber, new Date());
    }

    private void clearRoomAssignment(int roomNumber) {
        roomService.clearRoom(roomNumber);
        clientService.removeClientByRoomNumber(roomNumber);
    }

    public Optional<Client> findClientByRoom(int roomNumber) {
        return clientService.findClientByRoomNumber(roomNumber);
    }

    public Optional<Room> findRoom(int roomNumber) {
        return roomService.findRoom(roomNumber);
    }

    public Optional<Client> findClientById(String clientId) {
        return clientService.findClientById(clientId);
    }

    public Optional<Amenity> findAmenityByName(String name) {
        return amenityService.findAmenityByName(name);
    }

    public double calculateTotalIncome() {
        return orderService.calculateTotalIncome();
    }

    public void registerClient(Client client) {
        clientService.registerClient(client);
    }

    public void addRoom(Room room) {
        roomService.addRoom(room);
    }

    public void addAmenity(Amenity amenity) {
        amenityService.addAmenity(amenity);
    }

    public void updateRoomStatus(int number, RoomCondition status) {
        roomService.updateRoomStatus(number, status);
    }

    public void updateRoomPrice(int number, double newPrice) {
        roomService.updateRoomPrice(number, newPrice);
    }

    public void updateAmenityPrice(String amenityName, double newPrice) {
        amenityService.updateAmenityPrice(amenityName, newPrice);
    }

    public Map<Integer, Room> getRooms(SortType sortType, boolean onlyAvailable) {
        return onlyAvailable
                ? roomService.getSortedAvailableRooms(sortType)
                : roomService.getSortedRooms(sortType);
    }

    public List<RoomBooking> getAllActiveBookings(SortType sortType) {
        return orderService.getActiveBookingsSorted(sortType);
    }

    public List<RoomBooking> getAllCompletedBookings() {
        return orderService.getCompletedBookings();
    }

    public List<AmenityOrder> getClientAmenitiesSorted(Client client, SortType sortType) {
        return filterAmenityOrdersByClient(client, orderService.getAmenityOrdersSorted(sortType));
    }

    private List<AmenityOrder> filterAmenityOrdersByClient(Client client, List<AmenityOrder> orders) {
        return orders.stream()
                .filter(order -> order.getClient().equals(client))
                .collect(Collectors.toList());
    }

    public List<Amenity> getAmenities(SortType sortType) {
        return getSortedAmenities(sortType);
    }

    private List<Amenity> getSortedAmenities(SortType sortType) {
        return switch (sortType) {
            case PRICE -> amenityService.getAmenitiesSortedByPrice();
            case ALPHABET -> amenityService.getAmenitiesSortedByName();
            case NONE -> amenityService.getAllAmenities();
            default -> throw new IllegalArgumentException("Unsupported sort type for amenities");
        };
    }

    public void addAmenityToClient(int roomNumber, Amenity amenity, Date serviceDate) {
        validateRoomNumber(roomNumber);
        orderService.addAmenityToBooking(roomNumber, amenity, serviceDate);
    }

    private void validateRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalStateException("Client is not assigned to any room");
        }
    }

    public List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber) {
        return orderService.getLastThreeBookingsForRoom(roomNumber);
    }

    public Map<Integer, Room> getAvailableRoomsByDate(Date date) {
        return roomService.getAvailableRoomsByDate(date);
    }

    public double calculateRoomPayment(int roomNumber, Date endDate) {
        double roomCost = roomService.calculateStayCost(roomNumber, endDate);
        double amenityCost = orderService.calculateAmenityCost(roomNumber);
        return roomCost + amenityCost;
    }

    public int getAvailableRoomsCount() {
        return roomService.countAvailableRooms();
    }

    public boolean isRoomAvailable(int roomNumber) {
        return roomService.isRoomAvailable(roomNumber);
    }

    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    public int getClientCount() {
        return clientService.getClientCount();
    }

    public String getRoomDetails(int roomNumber) {
        return roomService.getRoomDetails(roomNumber);
    }
}