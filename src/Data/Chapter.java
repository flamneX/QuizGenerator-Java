package Data;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class Chapter extends Course {
    protected String chapterNo;
    protected String chapterName;

    public Chapter(Course course, String[] chapterInfo) {
        super(course);
        chapterNo = chapterInfo[0];
        chapterName = chapterInfo[1];
    }

    public Chapter(Chapter chapter) {
        super((Course) chapter);
        chapterNo = chapter.chapterNo;
        chapterName = chapter.chapterName;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    //Accesssor
    public String getChapterNo() {
        return chapterNo;
    }

    public String getChapterName() {
        return chapterName;
    }

    //Checks If Both Chapter No.s Are Equal
    @Override
    public boolean equals(Object o) {
        if(o instanceof Chapter) {
            return this.chapterNo.equals(((Chapter) o).chapterNo);
        } else {
            return false;
        }
        
    }
    
    //Sort Chapter By Chapter Number
    public static Comparator<Chapter> chapterNoCompare = new Comparator<Chapter>() {
        public int compare(Chapter c1, Chapter c2) {
            int no1 = Integer.parseInt(c1.chapterNo);
            int no2 = Integer.parseInt(c2.chapterNo);
            return no1 - no2;
        }
    };

    //Create New Chapter File
    public void fileCreate() {
        try {
            File chapterFile = new File("Database/Course/"+courseID+" "+courseName+"/"+chapterNo+" "+chapterName+".txt");
            if(!chapterFile.exists()) {
                chapterFile.createNewFile();
            }
        } catch (IOException e) {
        }
    }

    //Read Chapter Data From Folder
    public static void chapterGet(Course course, ArrayList<Chapter> chapterList) {
        chapterList.clear();
        try {
            File chapterFile = new File("Database/Course/"+course.courseID+" "+course.courseName);
            String[] dirName =  chapterFile.list();
            for(String dirItem : dirName) {
                int sep = dirItem.indexOf(" ");
                int sep2 = dirItem.indexOf(".txt");
                chapterList.add(new Chapter(course, new String[]{dirItem.substring(0, sep), dirItem.substring(sep+1, sep2)}));
            }
            Collections.sort(chapterList, Chapter.chapterNoCompare);
        } catch (Exception e) {
            new File("Database/Course/"+course.courseID+" "+course.courseName).mkdir();
        }
    }

    //Rename Chapter File
    public void fileRename(Chapter newChapter) {
        File oldFile = new File("Database/Course/"+courseID+" "+courseName+"/"+chapterNo+" "+chapterName+".txt");
        File newFile = new File("Database/Course/"+newChapter.courseID+" "+newChapter.courseName+"/"+newChapter.chapterNo+" "+newChapter.chapterName+".txt");
        oldFile.renameTo(newFile);
    }

    //Delete Chapter File
    public void fileDelete() {
        File chapterFile = new File("Database/Course/"+courseID+" "+courseName+"/"+chapterNo+" "+chapterName+".txt");
        chapterFile.delete();
    }
}
