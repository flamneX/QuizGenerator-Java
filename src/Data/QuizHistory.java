package Data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class QuizHistory {
    private String studentID, date, time;
    private Course selCourse;
    private int chapterAmt;
    private ArrayList<Question> quizList;
    private String[] quizAns;

    //Constructor
    public QuizHistory(String studentID, Course selCourse, int chapterAmt, ArrayList<Question> quizList, String[] quizAns, String date, String time) {
        this.studentID = studentID;
        this.selCourse = selCourse;
        this.chapterAmt = chapterAmt;
        this.quizAns = quizAns;
        this.quizList = quizList;
        this.date = date;
        this.time = time;
    }

    //Accessor
    public String getStudentID() {
        return studentID;
    }

    public Course getCourse() {
        return selCourse;
    }

    public int getChapterAmt() {
        return chapterAmt;
    }

    public ArrayList<Question> getQuestions() {
        return quizList;
    }

    public String[] getAnwers() {
        return quizAns;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }


    //Calculate Correct Answer Amount
    public int correctAns() {
        int counter = 0;
        for (int i = 0; i < quizList.size(); i++) {
            if (quizList.get(i).getAnswer().equals(quizList.get(i).getAnswer(quizAns[i]))) {
                counter++;
            }
        }
        return counter;
    }

    //Records The Quiz In A File
    public void recorder() {
        //Create Quiz Directory
        File course = new File("Database/Quiz/" + selCourse.courseID + " " + selCourse.courseName);
        course.mkdirs();

        try {
            int counter = 1;
            String fileName = studentID + " " + counter + ".txt";

            String[] fileItem = course.list();
            for (String name : fileItem) {
                if (fileName.equals(name)) {
                    counter++;
                    fileName = studentID + " " + counter + ".txt";
                }
            }
            File quiz = new File(course, fileName);
            PrintWriter fileWriter = new PrintWriter(quiz);

            //Insert DataTime
            fileWriter.print(date + ";" + time + ";");

            //Insert Chapter
            fileWriter.println(chapterAmt + ";");

            //Insert Question
            for(int i = 0; i < quizList.size(); i++) {
                fileWriter.println(quizList.get(i).toString()+quizAns[i]+";");
            }
            fileWriter.close();
            quiz.setWritable(false);
        } catch (Exception e) {
        }
    }

    //Read Quiz Data From File
    public static void quizGet(String userID, Course selCourse, ArrayList<QuizHistory> quizHistory) {
        quizHistory.clear();
        
        //Create Quiz Directory
        File course = new File("Database/Quiz/" + selCourse.courseID + " " + selCourse.courseName);
        course.mkdirs();

        try {
            //Get Data From File name
            String[] fileItem = course.list();

            for(String fileName : fileItem) {
                ArrayList<String> ansList = new ArrayList<String>();
                int sep = fileName.indexOf(" ");
                String studentID = fileName.substring(0, sep);
                if (studentID.equals(userID)) {
                    ArrayList<Question> questionList = new ArrayList<Question>();

                    //Read File Contents
                    File quiz = new File(course, fileName);
                    Scanner fileReader = new Scanner(quiz);

                    String[] data = fileReader.nextLine().split(";");
                    String date = data[0];
                    String time = data[1];
                    int chapterAmt = Integer.parseInt(data[2]);

                    while(fileReader.hasNextLine()) {
                        String[] questionData = fileReader.nextLine().split(";");
                        questionList.add(new Question(new Chapter(selCourse, new String[]{"", ""}), Arrays.copyOfRange(questionData, 0, 6)));
                        ansList.add(questionData[6]);
                    }
                    String[] answerList = new String[ansList.size()];
                    answerList = ansList.toArray(answerList);
                    
                    quizHistory.add(new QuizHistory(studentID, selCourse, chapterAmt, questionList, answerList, date, time));
                    fileReader.close();
                }
            }
        } catch (Exception e) {
        }
    }

    //Get List Of Students And No. Of Attempts
    public static void studentGet(Course selCourse, HashMap<String, Integer> studentAttempts) {
        studentAttempts.clear();
        HashSet<String> studentList = new HashSet<String>();
        
        //Create Quiz Directory
        File dir = new File("Database/Quiz/");
        dir.mkdir();
        File course = new File("Database/Quiz/" + selCourse.courseID + " " + selCourse.courseName);
        course.mkdir();

        String[] fileItem = course.list();
        for(String fileName : fileItem) {
            int sep = fileName.indexOf(" ");
            studentList.add(fileName.substring(0, sep));
        }

        for (String student : studentList) {
            int counter = 0;
            for(String fileName : fileItem) {
                int sep = fileName.indexOf(" ");
                if (fileName.substring(0, sep).equals(student)) {
                    counter++;
                }
            }
            studentAttempts.put(student, counter);
        }
    }
}
