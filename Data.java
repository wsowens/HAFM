import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;


public class Data {
	public static String getExtension(String filename) {
		String[] split = filename.split("[.]");
		if (split.length == 1) {
			return "";
		}
		return split[split.length-1];
	}

	public static String getValidLoad(Scanner input) {
		boolean fileUnapproved = true;
		String filename = "finances.xls";
		while (fileUnapproved) {
			System.out.println("Please enter a filename to open a file. (Type \'new\' for new file.)");
			filename = input.nextLine();
			if (filename.equals("new")) {
				fileUnapproved = false;
			}
			else {
				if (!(new File(filename)).exists()) {
					System.out.println("Error, provided file does not exist.");
					continue;
				}
				//update this later when we support different delimiters
				if (!Data.test(filename, "/t")) {
					System.out.println("File cannot be read.");
				}
				else {
					fileUnapproved = false;
				}
			}
		}
		return filename;
	}

	public static String getValidFilename(Scanner input) {
		boolean fileUnapproved = true;
		String filename = "finances.xls";
		while (fileUnapproved) {
			System.out.println("Save file as:");
			fileUnapproved = false;
			filename = input.nextLine();
			if (getExtension(filename).equals("")) {
				filename += ".xls";
			}
			if ((new File(filename)).exists()) {
				System.out.println("File " + filename + " already exists. Overwrite? (Enter 'y' if yes.)");
				if (!input.nextLine().equals("y")) {
					fileUnapproved = true;
				}
			}
		}
		return filename;
	}

	public static boolean save(Profile profile, String filename, String delimiter) {
		try {
			File file = new File(filename);
			PrintWriter export = new PrintWriter(file);
			export.print(profile.getDelimited(delimiter));
			export.close();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	public static boolean print(Profile profile, String filename) {
		//do this better
		try {
			File file = new File(filename);
			PrintWriter export = new PrintWriter(file);
			export.print(profile);
			export.close();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	//new method, generates a profile automatically
	public static Profile read(String filename, String delimiter) {
		Profile loadedProfile = new Profile();
		if (filename.equals("new")) {
			return loadedProfile;
		}
		Scanner input;
		try {
			input = new Scanner(new File(filename));
		}
		catch (java.io.FileNotFoundException ex) {
			System.out.print(ex);
			return loadedProfile;
		}
		String text = input.useDelimiter("\\A").next();
		String[] lines = text.split("\n");
		int i = 0;
		while (!lines[++i].toLowerCase().equals("categories")) {
			String[] temp = lines[i].split(delimiter);
			Account account = new Account(temp[0], Double.parseDouble(temp[1]));
			loadedProfile.addAccount(account);
		}
		while (!lines[++i].toLowerCase().equals("transactions")) {
			String[] temp = lines[i].split(delimiter);
			Category category = new Category(temp[0], Double.parseDouble(temp[1]), Double.parseDouble(temp[2]), loadedProfile.getAccount(temp[4]));
			loadedProfile.addCategory(category);
		}
		while (++i < lines.length) {
			String[] temp = lines[i].split(delimiter);
			Transaction trans = new Transaction(temp[0], Double.parseDouble(temp[1]), temp[2], loadedProfile.getCategory(temp[3]), temp[4]);
			//accounting for the fact that these transactions have already been logged
			trans.unapply();
			loadedProfile.addTransaction(trans);
		}
		input.close();
		return loadedProfile;
	}

	//this is a very amateur approach, get more particular with it later
	public static boolean test(String filename, String delimiter) {
		try {
			read(filename, delimiter);
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}

	//almost certainly useless at this point
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
}
