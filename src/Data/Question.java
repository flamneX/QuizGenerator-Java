package Data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class Question extends Chapter {
    protected String question;
    protected String option1;
    protected String option2;
    protected String option3;
    protected String option4;
    protected String answer;

    public Question(Chapter chapter, String[] questionInfo) {
        super(chapter);
        question = questionInfo[0].replaceAll("\\\\n", "\n");
        option1 = questionInfo[1];
        option2 = questionInfo[2];
        option3 = questionInfo[3];
        option4 = questionInfo[4];
        answer = questionInfo[5];
    }

    public Question(Question questionObj) {
        super((Chapter) questionObj);
        question = questionObj.question;
        option1 = questionObj.option1;
        option2 = questionObj.option2;
        option3 = questionObj.option3;
        option4 = questionObj.option4;
        answer = questionObj.answer;
    }


    //Mutators
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    //Accessors
    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    //Checks If Both Questions Are Equal
    @Override
    public boolean equals(Object o) {
        if(o instanceof Question) {
            return this.question.equals(((Question) o).question);
        } else {
            return false;
        }
        
    }

    //Check If User's Answer Is Correct
    public boolean checkAnswer(String userAns) {
        HashMap<String, String> ans = new HashMap<String, String>() {{
            put("A", option1);
            put("B", option2);
            put("C", option3);
            put("D", option4);
        }};
        if (userAns.equals(ans.get(answer))) {
            return true;
        } else {
            return false;
        }
    }

    public String getAnswer(String userAns) {
        HashMap<String, String> ans = new HashMap<String, String>() {{
            put(option1, "A");
            put(option2, "B");
            put(option3, "C");
            put(option4, "D");
        }};
        if (ans.containsKey(userAns)) {
            return ans.get(userAns);
        }
        else {
            return null;
        }
    }
    
    //Generate A Format Used For Writing Files
    @Override
    public String toString() {
        String formatted = question.replaceAll("\n", "\\\\n");
        return formatted + ";" + option1 + ";" + option2 + ";" + option3 + ";" + option4 + ";" + answer + ";";
    }

    //Sort Question List By Question
    public static Comparator<Question> questionCompare = new Comparator<Question>() {
        public int compare(Question q1, Question q2) {
            return q1.question.compareTo(q2.question);
        }
    };

    //Read Question Data Of A Chapter From File
    public static void questionGet(Chapter chapter, ArrayList<Question> questionList) {
        questionList.clear();
        try {
            File questionFile = new File("Database/Course/"+chapter.courseID+" "+chapter.courseName+"/"+chapter.chapterNo+" "+chapter.chapterName+".txt");
            if(!questionFile.exists()) {
                questionFile.createNewFile();
            }
            Scanner fileReader = new Scanner(questionFile);
            while(fileReader.hasNextLine()) {
                questionList.add(new Question(chapter, fileReader.nextLine().split(";")));
            }
            fileReader.close();
        } catch (Exception e) {
        }
    }

    //Read All Questions Of A Course From File
    public static void questionGet(ArrayList<Chapter> chapterList, ArrayList<Question> questionList) {
        questionList.clear();
        for(Chapter chapter : chapterList) {
            try {
                File questionFile = new File("Database/Course/"+chapter.courseID+" "+chapter.courseName+"/"+chapter.chapterNo+" "+chapter.chapterName+".txt");
                if(!questionFile.exists()) {
                    questionFile.createNewFile();
                }
                Scanner fileReader = new Scanner(questionFile);
                while(fileReader.hasNextLine()) {
                    questionList.add(new Question(chapter, fileReader.nextLine().split(";")));
                }
                fileReader.close();
            } catch (Exception e) {
            }
        }
    }

    //Write Question Data Info File
    public static void questionWrite(Chapter chapter, ArrayList<Question> questionList) {
        try {
            File questionFile = new File("Database/Course/"+chapter.courseID+" "+chapter.courseName+"/"+chapter.chapterNo+" "+chapter.chapterName+".txt");
            if(!questionFile.exists()) {
                questionFile.createNewFile();
            }
            PrintWriter FileWriter = new PrintWriter(questionFile);
            for(Question questionItem : questionList) {
                FileWriter.println(questionItem.toString());
            }
            FileWriter.close();
        } catch (Exception e) {
        }
    }
}
