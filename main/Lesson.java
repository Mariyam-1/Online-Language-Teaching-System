package main;
import java.util.*;

public class Lesson extends Task {
    private String content;
    private ArrayList<String> attachments;
    private int durationMinutes;
    private String attachmentUrl;

    public Lesson(String title, String description, String content, int durationMinutes,String attachmentUrl) {
        super(title, description);
        this.content = content;
        this.durationMinutes = durationMinutes;
        this.attachments = new ArrayList<>();
        this.attachmentUrl = attachmentUrl;
    }

    @Override
    public String toString() {
        return getTitle() + " (" + durationMinutes + " minutes)";
    }

    @Override
    public void startTask() {
        
            markStarted();
            System.out.println("Starting lesson: " + getTitle());
            System.out.println("Duration: " + durationMinutes + " minutes");
        
    }

    @Override
    public void displayTaskDetails() {
        System.out.println("Lesson: " + getTitle());
        System.out.println("Duration: " + durationMinutes + " minutes");
        System.out.println("Status: " + (isCompleted() ? "Completed" : isStarted() ? "In Progress" : "Not Started"));
        if (!attachments.isEmpty()) {
            System.out.println("Attachments: " + attachments.size());
        }
    }




    public String getContent() 
    { 
      return content; 
    }
    public int getDurationMinutes()
   { 
    return durationMinutes; 
    }
    public String getAttachmentUrl() 
    {
       return attachmentUrl; 
      }
    public ArrayList<String> getAttachments()
     {
       return attachments; 
      }
}
