import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ArrayList;

public class Category {
	public static HashMap<String, Category> categoryMap = new HashMap<String, Category>();
	public static ArrayList<Category> categoryList = new ArrayList<Category>();
	public static DecimalFormat df = new DecimalFormat("###.##");
	public static Category nullCat = new Category();

	private String name;
	private double amount;
	public double remaining;
	public ArrayList<Transaction> owned;

	//should I tie categories to a specific account? I guess it's okay for now
	public Account account;

	//later include some sort of foolproofing mechanism to prevent error from duplicate names
	private Category() {
		name = "[no category]";
		amount = 0;
		account = Account.nullAcc;
		owned = new ArrayList<Transaction>();
	}

	public Category(String name, double amount, Account account) {
		this.name = name;
		this.amount = amount;
		this.remaining = amount;
		categoryMap.put(name, this);
		this.account = account;
		categoryList.add(this);
		owned = new ArrayList<Transaction>();
	}
	public Category(String name, double amount, double remaining, Account account) {
		this.name = name;
		this.amount = amount;
		this.remaining = remaining;
		categoryMap.put(name, this);
		this.account = account;
		categoryList.add(this);
		owned = new ArrayList<Transaction>();
	}


	public Category(Category toCopy) {
		this.name = toCopy.name;
		this.amount = toCopy.amount;
		this.remaining = toCopy.remaining;
		//no map assignment... overwrites the other map assignment, could potentially delete
		this.account = toCopy.account;
		//no reason to add to categoryList, only has to be removed
		//creating an empty ArrayList for new owned
		owned = new ArrayList<Transaction>();
	}

	public void setName(String newName) {
		categoryMap.remove(this.name);
		this.name = newName;
		categoryMap.put(newName, this);
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

	public void deleteCategory() {
		categoryMap.remove(this);
		categoryList.remove(this);
		for (Transaction transaction : this.owned.toArray(new Transaction[this.owned.size()])) {
			transaction.modifyCategory(Category.nullCat);
		}
	}

	//assumes that this.owned and newCat.owned are equal
	public void updateCategory(Category newCategory) {
		newCategory.remaining = newCategory.amount;
		categoryMap.remove(this);
		categoryList.remove(this);

		for (Transaction transaction : this.owned.toArray(new Transaction[this.owned.size()])) {
			transaction.modifyCategory(newCategory);
		}
	}


	public boolean equals(Category cat) {
		return (name.equals(cat.name));
	}

	public String toString() {
		String output = name + " | " + "$" + df.format(amount) + " | " + "$" + df.format(remaining) + " | " + "$" + df.format(amount - remaining);
		return output;
	}

	public static void printAll() {
		int count = 0;
		for (Category category : categoryList) {
			count++;
			System.out.printf("%-4s %-20s | $%-8s | $%-8s | $%-8s | %-1s", "(" + count + ")", category.getName(), df.format(category.getAmount()), df.format(category.remaining), df.format(category.getAmount() - category.remaining), category.account.name);
			System.out.print("\n");
		}
		System.out.printf("     %-20s | $%-8s | $%-8s | $%-8s", "Total" , df.format(Category.getAmountTotal()), df.format(Category.getRemainingTotal()), df.format(Category.getAmountTotal() - Category.getRemainingTotal()));
		System.out.print("\n");
	}

	public static double getAmountTotal() {
		double total = 0;
		for (Category category : Category.categoryList)
				total += category.amount;
		return total;
	}

	public static double getRemainingTotal() {
		double total = 0;
		for (Category category : Category.categoryList)
			total += category.remaining;
		return total;
	}
	public static void clearNull() {
		Category.nullCat.amount = 0;
		Category.nullCat.remaining = 0;
	}
}
