import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Main {
	public static DecimalFormat df = new DecimalFormat("###.##");
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("=====Welcome to the #Adulting Finance Manager v. 1.1=====\n");
	
		boolean importIncomplete = true;
		while (importIncomplete) {
			System.out.println("Please enter a filename to open a file. (Type [new] for new file.)");
			String userInput = input.nextLine();
			if (userInput.equals("[new]")) {
				System.out.println("Creating new file.");
				break;
			}
			try {
				File importFile = new File(userInput);
				Scanner fileInput = new Scanner(importFile);
				fileInput.close();
				importIncomplete = false;
			}
			catch (FileNotFoundException ex) {
				System.out.println("Error. File cannot be found. Please try again.");
				importIncomplete = true;
				continue;
			}
			
			if (Data.read(userInput)) {
				System.out.println("Data read successful.");
				importIncomplete = false;
			}
			else {
				Data.wipe();
				importIncomplete = true;
			}
		}
		
		
		System.out.println("\nWelcome! You currently have a net worth of: ");
		System.out.println("$" + df.format(Account.getTotal()));
		
		
		
		
		
		System.out.println("\nWhat would you like to do?");
		
		boolean mainMenuLoop = true;
		boolean genMenuLoop = true;
		boolean subMenuLoop = true;
		
		int index = 0;
		
		while (mainMenuLoop) {
			genMenuLoop = true;
			switch (Menu.mainMenu(input)) {
			case 1:  
				while (genMenuLoop) {
					subMenuLoop = true;
					Account.printAll();
					switch (Menu.accountMenu(input)) {
					case 1:
						input.nextLine();
						System.out.println("Name:");
						String name = input.nextLine();
						System.out.println("Amount:");
						double balance = input.nextDouble();
						input.nextLine();
						try {
							new Account(name, balance);
						}
						catch (Exception ex) {
							System.out.println("User input error. Account not created.");
						}
						break;
					case 2: 
						input.nextLine();
						System.out.println("Select the number of an account to withdraw from.");
						index = input.nextInt() - 1;
						System.out.println((Account.accountList.get(index)));
						System.out.println("Enter an amount to withdraw.");
						double amount = input.nextDouble();
						System.out.println("Withdraw $" + amount + " from " + Account.accountList.get(index).name + "? (Enter 'y' if yes.)");
						input.nextLine();
						if (input.nextLine().equals("y")) {
							if (Account.accountList.get(index).withdraw(amount)) {
								System.out.println("Withdraw successful.");
							}
							else {
								System.out.println("Withdraw unsuccessful.");
							}
						}
						else {
							System.out.println("Withdraw cancelled.");
						}
						break;
					case 3:
						input.nextLine();
						System.out.println("Select the number of an account to deposit into.");
						index = input.nextInt() - 1;
						System.out.println((Account.accountList.get(index)));
						System.out.println("Enter an amount to deposit.");
						amount = input.nextDouble();
						System.out.println("Deposit $" + amount + " into " + Account.accountList.get(index).name + "? (Enter 'y' if yes.)");
						input.nextLine();
						if (input.nextLine().equals("y")) {
							if (Account.accountList.get(index).deposit(amount)) {
								System.out.println("Deposit successful.");
							}
							else {
								System.out.println("Deposit unsuccessful.");
							}
						}
						else {
							System.out.println("Deposit cancelled.");
						}
						break;
					case 4:
						input.nextLine();
						System.out.println("Select the number of an account to withdraw from.");
						Account withdrawAccount = Account.accountList.get(input.nextInt() - 1);
						System.out.println("Select the number of an account to deposit into.");
						Account depositAccount = Account.accountList.get(input.nextInt() -1);
						System.out.println(withdrawAccount);
						System.out.println(depositAccount);
						System.out.println("Enter an amount to transfer.");
						amount = input.nextDouble();
						System.out.println("Transfer $" + amount + " from " + withdrawAccount.name + " into " + depositAccount.name + "? (Enter 'y' if yes.)");
						input.nextLine();
						if (input.nextLine().equals("y")) {
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
						break;
					case 5:
						input.nextLine();
						System.out.println("Select the number of an account to delete.");
						index = input.nextInt() - 1;
						System.out.println((Account.accountList.get(index)));
						System.out.println("Are you sure you want to quit? (Enter 'y' if yes.)");
						input.nextLine();
						if (input.nextLine().equals("y")) {
							Account.accountList.get(index).deleteAccount();
							System.out.println("Account deleted.");
						}
						else {
							System.out.println("Deletion cancelled.");
						}
					case 6:
						genMenuLoop = false;
					}
					
				}
				break;
			case 2:
				while (genMenuLoop) {
					subMenuLoop = true;
					Category.printAll();
					switch (Menu.genMenu(input, "category")) {
					case 1:
						input.nextLine();
						System.out.println("Name:");
						String name = input.nextLine();
						System.out.println("Amount:");
						double amount = input.nextDouble();
						input.nextLine();
						System.out.println("Account:");
						Account account = Account.accountMap.get(input.nextLine());
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
						index = input.nextInt() - 1;
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
								newCategory.setAmount(input.nextDouble());
								break;
							case 3: 
								input.nextLine();
								System.out.println("Enter new account: ");
								newCategory.account = Account.accountMap.get(input.nextLine());
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
						index = input.nextInt() - 1;
						System.out.println(Category.categoryList.get(index));
						System.out.println("Are you sure you want to delete this transaction? (Enter 'y' if yes.)");
						if (input.next().equals("y")) {
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
						double amount = input.nextDouble();
						input.nextLine();
						System.out.println("Date:");
						String date = input.nextLine();
						System.out.println("Category:");
						Category category = Category.categoryMap.get(input.nextLine());
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
						index = input.nextInt() - 1;
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
								newTransaction.modifyAmount(input.nextDouble());
								break;
							case 3: 
								input.nextLine();
								System.out.println("Enter new date: ");
								newTransaction.date = input.nextLine();
								break;
							case 4: 
								input.nextLine();
								System.out.println("Enter new category: ");
								newTransaction.modifyCategory(Category.categoryMap.get(input.nextLine()));
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
						index = input.nextInt() - 1;
						System.out.println(Transaction.transactionList.get(index));
						System.out.println("Are you sure you want to delete this transaction? (Enter 'y' if yes.)");
						if (input.next().equals("y")) {
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
		
	}
}
