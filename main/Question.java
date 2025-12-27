package main;
import java.util.ArrayList;
enum QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
}
public class Question {
    private String questionText;
    private ArrayList<String> options;
    private String correctAnswer;
    private double points;
    private QuestionType type;

   
    public Question(String questionText, String correctAnswer, double points, QuestionType type) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.type = type;
        this.options = new ArrayList<>();
    }

    @Override
    public String toString() {
        return questionText + " (" + points + " points, " + type + ")";
    }

    public void displayQuestion() {
        System.out.println("\nQuestion: " + questionText);
        System.out.println("Points: " + points);
        
        if (type.equals(QuestionType.MULTIPLE_CHOICE)) {
            System.out.println("Options:");
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ") " + options.get(i));
            }
        } else if (type.equals(QuestionType.TRUE_FALSE)) {
            System.out.println("Options: True/False");
        }
    }
    
    public void addOption(String option)
    {
        options.add(option);
    }
    public String getQuestionText() 
    {
         return questionText; 
    }
    public ArrayList<String> getOptions()
    {
            return options;
    }
    public String getCorrectAnswer() 
    {
         return correctAnswer; 
    }
    public double getPoints() 
    { 
         return points; 
    }
    public QuestionType getType() 
    { 
        return type; 
    }

  
}
