import java.text.DecimalFormat;
import java.util.ArrayList;

public class Transaction {
	public static ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	public static DecimalFormat df = new DecimalFormat("###.##");
	
	private double amount;
	public String date;
	public String name;
	public String notes;
	private Category category;

	
	public Transaction(String name, double amount, String date, Category category, String notes) {
		this.name = name;
		this.amount = amount;
		this.date = date;
		addCategory(category);
		this.notes = notes;
		
		transactionList.add(this);
	}
	
	public Transaction(Transaction toCopy) {
		this.name = toCopy.name;
		this.amount = toCopy.amount;
		this.date = toCopy.date;
		this.category = toCopy.category;
		this.addCategory(category);
		this.notes = toCopy.notes;
	}
	
	
	public void addCategory(Category category) {
		this.category = category;
		category.owned.add(this);
		category.remaining -= this.amount;
		category.account.balance -= this.amount;
	}
	
	public void removeCategory() {
		category.owned.remove(this);
		category.remaining += this.amount;
		category.account.balance += this.amount;
		//should I subtrack this.amount from nullCat and nullAcc.balance?
		category = Category.nullCat;
	}
	
	public void modifyCategory(Category newCategory) {
		removeCategory();
		addCategory(newCategory);
	}
	
	//careful... ideally this is not public so main cannot access it
	//of course, this implies that transaction and category are in the same package
	Category getCategory() {
		return this.category;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void modifyAmount(double newAmount) {
		category.remaining += amount;
		category.account.balance += amount;
		this.amount = newAmount;
		category.remaining -= newAmount;
		category.account.balance -= newAmount;
	}
	
	
	
	//may need to do more than this to thoroughly delete transaction
	public void deleteTransaction() {
		transactionList.remove(this);
		removeCategory();
	}
	
	
	public String toString() {
		String output = name + " | " + "$" + df.format(amount) + " | " + date + " | " + category.getName() + " | " + notes;
		return output;
	}
	
	public static void printAll() {
		int count = 0;
		for (Transaction transaction : Transaction.transactionList) {
			count++;
			System.out.printf("%-4s %-20s | $%-8s | %-7s | %-15s | %-1s" , "(" + count + ")", transaction.name , df.format(transaction.getAmount()), transaction.date, transaction.getCategory().getName(), transaction.notes);
			System.out.print("\n");
		}
		
	}
	
}