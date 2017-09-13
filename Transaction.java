public class Transaction {
	private double amount;
	private String date;
	private String name;
	private String notes;
	private Category category;

	public Transaction(String name, double amount, String date, Category category, String notes) {
		this.name = name;
		this.amount = amount;
		this.date = date;
		this.category = category;
		this.notes = notes;
	}

	public Transaction(Transaction toCopy) {
		this.name = toCopy.name;
		this.amount = toCopy.amount;
		this.date = toCopy.date;
		this.category = toCopy.category;
		this.notes = toCopy.notes;
	}

	public void apply() {
		this.category.setRemaining(category.getRemaining() - this.amount);
		this.category.getAccount().withdraw(this.amount);
	}

	public void unapply() {
		this.category.setRemaining(category.getRemaining() + this.amount);
		this.category.getAccount().deposit(this.amount);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAmount(Double amount) {
		this.unapply();
		this.amount = amount;
		this.apply();
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setCategory(Category newCategory) {
		//possibly redundant to have it do this...
		this.unapply();
		this.category = newCategory;
		this.apply();
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getName() {
		return name;
	}

	public double getAmount() {
		return amount;
	}

	public String getDate() {
		return date;
	}

	public Category getCategory() {
		return this.category;
	}

	public String getNotes() {
		return notes;
	}

	//make this work with profile
	public String toString() {
		String output = name + " | " + "$" + Menu.formatNum(amount) + " | " + date + " | " + category.getName() + " | " + notes;
		return output;
	}

}
