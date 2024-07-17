package com.bunny.magic_mouse;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@SpringBootApplication
public class MagicMouseApplication {

    public static void main(String[] args) throws InterruptedException, AWTException, CsvValidationException, IOException {
        SpringApplication.run(MagicMouseApplication.class, args);

        System.setProperty("java.awt.headless", "false");
        System.out.println("1. autocliker: ");
        System.out.println("2. record mouse movement: ");
        System.out.println("3. start script: ");
        Scanner keyboard = new Scanner(System.in);
        int command = keyboard.nextInt();
        List<Command> commandList = new ArrayList<>();

        while (command != 0) {
            if (command == 1) {
                runAutoClicker();
            }

            if (command == 2) {
                commandList = mouseRecorder();
                System.out.println("press 3 to start bot");
                command = keyboard.nextInt();
            }

            if (command == 3) {

                System.out.println("Name of script: ");
                String script = keyboard.next();
                commandList = readFile(script);
                System.out.println("Amount of loops: ");
                int loops = keyboard.nextInt();
                List<Integer> intervals = getIntervals(script);
                Thread.sleep(2000);
                for (int i = 0; i < loops; i++) {
                    runMouseMover(commandList, intervals);
                }
                command = keyboard.nextInt();
            }
        }

    }

    private static List<Integer> getIntervals(String script) {
        List<Integer> integers = new ArrayList<>();
        return switch (script) {
            case "canifis" -> List.of(8, 12, 3, 6, 4, 9, 4, 9, 4, 7, 4, 12, 12, 9);
            case "fruitstall" -> List.of(2, 3);
            case "iron" -> List.of(1, 3, 1, 3, 1, 3);
            default -> null;
        };
    }

    private static List<Command> mouseRecorder() throws InterruptedException {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point pos = new Point();
                pos = MouseInfo.getPointerInfo().getLocation();
                System.out.println(pos.x + " " + pos.y);
            }
        };

        Scanner keyboard = new Scanner(System.in);
        System.out.println("press any key to start and '0' to stop and 1 to get coordinates");
        int command = keyboard.nextInt();
        List<Command> commandList = new ArrayList<>();
        Point pos = new Point();
        while (command != 0) {
            pos = MouseInfo.getPointerInfo().getLocation();
            System.out.println(pos.x + " " + pos.y);
            commandList.add(new Command(pos.x, pos.y, null));
            command = keyboard.nextInt();
        }
        return commandList;
    }

    private static void runMouseMover(List<Command> commandList, List<Integer> integers) throws AWTException, InterruptedException {

        Robot r = new Robot();

        for (int i = 0; i < commandList.size(); i++) {
            int x = commandList.get(i).getX();
            int y = commandList.get(i).getY();
            int wait = integers.get(i) * 650;
            moveMouse(x, y, r);
            Thread.sleep(wait);
            mouseClick();
            System.out.println("move: " + commandList.get(i).getDescription());
            System.out.println("waited: " + (wait/0.6));
        }
        System.out.println("end of command");
        return;
    }

    private static List<Command> readFile(String script) throws IOException, CsvValidationException {
        String fileName = "src\\main\\resources\\" + script + ".csv";
        CSVReader reader = new CSVReader(new FileReader(fileName));
        List<Command> commandList = new ArrayList<>();
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {

            int x = formatStringToNumber(nextLine[0]);
            int y = formatStringToNumber(nextLine[1]);
            String description = nextLine[2];
            Command command = new Command(x, y, description);
            commandList.add(command);
        }
        return commandList;
    }

    public static int formatStringToNumber(String string) {
        string = string.replace("\uFEFF", "");
        boolean isNumber = Pattern.matches("[0-9]+", string);
        if (isNumber) {
            return Integer.parseInt(string);
        }

        throw new NumberFormatException();
    }

    private static void moveMouse(int x, int y, Robot r) {
        Point pd = new Point(x, y); // X,Y where mouse must go
        int n = 0;

        while ((!pd.equals(MouseInfo.getPointerInfo().getLocation())) && (++n <= 5)) {
            r.mouseMove(pd.x, pd.y);
        }

    }

    public static void mouseClick() throws AWTException {
        Robot bot = new Robot();
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void shiftClick() throws AWTException {
        Robot bot = new Robot();
        bot.keyPress(KeyEvent.SHIFT_DOWN_MASK);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        bot.keyRelease(KeyEvent.SHIFT_DOWN_MASK);
    }

    public static void wait(int x) {
        try {
            Thread.sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runAutoClicker() throws AWTException {
        wait(2500);
        long max = 9223372036854775807L;
        for (long i = 0; i < max; i++) {
            wait(new Random().nextInt(1000 - 100 + 1) + 1000);
//            wait(new Random().nextInt(10000 - 1000 + 1) + 1000);
            mouseClick();
        }
    }

}
