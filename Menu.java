import java.util.Scanner;
import java.util.regex.*;
import java.lang.Integer;

public class Menu {
	static Pattern dollarFormat = Pattern.compile("[$]?\\d*[.]?\\d*");
	static Pattern dateFormat = Pattern.compile("\\d{2}[/]\\d{2}[/]\\d{4}");
	static Pattern numFormat = Pattern.compile("\\d*[.]?\\d*");

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

	public static boolean getConfirmation(Scanner input, String question) {
		System.out.println(question + " (Enter \'y\' if yes.)");
		return input.next().toLowerCase().equals("y");
	}

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

	public static void main(String[] args) {
		for (String item : args) {
			System.out.print(item + ": ");
			System.out.println(dollarFormat.matcher(item).matches());
		}
	}
}
