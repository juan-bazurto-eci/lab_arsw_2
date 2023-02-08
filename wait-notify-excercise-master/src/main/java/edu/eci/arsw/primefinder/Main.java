package edu.eci.arsw.primefinder;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Control control = Control.newControl();
        control.start();

        while (control.threadsRunning()) {
            long startTime = System.nanoTime();
            TimeUnit.SECONDS.sleep(2);
            long endTime = System.nanoTime();
            control.hold();
            control.counter();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Press enter to continue");
            String input = scanner.nextLine();
            if (Objects.equals(input, "")) {
                control.restart();
            }
        }
    }
}
