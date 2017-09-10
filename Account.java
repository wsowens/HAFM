import java.util.HashMap;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class Account {
	public static HashMap<String, Account> accountMap = new HashMap<String, Account>();
	public static ArrayList<Account> accountList = new ArrayList<Account>();

	public static DecimalFormat df = new DecimalFormat("###.##");
	public static Account nullAcc = new Account();
	public String name;
	public double balance;

	private Account() {
		name = "[no account]";
		this.balance = 0;
	}

	public Account(String name, double balance) {
		this.name = name;
		this.balance = balance;
		accountMap.put(name, this);
		accountList.add(this);
	}

	public double getBalance() {
		return this.balance;
	}

	public String getName() {
		return this.name;
	}

	public boolean withdraw(double value) {
		try {
			this.balance -= value;
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	public boolean deposit(double value) {
		try {
			this.balance += value;
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	public boolean transfer(double value, Account otherAccount) {
		try {
			this.balance -= value;
			otherAccount.balance += value;
			return true;
		}

		catch (Exception ex) {
			return false;
		}
	}

	//implement deleting an account, test that this works, add a flag that prints out in Cat.printAll() if Category lacks an account
	public void deleteAccount() {
		accountMap.remove(this);
		accountList.remove(this);
		for (Category category : Category.categoryList) {
			if (category.account.equals(this)) {
				Transaction[] temp = category.owned.toArray(new Transaction[category.owned.size()]);
				for (Transaction transaction : temp) {
					transaction.modifyCategory(Category.nullCat);
				}
				category.account = Account.nullAcc;
				for (Transaction transaction : temp) {
					transaction.modifyCategory(category);
				}
			}
		}
		this.balance = 0;
	}

	public boolean equals(Account otherAccount) {
		return this.name.equals(otherAccount.name);
	}

	public String toString() {
		String output = name + " | " + "$" + df.format(balance);
		return output;
	}

	public static void printAll() {
		int count = 0;
		for (Account account : Account.accountList) {
			count++;
			System.out.printf("%-4s %-20s | $%-1s", "(" + count + ")", account.name, df.format(account.balance));
			System.out.print("\n");
		}
		System.out.printf("     %-20s | $%-1s", "Total", df.format(Account.getTotal()));
		System.out.print("\n");
	}

	public static double getTotal() {
		double total = 0;
		for (Account account : Account.accountList)
				total += account.balance;
		return total;
	}

	public static void clearNull() {
		Account.nullAcc.balance = 0;
	}

}
