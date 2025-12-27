package main;
import java.util.ArrayList;
import java.util.Collections;


enum Level {
    BEGINNER, INTERMEDIATE, PROFESSIONAL
}

public class Course implements Comparable<Course> {
    private String title;
    private String languageName;
    private double price;
    private int numberOfStudents;
    private Level level;
    private Professor professor;
    private ArrayList<Lesson> lessons;
    private ArrayList<Assessment> assessments;
    //private ArrayList<Assignment> assignments;
    
    public Course(String title, String languageName, Level level, double price, Professor professor) {
        this.title = title;
        this.languageName = languageName;
        this.level = level;
        this.price = price; 
        this.professor = professor;
        this.numberOfStudents = 0;  
        this.lessons = new ArrayList<>();
        this.assessments = new ArrayList<>();
       // this.assignments = new ArrayList<>();
       
    }
    
    public void incrementStudentCount() {
        this.numberOfStudents++;
    }
    
    public void decrementStudentCount() {
        if (this.numberOfStudents > 0) {
            this.numberOfStudents--;
        }
    }
    
    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }
    
    public void addAssessment(Assessment assessment) {
        this.assessments.add(assessment);
    }
    
    // public void addAssignment(Assignment assignment) {
    //     this.assignments.add(assignment);
    // }
    

    // *****************ASSIGNMENT*****************
    // public Assessment getFinalExam() { 
    //     for (Assessment assessment : assessments) {
    //         if (assessment.getType() == AssessmentType.FINAL_EXAM) {
    //             return assessment;
    //         }
    //     }
    //     return null;
    // }
    public String getTitle() 
    {
       return title; 
    }
    public String getLanguageName() 
    {
       return languageName;
    }
    public double getPrice()
    {
       return price; 
    }
    public int getNumberOfStudents()
    {
       return numberOfStudents;
    }
    public Level getLevel() 
    {
       return level; 
    }
    public Professor getProfessor() 
    {
       return professor; 
    }
    public ArrayList<Lesson> getLessons() 
    {
       return lessons; 
    }
    public ArrayList<Assessment> getAssessments()
    { 
      return assessments; 
    }
      

    @Override
    public int compareTo(Course other) {
        return Double.compare(this.price, other.price);
    }

    public static ArrayList<Course> sortByPrice(ArrayList<Course> courses) {
        ArrayList<Course> sortedCourses = new ArrayList<>(courses);
        Collections.sort(sortedCourses);
        return sortedCourses;
    }

    @Override
    public String toString() {
        return title + " (" + languageName + " - " + level + ")";
    }
}
