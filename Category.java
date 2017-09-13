public class Category {
	private String name;
	private double amount;
	private double remaining;
	private Account account;

	public Category(String name, double amount, Account account) {
		this(name, amount, amount, account);
	}

	public Category(String name, double amount, double remaining, Account account) {
		this.name = name;
		this.amount = amount;
		this.remaining = remaining;
		this.account = account;
	}

	public Category(Category toCopy) {
		this.name = toCopy.name;
		this.amount = toCopy.amount;
		this.remaining = toCopy.remaining;
		this.account = toCopy.account;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public void setAmount(double newAmount) {
		double diff = amount - remaining;
		this.amount = newAmount;
		this.remaining = newAmount - diff;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setRemaining(double newRemaining) {
		this.remaining = newRemaining;
	}

	public double getRemaining() {
		return this.remaining;
	}

	public String getName() {
		return name;
	}

	public double getAmount() {
		return amount;
	}

	public Account getAccount() {
		return this.account;
	}

	public boolean equals(Category cat) {
		return (name.equals(cat.name));
	}

	//make this work with profile
	public String toString() {
		String output = name + " | " + "$" + Menu.formatNum(amount) + " | " + "$" + Menu.formatNum(remaining) + " | " + "$" + Menu.formatNum(amount - remaining);
		return output;
	}

}
