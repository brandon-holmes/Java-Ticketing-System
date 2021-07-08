//This is a Ticketing System terminal program. The user inputs commands via the terminal and will update the system accordingly.
package app;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.FileWriter; // Import this class to write to File
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.*;

public class App {
    // public arraylist for userfile?
    // public arraylist for ticketsfile?
    // public arraylist for transactions?

    public static ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();

    // // public boolean for logged in status?
    public static Boolean loggedInStatus = false;

    public static void main(String[] args) throws Exception {
        /*
         * Read in user file userFile in format -> userName,userType,userCredit\n
         */
        ArrayList<String> usersList = new ArrayList<String>();
        File usersFile = new File("tickets/userfile.txt");
        Scanner scanner = new Scanner(usersFile);
        while (scanner.hasNextLine()) {
            String[] users = (scanner.nextLine()).split(",");
            usersList.add(Arrays.toString(users));
        }
        scanner.close();

        /*
         * Read in tickets file tickets file in format ->
         * eventName,seller,numberOfTickets,price\n
         */
        ArrayList<String> ticketsList = new ArrayList<String>();
        File ticketsFile = new File("tickets/ticketsfile.txt");
        Scanner scannerTickets = new Scanner(ticketsFile);
        while (scannerTickets.hasNextLine()) {
            String[] tickets = (scannerTickets.nextLine()).split(",");
            ticketsList.add(Arrays.toString(tickets));
        }
        scannerTickets.close();

        // Log In ->
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your username: ");
        String username = input.next();

        for (int x = 0; x < usersList.size(); x++) {
            if (usersList.get(x).split(",")[0].substring(1).equals(username)) {
                System.out.println("Welcome to the Ticket Selling System, " + username);
                loggedInStatus = true;
            }
        }
        if (loggedInStatus == false) {
            System.err.println("Invalid User");
        }

        while (loggedInStatus) {
            Scanner inputCases = new Scanner(System.in);
            System.out.println("Please enter your desired action (buy, sell, etc.): ");
            String action = inputCases.next();

            switch(action.toLowerCase()) {
            	case "login":
            		System.err.println("Please logout before attempting to login");
            		break;
            	case "buy":
                    buy(ticketsList, username, usersList);
                    break;
                case "sell":
                	
                    break;
                case "logout":
                    logout(username, usersList);
                    break;
                case "create":
                	if(currentUser.getUserType() != "AA") {break;}
                    break;
                case "delete":
                	if(currentUser.getUserType() != "AA") {break;}
                	break;
                case "addcredit":
                	if(currentUser.getUserType() != "AA") {break;}
                    break;
                case "refund":
                    break;
                default:
                    System.err.println("Action not found.");
                    
            }
        }

    }

    public static void login() {

    }

    public static void logout(String username, ArrayList<String> usersList) throws Exception {
        if (loggedInStatus == true) {
            // Writes daily transaction list to Daily Transaction File
            FileWriter writer = new FileWriter("daily-transaction-file.txt");
            for (Transaction transaction : transactionsList) {
                writer.write(transaction.toString());
            }

            String usertype = "";
            String availableCredit = "";

            // searches for correct user
            for (int y = 0; y < usersList.size(); y++) {
                if (usersList.get(y).split(",")[0].substring(1).equals(username)) {
                    usertype = usersList.get(y).split(",")[1].substring(1);
                    availableCredit = usersList.get(y).split(",")[2].substring(1);
                    availableCredit = availableCredit.replace("]", "");
                }

            }

            // Writes end of session transaction
            writer.write(new Transaction("00", username, usertype, availableCredit).toString());
            writer.close();
            // Sets logged in status to false, system does not accept any transactions
            // other than login
            loggedInStatus = false;
            System.out.println("Thank you for being our customer.");
        } else {
            System.out.println("Please login before attempting any transaction.");
        }
    }

    public static void create() {

    }

    public static void delete() {

    }

    public static void sell() {

    }

    public static void buy(ArrayList<String> ticketsList, String username, ArrayList<String> usersList) {
        Scanner inputEvent = new Scanner(System.in);
        System.out.println("Please enter the event title: ");
        String event = inputEvent.next();
        logoutHelper(event, username, usersList);

        for (int x = 0; x < ticketsList.size(); x++) {
            if (ticketsList.get(x).split(",")[0].substring(1).equals(event)) {
                System.out.println("Please enter the number of tickets to purchase: ");
                String ticketsNumber = inputEvent.next();
                logoutHelper(ticketsNumber, username, usersList);
                Integer ticketsNum = Integer.parseInt(ticketsNumber);
                if (ticketsNum == (Integer) ticketsNum) {
                    System.out.println("Please enter the sellers username: ");
                    String sellerName = inputEvent.next();
                    logoutHelper(sellerName, username, usersList);
                    if (ticketsList.get(x).split(",")[1].substring(1).equals(sellerName)) {
                        System.out.println("Please Confirm your order (Y/N): ");
                        String confirmOrder = inputEvent.next();
                        logoutHelper(confirmOrder, username, usersList);
                        if (confirmOrder.toLowerCase().equals("y")) {
                            Double ticketCost = Double.parseDouble(ticketsList.get(x).split(",")[3].substring(1,
                                    ticketsList.get(x).split(",")[3].length() - 1));
                            System.out.println(
                                    "Ticket Cost: $" + ticketCost + ", Total Cost: $" + ticketCost * ticketsNum);
                            // need to add transaction here
                        }
                    }
                }
            }
        }
    }

    public static void refund() {

    }

    public static void addCredit() {

    }

    public static void logoutHelper(String inputString, String username, ArrayList<String> usersList) {
        if (inputString.toLowerCase().equals("logout")) {
            try {
                logout(username, usersList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

	class User {
    private String userName;
    private String userType;
    private Double userCredit;

    public String getUserName() {
        return this.userName;
    }
    public String getUserType() {
        return this.userType;
    }

    public Double getUserCredit() {
        return this.userCredit;
    }
    public void setUserCredit(Double credit) {  
        this.userCredit = credit;
    }  
}

	class Ticket {
    private String eventName;
    private String seller;
    private Integer numberOfTickets;
    private Double price;

    public String getEventName() {
        return this.eventName;
    }
    public String getSeller() {
        return this.seller;
    }
    public Integer getNumberOfTickets() {
        return this.numberOfTickets;
    }
    public Double getPrice() {
        return this.price;
    }

    public void setNumberOfTickets(int tickets) {  
        this.numberOfTickets = tickets;
    }
}
    
	class Transaction {
    private String transactionCode;
    private String userName;
    private String userType;
    private String availableCredit;
    
    public Transaction(String transactionCode, String username, String userType, String availableCredit) {
        this.transactionCode = transactionCode;
        this.userName = username;
        this.userType = userType;
        this.availableCredit = availableCredit;
    }

    //Overriding toString to make simple file output
    @Override
    public String toString(){
        return String.format(this.transactionCode + " " + this.userName + " " + this.userType + " " +this.availableCredit + "\n");
    }  
}
	

