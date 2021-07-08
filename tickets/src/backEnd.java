
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class backEnd {
	//ArrayLists of ArrayLists for transactions, users, tickets
	static List<List<String>> transactions = new ArrayList<List<String>>();
	static List<List<String>> users = new ArrayList<List<String>>();
	static List<List<String>> tickets = new ArrayList<List<String>>();
	
	public static void main(String[] args) {
		//Calls methods to add transactions/users/tickets to ArrayLists above
		getTransactions();
		getUsers();
		getTickets();
		//test to see contents of any arraylist
		//System.out.println(Arrays.deepToString(tickets.toArray()));
		
		//for loop to go through all transactions in transaction file
		for(int i=0; i<transactions.size();i++) {
			//var for current transaction 
			List currTrans = transactions.get(i);
			
			//The follwoing if statement checks the two digit transaction
			//code to run the correct code for each transaction
			//Option 1: create user. Adds user to user list.
			if(currTrans.get(0).equals("01")) {
				String name = (String) currTrans.get(1);
				String type = (String) currTrans.get(2);
				String credit = (String) currTrans.get(3);
				
				//ArrayList for a single user
				//used to add to Arralist of Users
				ArrayList<String> singleUser = new ArrayList<String>();
				singleUser.add(name);
				singleUser.add(type);
				singleUser.add(credit);
				users.add(singleUser);
			}
			
			//Option 2: delete users. Removes a user from the Users list
			else if(currTrans.get(0).equals("02")) {
				String name = (String) currTrans.get(1);
				//searches users list for a username matching the one provided
				//if found removes user and breaks loop.
				for(int j =0; j<users.size();j++) {
					if(users.get(i).get(0).equals(name)){
						users.remove(i);
						break;
					}
				}
			}
			
			//Option 3: Sell
			else if(currTrans.get(0).equals("03")) {
				//get names of event and seller, along with number of tickets sold
				//and the price of each ticket
				String eventName = (String) currTrans.get(1);
				String sellerName = (String) currTrans.get(2);
				String numTickets = (String) currTrans.get(3);
				String priceTickets = (String) currTrans.get(4);
				
				//ArrayList for a single ticket
				//used to add to Arralist of Tickets
				ArrayList<String> singleTicket = new ArrayList<String>();
				singleTicket.add(eventName);
				singleTicket.add(sellerName);
				singleTicket.add(numTickets);
				singleTicket.add(priceTickets);
				tickets.add(singleTicket);	
			}
			
			//Option 4: Buy. Subtracts/adds credit to the buyer/seller, subtracts tickets from seller 
			else if(currTrans.get(0).equals("04")) {
				String eventName = (String) currTrans.get(1);
				//iterate through tickets to find ticket info for event
				int j;
				for(j=0; j<tickets.size();j++) {
					if(tickets.get(j).get(0).equals(eventName)){
						break;
					}
				}
				int ticketsBought = (int) currTrans.get(3);
				int totalTickets = Integer.parseInt(tickets.get(j).get(2));
				int moneySpent = Integer.parseInt(tickets.get(j).get(3)) * Integer.parseInt(tickets.get(j).get(4));
				//sets the amount of tickets left after the sale		
				String newAmountTickets = String.valueOf(totalTickets-ticketsBought);

				if((Integer) newAmountTickets < 0) {
					errorLog("Buy transaction not valid, not enough tickets");
				}
				tickets.get(j).set(2, newAmountTickets);
				//Searches for buyer by searching for the next logout transaction 		
				String buyerName = null;
				boolean lookingForBuyer = true;
				int n = i;
				
				while(lookingForBuyer) {
					if(transactions.get(n).get(0).equals("00")) {
						buyerName = transactions.get(n).get(1);
						lookingForBuyer = false;	
						break;
					}
					n++;
				}
				//Subtracts credits from buyer
				for(j=0; j<users.size();j++) {
					if(users.get(j).get(0).equals(buyerName)) {
						int newBuyerCred = Integer.parseInt(users.get(j).get(2)) -moneySpent;
						users.get(j).set(2,String.valueOf(newBuyerCred) );
						break;
					}
				}
				//adds credits to seller
				for(j=0; j<users.size();j++) {
					if(users.get(j).get(0).equals((String) currTrans.get(2))) {
						int newSellerCred = Integer.parseInt(users.get(j).get(2)) + moneySpent;
						users.get(j).set(2, (String.valueOf(newSellerCred)));
						break;
					}
				}
			}
			//Options 5: refund
			else if(currTrans.get(0).equals("05")) {
				String buyerName = (String) currTrans.get(1);
				String sellerName = (String) currTrans.get(2);
				int refund = (int) currTrans.get(3);
				
				for(int j=0; j<users.size();j++) {
					if(users.get(j).get(0).equals(buyerName)) {
						int newBuyerCred = Integer.parseInt(users.get(j).get(2)) + refund;
						users.get(j).set(2, (String.valueOf(newBuyerCred)));
						break;
					}
				}
				for(int j=0; j<users.size();j++) {
					if(users.get(j).get(0).equals(sellerName)) {
						int newSellerCred = Integer.parseInt(users.get(j).get(2)) - refund;
						users.get(j).set(2, (String.valueOf(newSellerCred)));
						break;
					}
				}
			}
			//Options 6: add credit. Gives credit to specified user
			else if(currTrans.get(0).equals("06")) {
				String accName = (String) currTrans.get(0);
				int addCredit = (int) currTrans.get(3);
				//runs through users file to find seller and add credits
				for(int j=0; j<users.size();j++) {
					if(users.get(j).get(0).equals(accName)) {
						int newBuyerCred = Integer.parseInt(users.get(j).get(2)) + addCredit;
						users.get(j).set(2, (String.valueOf(newBuyerCred)));
						break;
					}
				}
			}
		}
	}
	
	//parses ticket file to find tickets and add them too arraylist "tickets"
	static void getTickets(){
		File ticketFile = new File("ticketsfile.txt");
		
		Scanner sc = null;
		try {
			sc = new Scanner(ticketFile);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			errorLog("Tickets file not valid");
		}
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] txtVars = line.split(" ");

			//ArrayList for a single ticket
			//used to add to Arralist of tickets
			ArrayList<String> singleTicket = new ArrayList<String>();
			singleTicket.add(txtVars[0]);
			singleTicket.add(txtVars[1]);
			singleTicket.add(txtVars[2]);
			singleTicket.add(txtVars[3]);
			tickets.add(singleTicket);
		}
	}
	
	//parses user file to find tickets and add them too arraylist "users"
	static void getUsers(){
		File usrFile = new File("userfile.txt");
		
		Scanner sc = null;
		try {
			sc = new Scanner(usrFile);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			errorLog("Users file not valid");

		}
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] txtVars = line.split(" ");
			
			//ArrayList for a single user
			//used to add to Arralist of Users
			ArrayList<String> singleUser = new ArrayList<String>();
			singleUser.add(txtVars[0]);
			singleUser.add(txtVars[1]);
			singleUser.add(txtVars[2]);
			users.add(singleUser);
		}
	}
	
	static void getTransactions(){
		File dailyTrans = new File("daily-transaction-file.txt");
		
		Scanner sc = null;
		try {
			sc = new Scanner(dailyTrans);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			errorLog("Transaction file not valid");
		}
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] txtVars = line.split(" ");
			//ArrayList for a single transaction
			//used to add to Arralist of transactions
			ArrayList<String> singleTrans = new ArrayList<String>();
			singleTrans.add(txtVars[0]);
			singleTrans.add(txtVars[1]);
			singleTrans.add(txtVars[2]);
			singleTrans.add(txtVars[3]);
			try {
				singleTrans.add(txtVars[4]);
			}
			catch(IndexOutOfBoundsException e) {
				singleTrans.add("");
				errorLog("Transaction not valid");
			}
			transactions.add(singleTrans);
		}
	}
	
	//creates new current "accounts file" and new "tickets available" file
	static void mergefile() {
		
	}

	static void errorLog(String type) {
		System.err.println("Error: " + type + ". Please review input files.");
	}
	
	
}
