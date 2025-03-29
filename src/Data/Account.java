package Data;

import java.util.Comparator;

public class Account {
    protected String userID;
    protected String password;
    protected String name;
    protected String email;

    //Constructors
    public Account(String[] accInfo) {
        userID = accInfo[0];
        password = accInfo[1];
        name = accInfo[2];
        email = accInfo[3];
    }

    public Account(Account account) {
        userID = account.userID;
        password = account.password;
        name = account.name;
        email = account.email;
    }

    //Mutators
    public void setID(String userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Accessors
    public String getID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        if (this instanceof Student) {
            return "Student";
        }
        else if (this instanceof Staff) {
            return "Staff";
        }
        else {
            return "Admin";
        }
    }

    //Checks If Both User IDs Are Equal
    @Override
    public boolean equals(Object o) {
        if(o instanceof Account) {
            return this.userID.equals(((Account)o).userID);
        } else {
            return false;
        }
    }
    
    //Used To Sort Account List Based On User ID
    public static Comparator<Account> nameCompare = new Comparator<Account>() {
        public int compare(Account a1, Account a2) {
            return a1.name.compareTo(a2.name);
        }
    };
 
    //Generate A Format To Write In File
    @Override
    public String toString() {
        return userID + ";" + password + ";" + name + ";" + email + ";";
    }
}