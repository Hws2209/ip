package ip.main;

import ip.task.Todo;
import ip.task.Deadline;
import ip.task.Event;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Duke {
    private static Storage storage;
    private static TaskList tasks = new TaskList();

    public static void main(String[] args) {
        String logo = "  ____   _   _      __     ______    _       _  _____\n" +
                " / ___| | | | |    /  \\    |  _  \\  | |     | ||  ___|\n" +
                "| |     | |_| |   / /\\ \\   | |_| /  | |     | || |___\n" +
                "| |     |  _  |  / /__\\ \\  |  __ \\  | |     | ||  ___|\n" +
                "| |___  | | | | / ______ \\ | |  \\ \\ | |____ | || |___\n" +
                " \\____| |_| |_|/_/      \\_\\|_|   \\_\\|______||_||_____|\n";

        System.out.println("Hello! I'm Charlie!\n" + logo);
        System.out.println("What can I do for you?");

        storage = new Storage("./data/task_list.txt");


        try {
            storage.readStoredData(tasks);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return;
        }
        
        readInputAndExecute();
    }

    private static void readInputAndExecute() {
        Scanner in = new Scanner(System.in);
        String line;
        while (true) {
            line = in.nextLine().trim();
            if (line.equals("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                break;
            }

            if (line.equals("list")) {
                printTaskList();
                continue;
            }

            try {
                if (line.startsWith("mark")) {
                    markTask(line);
                    storage.updateStoredData(tasks);
                    continue;
                }

                if (line.startsWith("unmark")) {
                    unmarkTask(line);
                    storage.updateStoredData(tasks);
                    continue;
                }

                if (line.startsWith("delete")) {
                    deleteTask(line);
                    storage.updateStoredData(tasks);
                    continue;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("     Please provide a number from 1 to " + tasks.size());
                continue;
            } catch (NumberFormatException e) {
                System.out.println("     Please input an integer");
                continue;
            }

            boolean shouldUpdate = addTask(line);
            if (shouldUpdate) {
                storage.updateStoredData(tasks);
            }
        }
    }

    private static boolean addTask(String line) {
        if (line.startsWith("todo")) {
            try {
                tasks.add(new Todo(line));
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("     Please input in the form 'todo <description>'");
                return false;
            }
        } else if (line.startsWith("deadline")) {
            try {
                tasks.add(new Deadline(line));
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("     Please input in the form 'deadline <description> /by <when>'");
                return false;
            }
        } else if (line.startsWith("event")) {
            try {
                tasks.add(new Event(line));
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("     Please input in the form 'event <description> /from <when> /to <when>'");
                return false;
            }
        } else {
            System.out.println("     Possible commands: bye, list, mark, unmark, todo, deadline, event");
            return false;
        }

        System.out.println("     New task added: " + tasks.get(tasks.size() - 1).getDetails());
        System.out.println("     Current number of tasks: " + tasks.size());
        return true;
    }

    private static void deleteTask(String line) {
        int indexDeleted = Integer.parseInt((line.substring(7))) - 1;
        System.out.println("     Task removed: " + tasks.get(indexDeleted).getDetails());
        tasks.remove(indexDeleted);
        System.out.println("     Current number of tasks: " + tasks.size());
    }

    private static void printTaskList() {
        System.out.println("     Here is your list of tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i).getDetails());
        }
    }

    private static void markTask(String line) {
        int indexMarked = Integer.parseInt(line.substring(5)) - 1;
        tasks.get(indexMarked).setDone(true);
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + tasks.get(indexMarked).getDetails());
    }

    private static void unmarkTask(String line) {
        int indexUnmarked = Integer.parseInt(line.substring(7)) - 1;
        tasks.get(indexUnmarked).setDone(false);
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + tasks.get(indexUnmarked).getDetails());
    }
}
