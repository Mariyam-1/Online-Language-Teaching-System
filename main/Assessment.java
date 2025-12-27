package main;

import java.util.*;

enum AssessmentType 
{
    QUIZ, FINAL_EXAM
}
public class Assessment extends Task implements Gradable {

   
    private ArrayList<Question> questions;
    private double score;
    private double maxScore;
    private int assessmentDuration;
    private boolean isPassed;
    private AssessmentType type;


    public Assessment(String title, String description, int assessmentDuration, AssessmentType type) {
        super(title, description);
        this.assessmentDuration = assessmentDuration;
        this.questions = new ArrayList<>();
        this.score = 0;
        this.maxScore = 0; // Will be calculated based on total points from questions
        this.isPassed = false;
        this.type = type;
    }

    @Override
    public String toString() {
        return getTitle() + " (" + type + ")";
    }

    @Override
    public void startTask() {
        markStarted();
        System.out.println("Starting " + type.toString() + ": " + getTitle());
        System.out.println("Time limit: " + assessmentDuration + " minutes");
    }

    @Override
    public void displayTaskDetails() {
        System.out.println(type + ": " + getTitle());
        System.out.println("Number of questions: " + questions.size());
        System.out.println("Passing score: 70/" + maxScore);
    }

    public void displayAllQuestions() {
        System.out.println("\n=== " + type + ": " + getTitle() + " ===");
        System.out.println("Time limit: " + assessmentDuration + " minutes");
        System.out.println("Total questions: " + questions.size());
        
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("\nQuestion " + (i + 1));
            questions.get(i).displayQuestion();
        }
    }

    @Override
    public double getGrade() {
        return score;
    }

    @Override
    public boolean calculateGrade() {
        if (maxScore > 0) {
            double percentage = (score / maxScore) * 100;
            isPassed = percentage >= 70;
        } else {
            isPassed = false;
        }
        return isPassed;
    }

    @Override
    public void displayGradeReport() {
        System.out.println(type + " Grade Report");
        System.out.println("Score: " + score + "/" + maxScore);
        System.out.println("Status: " + (isPassed ? "PASSED" : "FAILED"));
    }

    public void addQuestion(Question question) {
        questions.add(question);
        maxScore += question.getPoints(); // Update maxScore when adding questions
    }

    public double submitAnswers(ArrayList<String> answers) {
        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException("Number of answers must match number of questions");
        }

        double totalScore = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String answer = answers.get(i);
            
            if (question.getCorrectAnswer().equalsIgnoreCase(answer.trim())) {
                totalScore += question.getPoints();
            }
        }
        this.score = totalScore;
        calculateGrade(); // Calculate if passed after setting score
        return totalScore;
    }

    public ArrayList<Question> getQuestions() { 
        return questions; 
    }

    public int getassessmentDuration() { 
        return assessmentDuration; 
    }

    public boolean isPassed() { 
        return isPassed; 
    }

    public AssessmentType getType() { 
        return type; 
    }

    public double getMaxScore() {
        return maxScore;
    }
} 