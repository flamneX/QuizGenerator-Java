package Data;

import java.io.File;
import java.util.Comparator;
import java.util.ArrayList;

public class Course {
    protected String courseID;
    protected String courseName;

    //Constructors
    public Course(String[] courseInfo) {
        courseID = courseInfo[0];
        courseName = courseInfo[1];
    }

    public Course(Course course) {
        courseID = course.courseID;
        courseName = course.courseName; 
    }

    //Mutators
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    //Accessors
    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }
    
    //Checks If Both Course IDs Are Equal
    @Override
    public boolean equals(Object o) {
        if(o instanceof Course) {
            return this.courseID.equals(((Course) o).courseID);
        } else {
            return false;
        }
        
    }

    //Used To Sort Courses By Course ID
    public static Comparator<Course> IDcompare = new Comparator<Course>() {
        public int compare(Course c1, Course c2) {
            return c1.courseID.compareTo(c2.courseID);
        }
    };

    //Create New Course Folder
    public void dirCreate() {
        File courseDir = new File("Database/Course/" + courseID + " " + courseName);
        courseDir.mkdirs();
    }

    //Read Course Data From Folder
    public static void courseGet(ArrayList<Course> courseList) {
        courseList.clear();
        try {
            File courseFile = new File("Database/Course");
            String[] dirName =  courseFile.list();
            for(String dirItem : dirName) {
                int sep = dirItem.indexOf(" ");
                courseList.add(new Course(new String[]{dirItem.substring(0, sep), dirItem.substring(sep+1, dirItem.length())}));
            }
        } catch (Exception e) {
            new File("Database/Course").mkdir();
        }
    }

    //Rename Course Folder
    public void dirRename(Course newCourse) {
        File oldFile = new File("Database/Course/" + courseID + " " + courseName);
        File newFile = new File("Database/Course/" + newCourse.courseID + " " + newCourse.courseName);
        oldFile.renameTo(newFile);

        File oldQuiz = new File("Database/Quiz/" + courseID + " " + courseName);
        File newQuiz = new File("Database/Quiz/" + newCourse.courseID + " " + newCourse.courseName);
        oldQuiz.renameTo(newQuiz);
    }

    //Delete Course Folder
    public void dirDelete() {
        File courseDir = new File("Database/Course/" + courseID + " " + courseName);
        String[] dirItem = courseDir.list();
        for(String file : dirItem) {
            new File(courseDir.getPath(), file).delete();
        }
        courseDir.delete();

        File quizDir = new File("Database/Quiz/" + courseID + " " + courseName);
        String[] quizItem = quizDir.list();
        for(String file : quizItem) {
            new File(courseDir.getPath(), file).delete();
        }
        quizDir.delete();
    }
}
