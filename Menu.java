import java.util.Scanner;
public class Menu {
	
	public static int mainMenu(Scanner input){
		System.out.println("\nOptions: ");
		System.out.println("1. Accounts");
		System.out.println("2. Categories");
		System.out.println("3. Transactions");
		System.out.println("4. Print");
		System.out.println("5. Save and Exit");
		return input.nextInt();
	}
	
	public static int genMenu(Scanner input, String fill) {
		System.out.println("\n1. Add " + fill);
		System.out.println("2. Update " + fill);
		System.out.println("3. Remove " + fill);
		System.out.println("4. Back");
		return input.nextInt();
	}
	
	public static int accountMenu(Scanner input) {
		System.out.println("\n1. Open account");
		System.out.println("2. Withdraw");
		System.out.println("3. Deposit");
		System.out.println("4. Transfer");
		System.out.println("5. Close");
		System.out.println("6. Back");
		return input.nextInt();
	}
	
	public static int exitMenu(Scanner input) {
		System.out.println("1. Save");
		System.out.println("2. Exit and Save");
		System.out.println("3. Exit without Saving");
		System.out.println("4. Back");
		return input.nextInt();
	}

}
