import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("=====Welcome to the #Adulting Finance Manager v. 2.0=====\n");

		String filename = Data.getValidLoad(input);
		//automatic delimiter knowledge based on file extension, later
		Profile current = Data.read(filename, "\t");

		Menu menu = new Menu(input, current);

		System.out.println("\nWelcome! You currently have a net worth of: ");
		System.out.println("$" + Menu.formatNum(current.getNetWorth()));
		System.out.println("\nWhat would you like to do?");

		boolean mainMenuLoop = true;
		boolean genMenuLoop = true;
		boolean subMenuLoop = true;
		while (mainMenuLoop) {
			genMenuLoop = true;
			switch (menu.mainMenu()) {
			//Accounts selected
			case 1:
				while (genMenuLoop) {
					subMenuLoop = true;
					menu.printAccounts();
					switch (menu.accountMenu()) {
					//create new account selected
					case 1:
						menu.accountCreate();
						break;
					// Withdraw selected
					case 2:
						menu.accountWithdraw();
						break;
					//deposit
					case 3:
						menu.accountDeposit();
						break;
					//transfer
					case 4:
						menu.accountTransfer();
						break;
					case 5:
						menu.accountClose();
						break;
					case 6:
						genMenuLoop = false;
					}
				}
			break;

			case 2:
				while (genMenuLoop) {
					menu.printCategories();
					switch (menu.categoryMenu()) {
					//creating new category
					case 1:
						menu.categoryCreate();
						break;
					case 2:
						menu.categoryModify();
						break;
					case 3:
						menu.categoryDelete();
						break;
					case 4:
						genMenuLoop = false;
					}

				}
				break;
			case 3:
				while (genMenuLoop) {
					menu.printTransactions();
					switch (menu.transactionMenu()) {
					case 1:
						menu.transactionCreate();
						break;
					case 2:
						menu.transactionModify();
						break;
					case 3:
						menu.transactionDelete();
						break;
					case 4:
						genMenuLoop = false;
					}
				}
				break;
				//remove brackets below
			case 4:
				String printFilename = Data.getValidFilename(input);
				if (Data.print(current, printFilename)) {
					System.out.println("Data successfully printed to " + printFilename + ".");
				}
				else {
					System.out.println("Error occurred. Data not printed.");
				}
				break;
			case 5:
				while (genMenuLoop) {
					boolean isCase1 = false;
					switch(menu.exitMenu()) {
					case 1:
						isCase1 = true;
					case 2:
						filename = Data.getValidFilename(input);
						//handle extensions/delimiters later
						if (Data.save(current, filename, "\t")) {
							System.out.println("Data successfully saved as " + filename + ".");
						}
						else {
							System.out.println("Error occurred. Data not saved.");
						}
						if (isCase1) {
							break;
						}
						if (menu.getConfirmation("Are you sure you want to quit?")) {
							mainMenuLoop = false;
							genMenuLoop = false;
						}
						break;
					case 3:
						if (menu.getConfirmation("Are you sure you want to quit?")) {
							mainMenuLoop = false;
							genMenuLoop = false;
						}
					case 4:
						genMenuLoop = false;
					}
				}
				break;
			}
		}
		System.out.println("Goodbye.");
		input.close();
	}
}
