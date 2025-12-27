package main;
import java.util.*;

public class Professor extends Users {
    private double wallet;
    private String bio;
    private ArrayList<Course> assignedCourses;
    private ArrayList<String> specialties;

    public Professor(String name, String email, String password,String bio)
     {
        super(name, email, password, Role.professor);
        this.wallet = 0.0;
        this.bio = bio;
        this.assignedCourses = new ArrayList<>();
        this.specialties = new ArrayList<>();
    }

    @Override
    public String toString() {
        return getName() + " (Professor - " + specialties.size() + " specialties)";
    }

    public void createContent(Course course, String title, String description, ContentType type, String attachmentUrl) {
        if (course == null || title == null || description == null) {
            throw new IllegalArgumentException("Course, title, and description cannot be null");
        }
        
        if (!assignedCourses.contains(course)) {
            throw new IllegalStateException("You are not assigned to this course");
        }

        switch (type) {
            case LESSON:
                course.addLesson(new Lesson(title, description, "", 60, attachmentUrl));
                break;
            case ASSESSMENT:
                course.addAssessment(new Assessment(title, description, 60, AssessmentType.QUIZ));
                break;
        }
        System.out.println(type + " created: " + title);
    }

    public void addQuestionToAssessment(Course course, Assessment assessment, String questionText, 
            String correctAnswer, double points, QuestionType type) {
        if (!assignedCourses.contains(course)) {
            System.out.println("You are not assigned to this course.");
            return;
        }

        Question question = new Question(questionText, correctAnswer, points, type);
        assessment.addQuestion(question);
        System.out.println("Question added to " + assessment.getTitle());
    }

    public void addOptionToQuestion(Question question, String option) {
        if (question.getType().equals(QuestionType.MULTIPLE_CHOICE)) {
            question.addOption(option);
            System.out.println("Option added to question: " + option);
        }
    }
    public boolean assignCourse(Course course) {
        if (course.getProfessor().equals(this)) {
            if (!assignedCourses.contains(course)) {
                assignedCourses.add(course);
                return true;
            }
        }
        return false;
    }

    public void viewAssessmentQuestions(Assessment assessment) {
        assessment.displayAllQuestions();
    }

    public void addSpecialty(String specialty) {
        this.specialties.add(specialty);
        System.out.println("Added specialty: " + specialty);
    }

    public void receivePayout(double amount) {
        this.wallet += amount;
        System.out.println("Received payout: $" + amount);
        System.out.println("New wallet balance: $" + this.wallet);
    }

    @Override
    public void ViewProfile() {
        super.ViewProfile();
        System.out.println("Bio: " + bio);
        System.out.println("Assigned Courses: " + assignedCourses.size());
        System.out.println("Specialties: " + specialties.size());
        System.out.println("Wallet: $" + wallet);
    }

    public double getWallet() { return wallet; }
    public String getBio() { return bio; }
    public List<Course> getAssignedCourses() { return assignedCourses; }
    public List<String> getSpecialties() { return specialties; }
}