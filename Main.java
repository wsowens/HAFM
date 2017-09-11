import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Main {
	public static DecimalFormat df = new DecimalFormat("###.##");

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("=====Welcome to the #Adulting Finance Manager v. 2.0=====\n");

		//this is done to avoid the 'current might not have been initialized' error
		Profile current = new Profile();
		boolean importIncomplete = true;
		while (importIncomplete) {
			System.out.println("Please enter a filename to open a file. (Type \'new\' for new file.)");
			String userInput = input.nextLine();
			if (userInput.equals("new")) {
				System.out.println("Creating new file.");
				importIncomplete = false;
			}
			else {
				File file = new File(userInput);
				if (!file.exists()) {
					System.out.println("Error, provided file does not exist.");
					continue;
				}
				if (Data.test(file)) {
					current = Data.readToProfile(userInput);
					importIncomplete = false;
				}
				else {
					System.out.println("File cannot be read.");
				}
			}
		}

		Menu menu = new Menu(input, current);
		System.out.println("\nWelcome! You currently have a net worth of: ");
		System.out.println("$" + Menu.formatNum(current.getNetWorth()));

		System.out.println("\nWhat would you like to do?");

		boolean mainMenuLoop = true;
		boolean genMenuLoop = true;
		boolean subMenuLoop = true;

		int index = 0;

		while (mainMenuLoop) {
			genMenuLoop = true;
			switch (menu.mainMenu()) {
			//Accounts selected
			case 1:
				while (genMenuLoop) {
					subMenuLoop = true;
					current.printAccounts();
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
					current.printCategories();
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
					current.printTransactions();
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
			}
		}
	}
}/*
			case 4:
				input.nextLine();
				String filename = "finances.txt";
				boolean fileUnapproved = true;
				while (fileUnapproved) {
					System.out.println("Print file as:");
					fileUnapproved = false;
					filename = input.nextLine();
					if ((new File(filename)).exists()) {
						System.out.println("File " + filename + " already exists. Overwrite? (Enter 'y' if yes.)");
						if (!input.nextLine().equals("y")) {
							fileUnapproved = true;
						}
					}
				}
				if (Data.print(filename)) {
					System.out.println("Data successfully printed to " + filename + ".");
				}
				else {
					System.out.println("Error occurred. Data not printed.");
				}
				break;
			case 5:
				while (genMenuLoop) {
					switch(Menu.exitMenu(input)) {
					case 1:
						input.nextLine();
						filename = "finances.txt";
						fileUnapproved = true;
						while (fileUnapproved) {
							System.out.println("Save file as:");
							fileUnapproved = false;
							filename = input.nextLine();
							if ((new File(filename)).exists()) {
								System.out.println("File " + filename + " already exists. Overwrite? (Enter 'y' if yes.)");
								if (!input.nextLine().equals("y")) {
									fileUnapproved = true;
								}
							}
						}
						if (Data.save(filename)) {
							System.out.println("Data successfully saved as " + filename + ".");
						}
						else {
							System.out.println("Error occurred. Data not saved.");
						}
						break;
					case 2:
						input.nextLine();
						filename = "finances.txt";
						fileUnapproved = true;
						while (fileUnapproved) {
							System.out.println("Save file as:");
							fileUnapproved = false;
							filename = input.nextLine();
							if ((new File(filename)).exists()) {
								System.out.println("File " + filename + " already exists. Overwrite? (Enter 'y' if yes.)");
								if (!input.nextLine().equals("y")) {
									fileUnapproved = true;
								}
							}
						}
						if (Data.save(filename)) {
							System.out.println("Data successfully saved as " + filename + ".");
						}
						else {
							System.out.println("Error occurred. Data not saved.");
						}
						System.out.println("Are you sure you want to quit? (Enter 'y' if yes.)");
						if (input.next().toLowerCase().equals("y")) {
							mainMenuLoop = false;
							genMenuLoop = false;
						}
						break;
					case 3:
						System.out.println("Are you sure you want to quit? (Enter 'y' if yes.)");
						if (input.next().toLowerCase().equals("y")) {
							mainMenuLoop = false;
							genMenuLoop = false;
						}
					case 4:
						genMenuLoop = false;
					}

				}
				break;
			default:
				System.out.println("Error: invalid input");
			}
		}
		System.out.println("Goodbye.");
		input.close();

	}
}*/
