 package main;
enum ContentType {
  LESSON,
  ASSESSMENT
} 
public abstract class Task 
{
  private String title;
  private String description;
  private boolean isCompleted;
  private boolean isStarted;
  private java.time.LocalDateTime startTime;
  private java.time.LocalDateTime completionTime;

  public Task(String title, String description) 
  {
    this.title = title;
    this.description = description;
    this.isCompleted = false;
    this.isStarted = false;
  }

    
  public String getTitle() 
  { 
    return title; 
  }
  public String getDescription() 
  { 
    return description; 
  }
  public boolean isCompleted() 
  { 
    return isCompleted; 
  }

  public boolean isStarted() {
    return isStarted;
  }

  public java.time.LocalDateTime getStartTime() {
    return startTime;
  }

  public java.time.LocalDateTime getCompletionTime() {
    return completionTime;
  }

  protected void markStarted() {
    if (!isStarted()) {
      this.isStarted = true;
      this.startTime = java.time.LocalDateTime.now();
    }
  }

  public void setCompleted(boolean completed) 
  {
    if (completed && !this.isCompleted) {
      this.completionTime = java.time.LocalDateTime.now();
    }
    this.isCompleted = completed;
  }

  
  public abstract void startTask();
  public abstract void displayTaskDetails();
}
