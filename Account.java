public class Account {
	private String name;
	private double balance;

	private Account() {
		name = "[no account]";
		this.balance = 0;
	}

	public Account(String name, double balance) {
		this.name = name;
		this.balance = balance;
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

	public boolean equals(Account otherAccount) {
		return this.name.equals(otherAccount.name);
	}

	//possibly incorporate into profile
	public String toString() {
		String output = name + " | " + "$" + Menu.formatNum(balance);
		return output;
	}

}
