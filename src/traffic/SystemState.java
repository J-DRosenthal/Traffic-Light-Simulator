package traffic;

import java.util.List;
import java.util.Queue;

public class SystemState extends Thread {
    int interval;
    Queue<Road> roadQueue;
    long lastTimeChanged;
    long startTime;
    int openRoadIndex = 0;

    public SystemState(String name, int interval, Queue<Road> roadQueue) {
        super(name);
        this.interval = interval;
        this.roadQueue = roadQueue;
        this.startTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval * 1000);
                openRoadIndex = openRoadIndex >= roadQueue.size() - 1 ? 0 : openRoadIndex + 1;
                lastTimeChanged = System.nanoTime();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}


    void printState() {

        System.out.printf("""
                ! %ds. have passed since system startup !
                ! Number of roads: %d !
                ! Intervals: %d !
                
                """, (System.nanoTime() - startTime) / 1000000000, roadQueue.size(), interval);
        displayRoads(openRoadIndex);
        System.out.println("! Press \"Enter\" to open menu !");
    }

    void displayRoads(int openRoadIndex) {

        List<Road> roadList = roadQueue.stream().toList();
        long timeSinceChange = (System.nanoTime() - lastTimeChanged) / 1000000000;

        for (int i = 0; i < roadList.size(); i++) {
            long timeUntilSpecificRoadChange = i == openRoadIndex ? interval - timeSinceChange :
                    i > openRoadIndex ? interval * (i - openRoadIndex) - timeSinceChange :
                            interval * (roadList.size() - openRoadIndex + i) - timeSinceChange;


            System.out.printf("Road \"%s\" will be %s for %ds\n",
                    roadList.get(i).name,
                    i == openRoadIndex ? "\u001B[32m" + "Open" + "\u001B[0m" : "\u001B[31m" + "Closed" + "\u001B[0m",
                    timeUntilSpecificRoadChange);
        }
        System.out.println();
    }
}
