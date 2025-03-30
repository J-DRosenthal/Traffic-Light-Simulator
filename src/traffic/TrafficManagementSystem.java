package traffic;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class TrafficManagementSystem {
    int roads;
    int interval;
    SystemState systemState;
    Queue<Road> roadQueue;

    public TrafficManagementSystem() {
        System.out.println("Welcome to the traffic management system!");
        setRoads();
        this.roadQueue = new ArrayBlockingQueue<>(roads);
        setInterval();
        this.systemState = new SystemState("QueueThread", interval, roadQueue);
        systemState.start();
        menu();
    }

    public void setRoads() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input the number of roads: ");

        int input;
        do {
            input = sc.nextInt();
            if (input < 1) {
                System.out.print("Incorrect input. Try again: ");
            }
        } while (input < 1);

        this.roads = input;
    }

    public void setInterval() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input the interval: ");
        int input;
        do {
            input = sc.nextInt();
            if (input < 1) {
                System.out.print("Incorrect input. Try again: ");
            }
        } while (input < 1);

        this.interval = input;
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        System.out.print("""
                Menu:
                1. Add road
                2. Delete road
                3. Open system
                0. Quit
                """);
        switch (sc.nextLine()) {
            case "1" -> {
                addRoad();
                menu();
            }
            case "2" -> {
                deleteRoad();
                menu();
            }
            case "3" -> {
                openSystem();
            }
            case "0" ->{
                System.out.println("Bye!");
                System.exit(0);
            }
            default -> {
                System.out.println("Incorrect input, please enter a number (0-3)");
                sc.nextLine();
                menu();
            }
        }
    }

    public void addRoad() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input road name");
        try {
            roadQueue.add(new Road(sc.nextLine()));
            System.out.println("Road added");
        } catch (IllegalStateException e) {
            System.out.println("Queue is full");
        }
    }
    public void deleteRoad() {
        try {
            roadQueue.remove();
            System.out.println("Road deleted");
        } catch (NoSuchElementException e) {
            System.out.println("Queue is empty");
        }

    }
    public void openSystem() {
        Scanner sc = new Scanner(System.in);
        try {
            while (System.in.available() == 0) {
                Thread.sleep(1000);
                systemState.printState();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        sc.nextLine();
        menu();
    }
}