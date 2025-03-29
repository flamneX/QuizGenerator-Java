package Data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Student extends Account {
    public Student(String[] accInfo) {
        super(accInfo);
    }

    public Student(Account student) {
        super((Account) student);
    }

    //Rename Quiz Files
    public void QuizRename(Account newAcc) {
        File database = new File("Database/Quiz");
        database.mkdirs();
        String[] courses = database.list();

        for (String course : courses) {
            File courseFile = new File(database, course);

            String[] fileItem = courseFile.list();
            for (String fileName : fileItem) {
                int sep1 = fileName.indexOf(" ");
                int sep2 = fileName.indexOf(".txt");
                if (userID.equals(fileName.substring(0, sep1))) {
                    File file = new File(courseFile, fileName);
                    String newName = newAcc.getID() + " " + fileName.substring(sep1+1, sep2) + ".txt";
                    File newFile = new File(courseFile, newName);
                    file.renameTo(newFile);
                }
            }
        }
    }

    //Delete Quiz Files
    public void QuizDelete() {
        File database = new File("Database/Quiz");
        database.mkdirs();
        String[] courses = database.list();

        for (String course : courses) {
            File courseFile = new File(database, course);

            String[] fileItem = courseFile.list();
            for (String fileName : fileItem) {
                int sep1 = fileName.indexOf(" ");
                if (userID.equals(fileName.substring(0, sep1))) {
                    new File(courseFile, fileName).delete();
                }
            }
        }
    }

    //Read Student Data From File
    public static void accGet(ArrayList<Student> accList) {
        accList.clear();
        new File("Database").mkdir();
        try {
            File accFile = new File("Database/Student.txt");
            if(!accFile.exists()) {
                accFile.createNewFile();
            }
            Scanner fileReader = new Scanner(accFile);
            while(fileReader.hasNextLine()) {
                accList.add(new Student(fileReader.nextLine().split(";")));
            }
            accFile.setWritable(false);
            fileReader.close();
        } catch (Exception e) {
        }
    }

    //Write Account Info In File
    public static void accWrite(ArrayList<Student> accList) {
        new File("Database").mkdir();

        try {
            File accFile = new File("Database/Student.txt");
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
