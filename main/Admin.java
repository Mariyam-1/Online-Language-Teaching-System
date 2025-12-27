package main;
import java.util.*;

public class Admin extends Users
{
  private ArrayList<Users> users;
  private ArrayList<Course> courses;

  public Admin(String name, String email, String password)
  {
    super(name, email, password, Role.admin);
    this.users = new ArrayList<>();
    this.courses = new ArrayList<>();
  }

  @Override
  public String toString() {
    return getName() + " (Admin - Managing " + users.size() + " users, " + courses.size() + " courses)";
  }

  public void addUser(Users user) {
    users.add(user);
    System.out.println("Added user: " + user.getName());
  }
  
  public void removeUser(Users user) {
    users.remove(user);
    System.out.println("Removed user: " + user.getName());
  }
  
  public void createCourse(String title, String language, Level level, double price, Professor professor) {
    Course newCourse = new Course(title, language, level, price, professor);
    courses.add(newCourse);
    System.out.println("Created course: " + title);
  }

  
  @Override
  public void ViewProfile() {
    super.ViewProfile();
    System.out.println("Managing Users: " + users.size());
    System.out.println("Managing Courses: " + courses.size());
  }

  public ArrayList<Users> getUsers() { return users; }
  public ArrayList<Course> getCourses() { return courses; }

  public void displaySortedCourses() {
    ArrayList<Course> sortedCourses = Course.sortByPrice(courses) ;
    System.out.println("\n=== Courses Sorted by Price" + "Ascending" + " ===");
    for (Course course : sortedCourses) {
        System.out.println(course.getTitle() + " - $" + course.getPrice() +
         " (Level: " + course.getLevel() + ", Students: " +
          course.getNumberOfStudents() + ")");
    }
  }

  public void displayStudentsByGrade() {
    ArrayList<Student> students = new ArrayList<>();
    for (Users user : users) {
        if (user instanceof Student) {
            students.add((Student) user);
        }
    Student.displayStudentRankings(students);
    }
  }


}
