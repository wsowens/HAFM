import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.*;
import java.lang.Integer;

public class Menu {
	static Pattern dollarFormat = Pattern.compile("[$]?\\d*[.]?\\d*");
	static Pattern dateFormat = Pattern.compile("\\d{2}[/]\\d{2}[/]\\d{4}");
	static Pattern numFormat = Pattern.compile("\\d*[.]?\\d*");

	static DecimalFormat df = new DecimalFormat("###.##");

	Scanner input;
	Profile profile;

	public Menu(Scanner input, Profile profile) {
		this.input = input;
		this.profile = profile;
	}

	public static String formatNum(double num) {
		return df.format(num);
	}

	private static boolean isValid(String input, Pattern pattern) {
		return pattern.matcher(input).matches();
	}

	public static String getValid(Scanner input, Pattern pattern) {
		String inputString = input.nextLine();
		while (!isValid(inputString, pattern)) {
			System.out.println("Error. Invalid format.");
			inputString = input.nextLine();
		}
		return inputString;
	}

	public static double getValidDouble(Scanner input) {
		String inputString = getValid(input, dollarFormat);
		inputString = inputString.replace("$", "");
		return Double.parseDouble(inputString);
	}

	public static int getValidInt(Scanner input, int min, int max) {
		int inputInt = Integer.parseInt(getValid(input, numFormat));
		while ((inputInt < min) || (inputInt > max)) {
			System.out.println("Error. Invalid input out of range.");
			inputInt = Integer.parseInt(getValid(input, numFormat));
		}
		return inputInt;
	}

	public boolean getConfirmation(String question) {
		System.out.println(question + " (Enter \'y\' if yes.)");
		return input.next().toLowerCase().equals("y");
	}

	public int mainMenu(){
		System.out.println("\nOptions: ");
		System.out.println("1. Accounts");
		System.out.print("2. Categories");
		if (!profile.hasAccount())
			System.out.print("\t[Create Accounts First]");
		System.out.print("\n3. Transactions");
		if (!profile.hasAccount())
			System.out.print("\t[Create Categories First]");
		System.out.println("\n4. Print");
		System.out.println("5. Save and Exit");
		int value = getValidInt(input, 0, 5);
		if (value == 2 && !profile.hasAccount()) {
			System.out.println("You must create an account first.");
			value = mainMenu();
		}
		else if (value == 3 && !profile.hasCategory()) {
			System.out.println("You must create a category first.");
			value = mainMenu();
		}
		return value;
	}

	public int genMenu(String fill) {
		System.out.println("\n1. Add " + fill);
		System.out.println("2. Update " + fill);
		System.out.println("3. Remove " + fill);
		System.out.println("4. Back");
		return getValidInt(input, 1, 4);
	}

	public int accountMenu() {
		System.out.println("\n1. Open account");
		System.out.print("2. Withdraw");
		if (!profile.hasAccount())
			System.out.print("\t[Create Account First]");
		System.out.print("\n3. Deposit");
		if (!profile.hasAccount())
			System.out.print("\t[Create Account First]");
		System.out.print("\n4. Transfer");
		if (!profile.hasAccount())
			System.out.print("\t[Create Account First]");
		System.out.print("\n5. Close");
		if (!profile.hasAccount())
			System.out.print("\t[Create Account First]");
		System.out.println("\n6. Back");
		int value = getValidInt(input, 1, 6);
		if (!profile.hasAccount() && value > 1 && value < 6) {
			System.out.println("Error. You must create an account first.");
			value = accountMenu();
		}
		return value;
	}

	public void accountCreate() {
		System.out.println("Name:");
		String name = input.nextLine();
		System.out.println("Amount:");
		double balance = getValidDouble(input);
		profile.addAccount(name, balance);
	}

	public Account accountSelection() {
		//handle case where there are no accounts?
		Account account = null;
		//implement number and name account access
		account = profile.getAccount(getValid(input, numFormat));
		while(account == null) {
			System.out.println("Error. Invalid account.");
			account = profile.getAccount(getValid(input, numFormat));
		}
		return account;
	}

	public void accountWithdraw() {
		System.out.println("Select the number of an account to withdraw from.");
		Account account = accountSelection();
		System.out.println(account);
		System.out.println("Enter an amount to withdraw.");
		double amount = Menu.getValidDouble(input);
		if (getConfirmation("Withdraw $" + amount + " from " + account.getName() + "?")) {
			if (account.withdraw(amount)) {
				System.out.println("Withdraw successful.");
			}
			else {
				System.out.println("Withdraw unsuccessful.");
			}
		}
		else {
			System.out.println("Withdraw cancelled.");
		}
	}

	public void accountDeposit() {
		System.out.println("Select the number of an account to deposit into");
		Account account = accountSelection();
		System.out.println(account);
		System.out.println("Enter an amount to deposit.");
		double amount = Menu.getValidDouble(input);
		if (getConfirmation( "Deposit $" + amount + " into " + account.getName() + "?")) {
			if (account.deposit(amount)) {
				System.out.println("Deposit successful.");
			}
			else {
				System.out.println("Deposit unsuccessful.");
			}
		}
		else {
			System.out.println("Deposit cancelled.");
		}
	}

	public void accountTransfer() {
		System.out.println("Select the number of an account to withdraw from.");
		Account withdrawAccount = accountSelection();
		System.out.println("Select the number of an account to deposit into.");
		Account depositAccount = accountSelection();
		System.out.println(withdrawAccount);
		System.out.println(depositAccount);
		System.out.println("Enter an amount to transfer.");
		double amount = getValidDouble(input);
		if (getConfirmation("Transfer $" + amount + " from "
			+ withdrawAccount.getName() + " into " + depositAccount.getName() + "?")) {
			if (withdrawAccount.transfer(amount, depositAccount)) {
				System.out.println("Transfer successful.");
			}
			else {
				System.out.println("Transfer unsuccessful.");
			}
		}
		else {
			System.out.println("Transfer cancelled.");
		}
	}

	public void accountClose() {
		System.out.println("Select the number of an account to delete.");
		Account account = accountSelection();
		System.out.println(account);
		if (getConfirmation("Are you sure you want to delete this account?")) {
			if (profile.removeAccount(account)) {
				System.out.println("Success.");
			}
			else {
				System.out.println("Deletion failed.");
			}
		}
		else {
			System.out.println("Deletion cancelled.");
		}
	}

	public int exitMenu() {
		System.out.println("1. Save");
		System.out.println("2. Exit and Save");
		System.out.println("3. Exit without Saving");
		System.out.println("4. Back");
		return getValidInt(input, 1, 4);
	}
}
