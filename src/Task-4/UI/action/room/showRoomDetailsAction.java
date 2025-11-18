package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;

import java.util.Scanner;

public class showRoomDetailsAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public showRoomDetailsAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.print("\nВведите номер комнаты: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.println(manager.getRoomDetails(roomNumber));
    }
}