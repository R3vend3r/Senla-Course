package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;

import java.util.Scanner;

public class showLastThreeRoomBookingsAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showLastThreeRoomBookingsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nИстория номера\nВведите номер: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Последние 3 клиента:");
        manager.getLastThreeBookingsForRoom(roomNumber)
                .forEach(b -> System.out.println(b.getClient()));
    }
}