import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;


public class Data {

	public static boolean save(String filename) {
		try {
			File file = new File(filename);
			PrintWriter export = new PrintWriter(file);
			for (Account account : Account.accountList) {
				export.print(account.name);
				export.print(";");
				export.print(account.balance);
				export.print(";");
				export.print("\n");
			}
			export.print("&&&");
			for (Category category : Category.categoryList) {
				export.print(category.getName());
				export.print(";");
				export.print(category.getAmount());
				export.print(";");
				export.print(category.getRemaining());
				export.print(";");
				export.print(category.account.name);
				export.print(";");
				export.print("\n");
			}
			export.print("&&&");
			for (Transaction transaction : Transaction.transactionList) {
				export.print(transaction.name);
				export.print(";");
				export.print(transaction.getAmount());
				export.print(";");
				export.print(transaction.date);
				export.print(";");
				export.print(transaction.getCategory().getName());
				export.print(";");
				export.print(transaction.notes);
				export.print(";");
				export.print("\n");
			}
			export.print("&&&");
			export.close();
			return true;
		}

		catch (Exception ex) {
			return false;
		}
	}

	public static boolean print(String filename) {
		DecimalFormat df = new DecimalFormat("###.##");
		try {
			File file = new File(filename);
			PrintWriter export = new PrintWriter(file);
			export.println("Accounts:");
			int count = 0;
			for (Account account : Account.accountList) {
				count++;
				export.printf("(" + count + ") %-16s | $%-1s", account.name, account.balance);
				export.print("\n");
			}
			export.printf("    %-16s | $%-1s", "Total", df.format(Account.getTotal()));
			export.print("\n");

			export.println("Categories:");
			count = 0;
			for (Category category :Category.categoryList) {
				count++;
				export.printf("(" + count +") %-16s | $%-8s | $%-8s | $%-8s | %-1s", category.getName(), df.format(category.getAmount()), df.format(category.remaining), df.format(category.getAmount() - category.remaining), category.account.name);
				export.print("\n");
			}
			export.printf("    %-16s | $%-8s | $%-8s | $%-8s", "Total" , df.format(Category.getAmountTotal()), df.format(Category.getRemainingTotal()), df.format(Category.getAmountTotal() - Category.getRemainingTotal()));
			export.print("\n");

			export.println("Transactions:");
			count = 0;
			for (Transaction transaction : Transaction.transactionList) {
				count++;
				export.printf("(" + count + ") %-16s | $%-8s | %-7s | %-15s | %-1s" ,  transaction.name , df.format(transaction.getAmount()), transaction.date, transaction.getCategory().getName(), transaction.notes);
				export.print("\n");
			}
			export.close();
			return true;
		}

		catch (Exception ex) {
			return false;
		}
	}


	public static boolean read(String filename) {
		try {
			Scanner input = new Scanner(new File(filename));
			String text = input.useDelimiter("\\A").next();

			String[] chunks = text.split("&&&");
			String[] accounts = chunks[0].split("\n");
			String[] categories = chunks[1].split("\n");
			String[] transactions = chunks[2].split("\n");

			for (String line : accounts) {
				String[] temp = line.split(";");
				new Account(temp[0], Double.parseDouble(temp[1]));
			}

			for (String line : categories) {
				String[] temp = line.split(";");
				new Category(temp[0], Double.parseDouble(temp[1]), Account.accountMap.get(temp[2]));
			}

			for (String line : transactions) {
				String[] temp = line.split(";");
				Transaction trans = new Transaction(temp[0], Double.parseDouble(temp[1]), temp[2], Category.categoryMap.get(temp[3]), temp[4]);
				//accounting for the fact that these transactions have already been logged
				trans.getCategory().account.balance += trans.getAmount();
			}

			input.close();
			return true;
		}

		catch (Exception ex){
			System.out.println("Error. File cannot be read. Data may be corrupted.");
			return false;
		}
	}

	//new method, generates a profile automatically
	public static Profile readToProfile(String filename) {
		Profile loadedProfile = new Profile();
		Scanner input;
		try {
			input = new Scanner(new File(filename));
		}
		catch (java.io.FileNotFoundException ex) {
			System.out.print(ex);
			return loadedProfile;
		}
		String text = input.useDelimiter("\\A").next();

		String[] chunks = text.split("&&&");
		String[] accounts = chunks[0].split("\n");
		String[] categories = chunks[1].split("\n");
		String[] transactions = chunks[2].split("\n");

		for (String line : accounts) {
			String[] temp = line.split(";");
			Account account = new Account(temp[0], Double.parseDouble(temp[1]));
			loadedProfile.addAccount(account);
		}

		for (String line : categories) {
			String[] temp = line.split(";");
			Category category = new Category(temp[0], Double.parseDouble(temp[1]), Double.parseDouble(temp[2]), Account.accountMap.get(temp[3]));
			loadedProfile.addCategory(category);
		}
		loadedProfile.printAccounts();
		loadedProfile.printCategories();
		System.out.println("==============");
		for (String line : transactions) {
			String[] temp = line.split(";");
			Transaction trans = new Transaction(temp[0], Double.parseDouble(temp[1]), temp[2], Category.categoryMap.get(temp[3]), temp[4]);
			//accounting for the fact that these transactions have already been logged
			trans.unapply();
			loadedProfile.addTransaction(trans);
		}

		input.close();
		return loadedProfile;
	}

	//this is a very generic approach, get more particular with it later
	public static boolean test(File file) {
		Profile loadedProfile = new Profile();
		boolean validFile = true;
		Scanner input;
		try {
				input = new Scanner(file);
		}
		catch (java.io.FileNotFoundException ex) {
			return false;
		}
		try {
			String text = input.useDelimiter("\\A").next();
			String[] chunks = text.split("&&&");
			String[] accounts = chunks[0].split("\n");
			String[] categories = chunks[1].split("\n");
			String[] transactions = chunks[2].split("\n");

			for (String line : accounts) {
				String[] temp = line.split(";");
				Account account = new Account(temp[0], Double.parseDouble(temp[1]));
				loadedProfile.addAccount(account);
			}

			for (String line : categories) {
				String[] temp = line.split(";");
				Category category = new Category(temp[0], Double.parseDouble(temp[1]), Double.parseDouble(temp[2]), Account.accountMap.get(temp[3]));
				loadedProfile.addCategory(category);
			}
			for (String line : transactions) {
				String[] temp = line.split(";");
				Transaction trans = new Transaction(temp[0], Double.parseDouble(temp[1]), temp[2], Category.categoryMap.get(temp[3]), temp[4]);
				//accounting for the fact that these transactions have already been logged
				trans.unapply();
				loadedProfile.addTransaction(trans);
			}
		}
		catch (Exception ex) {
			validFile = false;
		}
		finally {
			input.close();
		}
		return validFile;
	}

	static void arrayPrint(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print("(" + i + ") " + array[i] + "|");
		}
		System.out.print("\n");
	}

	static void wipe() {
		Transaction[] temp = Transaction.transactionList.toArray(new Transaction[Transaction.transactionList.size()]);
		for (Transaction transaction : temp) {
			transaction.deleteTransaction();
		}
		Category[] temp1 = Category.categoryList.toArray(new Category[Category.categoryList.size()]);
		for (Category category : temp1) {
			category.deleteCategory();
		}
		Category.clearNull();
		Account[] temp2 = Account.accountList.toArray(new Account[Account.accountList.size()]);
		for (Account account : temp2) {
			account.deleteAccount();
		}
		Account.clearNull();
	}

	public static void main(String[] args) {
		System.out.println("Loading finances...");
		Profile profile = readToProfile("C:/Users/Will/Programming/test-finances.txt");
		profile.printAccounts();
        profile.printCategories();
        profile.printTransactions();
		for (String item : args) {
			System.out.println("Testing " + item + "\t" + test(new File(item)));
		}
	}
}
