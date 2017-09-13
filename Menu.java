import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.*;
import java.lang.Integer;

public class Menu {
	public static Pattern dollarFormat = Pattern.compile("[$]?\\d*[.]?\\d*");
	public static Pattern dateFormat = Pattern.compile("\\d{2}[/]\\d{2}[/]\\d{4}");
	public static Pattern numFormat = Pattern.compile("\\d*[.]?\\d*");
	public static Pattern intFormat = Pattern.compile("\\d{1,}");

	private static DecimalFormat df = new DecimalFormat("###.##");

	private Scanner input;
	private Profile profile;

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

	//possibly superfluous?
	public static double getValidDouble(Scanner input) {
		String inputString = getValid(input, dollarFormat);
		inputString = inputString.replace("$", "");
		return Double.parseDouble(inputString);
	}

	public static int getValidInt(Scanner input, int min, int max) {
		int inputInt = Integer.parseInt(getValid(input, intFormat));
		while ((inputInt < min) || (inputInt > max)) {
			System.out.println("Error. Invalid input out of range.");
			inputInt = Integer.parseInt(getValid(input, numFormat));
		}
		return inputInt;
	}

	public boolean getConfirmation(String question) {
		System.out.println(question + " (Enter \'y\' if yes.)");
		return input.nextLine().toLowerCase().equals("y");
	}

	public void printAccounts() {
		System.out.println(profile.getAccountString());
	}

	public void printCategories() {
		System.out.println(profile.getCategoryString());
	}

	public void printTransactions() {
		System.out.println(profile.getTransactionString());
	}

	public int mainMenu(){
		System.out.println("\nOptions: ");
		System.out.println("1. Accounts");
		System.out.print("2. Categories");
		if (!profile.hasAccount())
			System.out.print("\t[Create Accounts First]");
		System.out.print("\n3. Transactions");
		if (!profile.hasCategory())
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
		//prevent duplicate names, (use while loop, "while (profile.getAccount(name) == null)")
		//make names non-numeric, check with validNum;
		String name = input.nextLine();
		System.out.println("Amount:");
		double balance = getValidDouble(input);
		profile.addAccount(name, balance);
	}

	public Account accountSelection() {
		//handle case where there are no accounts?
		Account account = null;
		while (account == null) {
			String userInput = input.nextLine();
			if (isValid(userInput, intFormat)) {
				account = profile.getAccount(Integer.parseInt(userInput) - 1);
			}
			else {
				account = profile.getAccount(userInput);
			}
			if (account == null) {
				System.out.println("Error. \'" + userInput + "\' is not a valid account.");
			}
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
		System.out.println("Withdrawing from:\t" + withdrawAccount);
		System.out.println("Depositing into:\t" + depositAccount);
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

	public int categoryMenu() {
		System.out.println("\n1. Add category");
		System.out.print("2. Update category");
		if (!profile.hasCategory())
			System.out.print("\t[Create Category First]");
		System.out.print("\n3. Remove category");
		if (!profile.hasCategory())
			System.out.print("\t[Create Category First]");
		System.out.println("\n4. Back");
		int value = getValidInt(input, 1, 4);
		if (!profile.hasCategory() && value > 1 && value < 4) {
			System.out.println("Error. You must create a category first.");
			value = categoryMenu();
		}
		return value;
	}

	public void categoryCreate() {
		System.out.println("Name:");
		//prevent duplicate names, (use while loop, "while (profile.getCategory(name) == null)")
		//make names non-numeric, check with validNum;
		String name = input.nextLine();
		System.out.println("Amount:");
		double amount = getValidDouble(input);
		System.out.println("Account:");
		Account account = accountSelection();
		profile.addCategory(name, amount, account);
	}

	public Category categorySelection() {
		//handle case where there are no accounts?
		Category category = null;
		while (category == null) {
			String userInput = input.nextLine();
			if (isValid(userInput, numFormat)) {
				category = profile.getCategory(Integer.parseInt(userInput) - 1);
			}
			else {
				category = profile.getCategory(userInput);
			}
			if (category == null) {
				System.out.println("Error. \'" + userInput + "\' is not a valid category.");
			}
		}
		return category;
	}

	//consider breaking up this function?
	public void categoryModify() {
		System.out.println("Select the number of a category to modify it.");
		Category oldCategory = categorySelection();
		Category newCategory = new Category(oldCategory);
		boolean subMenuLoop = true;
		while (subMenuLoop) {
			System.out.println(newCategory);
			System.out.println("\nSelect a data field to modify:");
			System.out.println("1. Name");
			System.out.println("2. Amount");
			System.out.println("3. Account");
			System.out.println("4. Cancel changes and exit");
			System.out.println("5. Save changes and exit");
			switch (getValidInt(input, 1, 5)) {
			case 1:
				System.out.println("Enter new name: ");
				boolean invalidName = true;
				String name = "";
				while (invalidName) {
					name = input.nextLine();
					if (isValid(name, numFormat)) {
						System.out.println("Error, names must be non-numeric.");
						continue;
					}
					else if (profile.isCategory(name)) {
						System.out.println("Error, name cannot be an existing name.");
					}
					else {
						invalidName = false;
					}
				}
				newCategory.setName(name);
				break;
			case 2:
				System.out.println("Enter new amount: ");
				newCategory.setAmount(getValidDouble(input));
				break;
			case 3:
				System.out.println("Enter new account: ");
				Account account = accountSelection();
				newCategory.setAccount(account);
				break;
			case 4:
				subMenuLoop = false;
				break;
			case 5:
				if (profile.updateCategory(oldCategory, newCategory)) {
					System.out.println("Update succeeded.");
				}
				else {
					System.out.println("Update failed.");
				}
				subMenuLoop = false;
			}
		}
	}

	public void categoryDelete() {
		System.out.println("Select the number of a category to delete it.");
		Category category = categorySelection();
		System.out.println(category);
		if (getConfirmation("Are you sure you want to delete this category?")) {
			if (profile.removeCategory(category)) {
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

	public int transactionMenu() {
		System.out.println("\n1. Add transaction");
		System.out.print("2. Update transaction");
		if (!profile.hasTransaction())
			System.out.print("\t[Create Transaction First]");
		System.out.print("\n3. Remove transaction");
		if (!profile.hasTransaction())
			System.out.print("\t[Create Transaction First]");
		System.out.println("\n4. Back");
		int value = getValidInt(input, 1, 4);
		if (!profile.hasTransaction() && value > 1 && value < 4) {
			System.out.println("Error. You must create a transaction first.");
			value = transactionMenu();
		}
		return value;
	}

	public void transactionCreate() {
		//prevent duplicate names, (use while loop, "while (profile.getCategory(name) == null)")
		//make names non-numeric, check with validNum;
		System.out.println("Name:");
		String name = input.nextLine();
		System.out.println("Amount:");
		double amount = getValidDouble(input);
		System.out.println("Date:");
		String date = getValid(input, Menu.dateFormat);
		System.out.println("Category:");
		Category category = categorySelection();
		System.out.println("Notes:");
		String notes = input.nextLine();
		profile.addTransaction(name, amount, date, category, notes);
	}

	public Transaction transactionSelection() {
		//handle case of no transactions?
		Transaction transaction = null;
		while (transaction == null) {
			String userInput = input.nextLine();
			if (isValid(userInput, intFormat)) {
				transaction = profile.getTransaction(Integer.parseInt(userInput) - 1);
			}
			else {
				transaction = profile.getTransaction(userInput);
			}
			if (transaction == null) {
				System.out.println("Error. \'" + userInput + "\' is not a valid transaction.");
			}
		}
		return transaction;
	}

	public void transactionModify() {
		System.out.println("Select the number of a transaction to modify it.");
		Transaction oldTransaction = transactionSelection();
		Transaction newTransaction = new Transaction(oldTransaction);
		boolean subMenuLoop = true;
		while (subMenuLoop) {
			System.out.println(newTransaction);
			System.out.println("\nSelect a data field to modify:");
			System.out.println("1. Name");
			System.out.println("2. Amount");
			System.out.println("3. Date");
			System.out.println("4. Category");
			System.out.println("5. Notes");
			System.out.println("6. Cancel changes and exit");
			System.out.println("7. Save changes and exit");
			switch (getValidInt(input, 1, 7)) {
			case 1:
				System.out.println("Enter new name: ");
				String name = "";
				boolean invalidName = true;
				while (invalidName) {
					name = input.nextLine();
					if (isValid(name, numFormat)) {
						System.out.println("Error, names must be non-numeric.");
						continue;
					}
					else if (profile.isTransaction(name)) {
						System.out.println("Error, name cannot be an existing name.");
					}
					else {
						invalidName = false;
					}
				}
				newTransaction.setName(name);
				break;
			case 2:
				System.out.println("Enter new amount: ");
				newTransaction.setAmount(getValidDouble(input));
				break;
			case 3:
				input.nextLine();
				System.out.println("Enter new date: ");
				newTransaction.setDate(Menu.getValid(input, Menu.dateFormat));
				break;
			case 4:
				System.out.println("Enter new category: ");
				Category newCategory = categorySelection();
				newTransaction.setCategory(newCategory);
				break;
			case 5:
				System.out.println("Enter new notes: ");
				String notes = input.nextLine();
				newTransaction.setNotes(notes);
				break;
			case 6:
				subMenuLoop = false;
				break;
			case 7:
				profile.removeTransaction(oldTransaction);
				profile.addTransaction(newTransaction);
				subMenuLoop = false;
			}
		}
	}
	public void transactionDelete() {
		System.out.println("Select the number of a transaction to delete it.");
		Transaction transaction = transactionSelection();
		System.out.println(transaction);
		if (getConfirmation("Are you sure you want to delete this transaction?")) {
			if (profile.removeTransaction(transaction)) {
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
