package main;
import java.util.*;

public class Student extends Users implements Comparable<Student> {
  private ArrayList<String> certificates;
  private double wallet;
  private ArrayList<Course> enrolledCourses;
  private double quizResult;
  private double examResult;


  public Student(String name, String email, String password, double wallet)
 {
    super(name, email, password, Role.student);
   
    this.certificates = new ArrayList<>();
    this.wallet = wallet;
    this.enrolledCourses = new ArrayList<>();
    

  }

  

  public void enrollInCourse(Course course) throws IllegalStateException {
    if (course == null) {
      throw new IllegalArgumentException("Course cannot be null");
    }
    
    if (enrolledCourses.contains(course)) {
      throw new IllegalStateException("Already enrolled in this course");
    }
    
    if (this.wallet < course.getPrice()) {
      throw new IllegalStateException("Insufficient funds. Please add more money to your wallet.");
    }
    
    this.enrolledCourses.add(course);
    this.wallet -= course.getPrice();
    course.incrementStudentCount();
    course.getProfessor().receivePayout(course.getPrice());
    System.out.println("Successfully enrolled in: " + course.getTitle());
  
}

  public void unenrollFromCourse(Course course)
   {
    if (this.enrolledCourses.remove(course)) {
      course.decrementStudentCount();
      System.out.println("Successfully unenrolled from: " + course.getTitle());
    }
  }

  public void completeLesson(Course course, Lesson lesson) 
  {
    if (this.enrolledCourses.contains(course)) {
      lesson.setCompleted(true); 
      
      System.out.println("Lesson completed: " + lesson.getTitle());
    }
  }

 


  public void takeQuiz(Course course, Assessment quiz, ArrayList<String> answers) {
    if (!this.enrolledCourses.contains(course)) {
        System.out.println("Not enrolled in this course");
        return;
    }
    
    double score = quiz.submitAnswers(answers);
    this.quizResult = score;
    quiz.setCompleted(true);
    
    System.out.println("Quiz completed with score: " + score);
  }

  public void takeFinalExam(Course course, Assessment exam, ArrayList<String> answers) {
    if (!this.enrolledCourses.contains(course)) {
        System.out.println("Not enrolled in this course");
        return;
    }
    
    double score = exam.submitAnswers(answers);
    this.examResult = score;
    exam.setCompleted(true);
    
    if (exam.calculateGrade()) {
        String certificate = course.getLanguageName() + " - " + course.getTitle();
        this.certificates.add(certificate);
        System.out.println("Congratulations! You earned a certificate in " + certificate);
    }
    
    System.out.println("Final exam completed with score: " + score);
  }

  public double getExamResult() {
    return examResult;
  }

  public double getQuizResult() {
    return quizResult;
  }


  public ArrayList<String> getCertificates()

{
  return certificates;
}

  public double getWallet() 
  {
    return wallet;
  }

  public List<Course> getEnrolledCourses()
  {
    return enrolledCourses;
  }

  @Override
  public String toString() {
    return getName() + " (Student - $" + wallet + ", "
     + enrolledCourses.size() + " courses)";
  }

  @Override
  public void ViewProfile() 
  {
    super.ViewProfile();
    System.out.println("Certificates: " + certificates.size());
    System.out.println("Wallet: $" + wallet);
    System.out.println("Enrolled Courses: " + enrolledCourses.size());
  }

  public double getAverageGrade() {
    if (quizResult == 0 && examResult == 0) {
        return 0;
    }
    double total = 0;
    int count = 0;
    
    if (quizResult > 0) {
        total += quizResult;
        count++;
    }
    if (examResult > 0) {
        total += examResult;
        count++;
    }
    
    return count > 0 ? total / count : 0;
  }

  @Override
  public int compareTo(Student other) {
    return Double.compare(other.getAverageGrade(), this.getAverageGrade());
  }

  public static ArrayList<Student> sortByGrades(ArrayList<Student> students) {
    ArrayList<Student> sortedStudents = new ArrayList<>(students);
    Collections.sort(sortedStudents); 
    return sortedStudents;
  }

  public static void displayStudentRankings(ArrayList<Student> students) {
    ArrayList<Student> sortedStudents = sortByGrades(students);
    System.out.println("\n=== Student Rankings ===");
    for (int i = 0; i < sortedStudents.size(); i++) {
        Student student = sortedStudents.get(i);
        System.out.println( i + 1 + "- " + student.getName() + " - Average Grade: " + student.getAverageGrade());
    }
  }

}
