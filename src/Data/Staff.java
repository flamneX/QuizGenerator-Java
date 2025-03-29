package Data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Staff extends Account {
    public Staff(String[] accInfo) {
        super(accInfo);
    }

    public Staff(Account staff) {
        super((Account) staff);
    }

    //Read Staff Data From File
    public static void accGet(ArrayList<Staff> accList) {
        accList.clear();
        new File("Database").mkdir();
        try {
            File accFile = new File("Database/Staff.txt");
            if(!accFile.exists()) {
                accFile.createNewFile();
            }
            Scanner fileReader = new Scanner(accFile);
            while(fileReader.hasNextLine()) {
                accList.add(new Staff(fileReader.nextLine().split(";")));
            }
            accFile.setWritable(false);
            fileReader.close();
        } catch (Exception e) {
        }
    }

    //Write Account Info In File
    public static void accWrite(ArrayList<Staff> accList) {
        new File("Database").mkdir();

        try {
            File accFile = new File("Database/Staff.txt");
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
