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
				//delete the braces below this;
			}
		}
	} /*
				break;
			case 2:
				while (genMenuLoop) {
					subMenuLoop = true;
					Category.printAll();
					switch (menu.genMenu(input, "category")) {
					//creating new category
					case 1:
						input.nextLine();
						System.out.println("Name:");
						String name = input.nextLine();
						System.out.println("Amount:");
						double amount = Menu.getValidDouble(input);
						System.out.println("Account:");
						//make a safer way of accessing this
						Account account = null;
						boolean invalidAccount = true;
						while (invalidAccount) {
							try {
								invalidAccount = false;
								account = Account.accountMap.get(input.nextLine());
								if (account == null) {
									System.out.println("Error. Account does not exist.");
									invalidAccount = true;
								}
							}
							catch (Exception ex) {
								System.out.println("Error. Account does not exist.");
								invalidAccount = true;
							}
						}
						try {
							new Category(name, amount, account);
						}
						catch (Exception ex) {
							System.out.println("User input error. Category not created.");
						}
						break;
					case 2:
						input.nextLine();
						System.out.println("Select the number of a category to modify it.");
						//potential infinite loop if categoryList.size() == 0, fix later
						index = Menu.getValidInt(input, 1, Category.categoryList.size()) - 1;
						Category newCategory = new Category(Category.categoryList.get(index));
						while (subMenuLoop) {
							System.out.println(newCategory);
							System.out.println("\nSelect a data field to modify:");
							System.out.println("1. Name");
							System.out.println("2. Amount");
							System.out.println("3. Account");
							System.out.println("4. Cancel changes and exit");
							System.out.println("5. Save changes and exit");
							switch (input.nextInt()) {
							case 1:
								boolean assignName = true;
								while (assignName)
									assignName = false;
									input.nextLine();
									System.out.println("Enter new name: ");
									String newName = input.nextLine();
									for (Category category : Category.categoryList) {
										//add an && statement ensuring that category is not the same as the original being modified (use index)
										if (category.getName().equals(newName)) {
											assignName = true;
											System.out.println("Error: duplicate category name already exists.");
										}
									}
									newCategory.setName(newName);
								break;
							case 2:
								input.nextLine();
								System.out.println("Enter new amount: ");
								newCategory.setAmount(Menu.getValidDouble(input));
								break;
							case 3:
								input.nextLine();
								System.out.println("Enter new account: ");
								Account newAccount = null;
								invalidAccount = true;
								while (invalidAccount) {
									try {
										invalidAccount = false;
										newAccount = Account.accountMap.get(input.nextLine());
										if (newAccount == null) {
											System.out.println("Error. Account does not exist.");
											invalidAccount = true;
										}
									}
									catch (Exception ex) {
										System.out.println("Error. Account does not exist.");
										invalidAccount = true;
									}
								}
								newCategory.account = newAccount;
								break;
							case 4:
								newCategory.deleteCategory();
								subMenuLoop = false;
								break;
							case 5:
								try {
									Category.categoryList.remove(newCategory);
									Category.categoryList.get(index).updateCategory(newCategory);
									Category.categoryMap.put(newCategory.getName(), newCategory);
									Category.categoryList.add(index, newCategory);
								}
								catch (Exception ex){
									newCategory.deleteCategory();
									System.out.println("User input error. Category not updated.");
								}
								subMenuLoop = false;

							}
						}
						break;
					case 3:
						input.nextLine();
						System.out.println("Select the number of a category to delete it.");
						//potential infinite loop if categoryList.size() == 0, fix later
						index = Menu.getValidInt(input, 1, Category.categoryList.size()) - 1;
						System.out.println(Category.categoryList.get(index));
						if (Menu.getConfirmation(input, "Are you sure you want to delete this category?")) {
							Category.categoryList.get(index).deleteCategory();
							System.out.println("Category deleted.");
						}
						else {
							System.out.println("Deletion cancelled.");
						}
						break;
					case 4:
						genMenuLoop = false;
					}

				}
				break;
			case 3:
				while (genMenuLoop) {
					subMenuLoop = true;
					Transaction.printAll();
					switch (Menu.genMenu(input, "transaction")) {
					case 1:
						input.nextLine();
						System.out.println("Name:");
						String name = input.nextLine();
						System.out.println("Amount:");
						double amount = Menu.getValidDouble(input);
						System.out.println("Date:");
						String date = Menu.getValid(input, Menu.dateFormat);
						System.out.println("Category:");
						//make a safer way of doing this
						Category category = null;
						boolean invalidCategory = true;
						while (invalidCategory) {
							try {
								invalidCategory = false;
								category = Category.categoryMap.get(input.nextLine());
								if (category == null) {
									System.out.println("Error. Category does not exist.");
									invalidCategory = true;
								}
							}
							catch (Exception ex) {
								System.out.println("Error. Category does not exist.");
								invalidCategory = true;
							}
						}
						System.out.println("Notes:");
						String notes = input.nextLine();
						try {
							new Transaction(name, amount, date, category, notes);
						}
						catch (Exception ex) {
							System.out.println("User input error. Transaction not created.");
						}
						break;
					case 2:
						input.nextLine();
						System.out.println("Select the number of a transaction to modify it.");
						index = Menu.getValidInt(input, 1, Transaction.transactionList.size()) -1;
						Transaction newTransaction = new Transaction(Transaction.transactionList.get(index));
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
							switch (input.nextInt()) {
							case 1:
								input.nextLine();
								System.out.println("Enter new name: ");
								newTransaction.name = input.nextLine();
								break;
							case 2:
								input.nextLine();
								System.out.println("Enter new amount: ");
								newTransaction.modifyAmount(Menu.getValidDouble(input));
								break;
							case 3:
								input.nextLine();
								System.out.println("Enter new date: ");
								newTransaction.date = Menu.getValid(input, Menu.dateFormat);
								break;
							case 4:
								input.nextLine();
								System.out.println("Enter new category: ");
								//make a safer way of doing this
								Category newCategory = null;
								invalidCategory = true;
								while (invalidCategory) {
									try {
										invalidCategory = false;
										newCategory = Category.categoryMap.get(input.nextLine());
										if (newCategory == null) {
											System.out.println("Error. Category does not exist.");
											invalidCategory = true;
										}
									}
									catch (Exception ex) {
										System.out.println("Error. Category does not exist.");
										invalidCategory = true;
									}
								}
								newTransaction.modifyCategory(newCategory);
								break;
							case 5:
								input.nextLine();
								System.out.println("Enter new notes: ");
								newTransaction.notes = input.nextLine();
								break;
							case 6:
								newTransaction.deleteTransaction();
								subMenuLoop = false;
								break;
							case 7:
								try {
									//this is clunky, fix it
									Transaction.transactionList.remove(newTransaction);
									Transaction.transactionList.get(index).deleteTransaction();
									Transaction.transactionList.add(index, newTransaction);
								}
								catch (Exception ex) {
									newTransaction.deleteTransaction();
									System.out.println("User input error. Transaction not updated.");
								}
								subMenuLoop = false;

							}
						}
						break;
					case 3:
						input.nextLine();
						System.out.println("Select the number of a transaction to delete it.");
						//potential infinite loop, yada yada
						index =  Menu.getValidInt(input, 1, Transaction.transactionList.size()) -1;
						System.out.println(Transaction.transactionList.get(index));
						if (Menu.getConfirmation(input, "Are you sure you want to delete this transaction?")) {
							Transaction.transactionList.get(index).deleteTransaction();
							System.out.println("Transaction deleted.");
						}
						else {
							System.out.println("Deletion cancelled.");
						}
						break;
					case 4:
						genMenuLoop = false;
					}

				}
				break;
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

	}*/
}
