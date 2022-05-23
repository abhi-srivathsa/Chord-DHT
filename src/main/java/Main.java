import Exceptions.NotInitializedException;
import Exceptions.PortException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Boolean exit = false;
        Scanner scan = new Scanner(System.in);
        while (!exit) {
            System.out.println("Select an option:\n 1 to Create a Chord ring \n 2 to Join an existing join ring\n 3 to add a new file \n 4 for to retrieve a file\n 5 to delete a file\n 6 to terminate a node \n 7 to print the finger tables and status\n 8 to exit ");
            String input = scan.nextLine();
            switch (input) {
                case 1:
                    create();
                    break;
                case 2:
                    join();
                    break;
                case 3:
                    insertFile();
                    break;
                case 4:
                    searchKey();
                    break;
                case 5:
                    deleteFile();
                    break;
                case 6:
                    terminate();
                    break;
                case 7:
                    Chord.printChord();
                    break;
                case 8:
                    Chord.deleteAll();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, try again! ");
                    break;
            }
        }
    }

    public static void create() {
        String ip = null;
        Scanner scan = new Scanner(System.in);
        try {
            InetAddress local = InetAddress.getLocalHost();
            ip = local.getHostAddress();
            System.out.println(" The IP address of the machine is: " + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Enter the port number to create the ring: ");
        int port = scan.nextInt();
        try {
            Chord.create(ip, port);
        } catch (PortException e) {
            System.out.println("The chosen port is already in use. Your new port is: " + e.getPort());
        }
        scan.skip("\n");
    }


    public static void join() {
        String ip = null;
        Scanner scan = new Scanner(System.in);
        try {
            InetAddress local = InetAddress.getLocalHost();
            ip = local.getHostAddress();
            System.out.println("Your IP address is: " + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Enter the IP address of the Chord Ring: ");
        String chordIp = scan.nextLine();
        System.out.println("Enter the port Number of the already known node: ");
        int chordPort = scan.nextInt();
        System.out.println("Enter the port number where your process is running: ");
        int port = scan.nextInt();

        try {
            Chord.join(ip, port, chordIp, chordPort);
        } catch (PortException e) {
            System.out.println("The chosen port is already in use. Your new port is: " + e.getPort());
        } catch (NotInitializedException e) {
            System.out.println(e.getMessage() + ". Try with another IP address");
        }
        scan.skip("\n");
    }

    public static void insertFile() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the data to be stored in the file : ");
        String text = scan.nextLine();
        Data data = new Data(text);
        System.out.println("Enter the port number where your process is running: ");
        int port = scan.nextInt();
        String key = null;
        try {
            key = Chord.publish(data, port);
            System.out.println("Data is published, The generated key for the file is: " + key);
        } catch (NotInitializedException e) {
            System.out.println(e.getMessage());
        }
        scan.skip("\n");
    }

    public static void searchKey() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the 4 character file key:");
        String key = scan.nextLine();

        if (key.length() == 4) {
            System.out.println("Enter the current system's port number : ");
            int port = scan.nextInt();
            try {
                String file = Chord.lookup(key, port);
                if (file != null) {
                    System.out.println(file);
                } else {
                    System.out.println("This file does not exist");
                }
            } catch (NotInitializedException e) {
                System.out.println(e.getMessage());
            }
            scan.skip("\n");
        } else {
            System.out.println("Invalid Key!");
        }
    }

    public static void deleteFile() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the 4 character file key:");
        String key = scan.nextLine();
        if (key.length() == 4) {
            System.out.println("Enter the current system's port number :");
            int port = scan.nextInt();
            try {
                Chord.deleteFile(key, port);
            } catch (NotInitializedException e) {
                System.out.println(e.getMessage());
            }
            scan.skip("\n");
        } else
            System.out.println("Invalid Key!");
    }

    public static void terminate() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the current system's port number :");
        int port = scan.nextInt();
        try {
            Chord.deleteNode(port);
        } catch (NotInitializedException e) {
            e.printStackTrace();
        }
        scan.skip("\n");
    }
}
