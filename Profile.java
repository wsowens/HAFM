import java.util.ArrayList;

public class Profile {
    private ArrayList<Account> accountList;
    private ArrayList<Category> categoryList;
    private ArrayList<Transaction> transactionList;
    boolean devMode;

    private Profile(boolean devMode) {
        accountList = new ArrayList<Account>();
        categoryList = new ArrayList<Category>();
        transactionList = new ArrayList<Transaction>();
        this.devMode = devMode;
    }

    public Profile() {
        this(false);
    }

    public void addAccount(String name, double balance) {
        accountList.add(new Account(name, balance));
        dprint("Adding account:\n");
        dprint(accountList.get(accountList.size()-1) + "\n");
    }

    public boolean addCategory(String name, double amount, Account account) {
        if (account == null) {
           dprint("ERROR. ACCOUNT == NULL");
            return false;
        }
        categoryList.add(new Category(name, amount, account));
        dprint("Adding category:\n");
        dprint(categoryList.get(categoryList.size()-1) + "\n");
        return true;
    }

    //make sure that Category is in the categoryList!!
    public void addTransaction(String name, double amount, String date, Category category, String notes) {
        //handle Category == null, or Account == null
        Transaction newTransaction = new Transaction(name, amount, date, category, notes);
        dprint("Adding transaction:\n");
        dprint(newTransaction + "\n");
        newTransaction.apply();
        transactionList.add(newTransaction);
    }

    public boolean removeAccount(int index) {
        dprint("Removing account:\n");
        dprint(accountList.get(index) + "\n");
        boolean success = true;
        for (Category category : categoryList) {
            if (category.getAccount().equals(accountList.get(index))) {
                dprint("ERROR: Category still connected to account:\n" + category + "\n");
                success = false;
            }
        }
        if (success) {
            dprint("Success.\n");
            accountList.remove(index);
        }
        return success;
    }

    public boolean removeCategory(int index) {
        dprint("Removing category:\n");
        dprint(categoryList.get(index) + "\n");
        boolean success = true;
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equals(categoryList.get(index))) {
                dprint("ERROR: Transaction still connected to category:\n" + transaction + "\n");
                success = false;
            }
        }
        if (success) {
            dprint("Success.\n");
            categoryList.remove(index);
        }
        return success;
    }

    //possible make this void
    public boolean removeTransaction(int index) {
        dprint("Removing transaction:\n");
        dprint(transactionList.get(index) + "\n");
        transactionList.get(index).unapply();
        return true;
    }

    public void updateCategory(int index, Category newCategory) {
        dprint("Updating category from:\n");
        dprint(categoryList.get(index) + "\n");
        dprint("to:\n" + newCategory + "\n");
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equals(categoryList.get(index))) {
                transaction.setCategory(newCategory);
            }
        }
    }

    public Account getAccount(int index) {
        //handle index out of bounds
        return accountList.get(index);
    }

    public Account getAccount(String name) {
        for (Account account : accountList) {
            if (account.getName().equals(name)) {
                return account;
            }
        }
        return null;
    }

    public boolean isAccount(int index) {
        return (index > -1) && (index < accountList.size());
    }

    //is this method superfluous?
    public boolean isAccount(String name) {
        for (Account account : accountList) {
            if (account.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Category getCategory(int index) {
        //handle index out of bounds
        return categoryList.get(index);
    }

    public Category getCategory(String name) {
        for (Category category : categoryList) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public boolean isCategory(int index) {
        return (index > -1) && (index < categoryList.size());
    }

    //is this method superfluous?
    public boolean isCategory(String name) {
        for (Category category : categoryList) {
            if (category.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Transaction getTransaction(int index) {
        //handle index out of bounds
        return transactionList.get(index);
    }

    public boolean isTransaction(int index) {
        return (index > -1) && (index < transactionList.size());
    }

    public double getNetWorth() {
		double total = 0;
		for (Account account : this.accountList)
				total += account.getBalance();
		return total;
	}

    void dprint(String input) {
        if (devMode) {
            System.out.print(input);
        }
    }

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        System.out.println("=====Welcome to the #Adulting Profile Manager v. 1.1=====\n");
        System.out.println("adding new Profile");
        Profile profile = new Profile(true);

        //creating accounts
        System.out.println("Creating new account:");
        System.out.println("Name: ");
        String name = input.nextLine();
        System.out.println("Amount: ");
        Double amount = Menu.getValidDouble(input);
        profile.addAccount(name, amount);
        System.out.println("Net Worth: " + profile.getNetWorth());
        System.out.println("Creating second new account:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        profile.addAccount(name, amount);
        System.out.println("Net Worth: " + profile.getNetWorth());

        //creating categories
        System.out.println("Creating new category:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Account: ");
        Account account = profile.getAccount(input.nextLine());
        while (account == null) {
            System.out.println("Error. Account not found.");
            account = profile.getAccount(input.nextLine());
        }
        profile.addCategory(name, amount, account);

        System.out.println("Creating second new category:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Account: ");
        account = profile.getAccount(input.nextLine());
        while (account == null) {
            System.out.println("Error. Account not found.");
            account = profile.getAccount(input.nextLine());
        }
        profile.addCategory(name, amount, account);

        //creating transactions
        System.out.println("Creating new transaction:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Date: ");
        String date = Menu.getValid(input, Menu.dateFormat);
        System.out.println("Category: ");
        Category category = profile.getCategory(input.nextLine());
        while (category == null) {
            System.out.println("Error. Category not found.");
            category = profile.getCategory(input.nextLine());
        }
        System.out.println("Notes: ");
        String notes = input.nextLine();
        profile.addTransaction(name, amount, date, category, notes);
        System.out.println("Net Worth: " + profile.getNetWorth());
        System.out.println("Creating new transaction:");
        System.out.println("Name: ");
        name = input.nextLine();
        System.out.println("Amount: ");
        amount = Menu.getValidDouble(input);
        System.out.println("Date: ");
        date = Menu.getValid(input, Menu.dateFormat);
        System.out.println("Category: ");
        category = profile.getCategory(input.nextLine());
        while (category == null) {
            System.out.println("Error. Category not found.");
            category = profile.getCategory(input.nextLine());
        }
        System.out.println("Notes: ");
        notes = input.nextLine();
        profile.addTransaction(name, amount, date, category, notes);
        System.out.println("Net Worth: " + profile.getNetWorth());
        System.out.println("Enter the index of an account:");
        profile.removeAccount(0);
        System.out.println("Net Worth: " + profile.getNetWorth());
        profile.removeAccount(1);
        System.out.println("Net Worth: " + profile.getNetWorth());
        profile.removeCategory(0);
        profile.removeCategory(1);
        profile.removeTransaction(0);
        System.out.println("Net Worth: " + profile.getNetWorth());
        profile.removeTransaction(1);
        System.out.println("Net Worth: " + profile.getNetWorth());
    }
}
