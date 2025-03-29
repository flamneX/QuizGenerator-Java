package Data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends Account {
    public Admin(String[] accInfo) {
        super(accInfo);
    }

    public Admin(Account admin) {
        super((Account) admin);
    }

    //Read Admin Data From File
    public static void accGet(ArrayList<Admin> accList) {
        accList.clear();
        new File("Database").mkdir();
        try {
            File accFile = new File("Database/Admin.txt");
            if(!accFile.exists()) {
                accFile.createNewFile();
            }
            Scanner fileReader = new Scanner(accFile);
            while(fileReader.hasNextLine()) {
                accList.add(new Admin(fileReader.nextLine().split(";")));
            }
            accFile.setWritable(false);
            fileReader.close();
        } catch (Exception e) {
        }
    }

    //Write Account Info In File
    public static void accWrite(ArrayList<Admin> accList) {
        new File("Database").mkdir();

        try {
            File accFile = new File("Database/Admin.txt");
            if(!accFile.exists()) {
                accFile.createNewFile();
            }
            accFile.setWritable(true);
            PrintWriter FileWriter = new PrintWriter(accFile);
            for(Account account : accList) {
                FileWriter.println(account.toString());
            }
            accFile.setWritable(false);
            FileWriter.close();
        } catch (Exception e) {
        }
    }
}