package main;

    
public class Users 
{
  protected static int Id=1;
  protected String Name;
  protected String Email;
  protected String Password;
  protected boolean AccountStatus;
  protected Role role;
  
  public Users(String Name, String Email, String Password, Role role)
  {
    Users.Id++; 
    this.Name = Name;
    this.Email = Email;
    this.Password = Password;
    this.role = role;
    this.AccountStatus = true; 
  }

  @Override
  public String toString() {
    return Name + " (" + role + ", " + (AccountStatus ? "Active" : "Inactive") + ")";
  }

  public boolean login(String Email, String Password)
  {
    return this.AccountStatus && this.Email.equals(Email) && this.Password.equals(Password);
  }

  public void ViewProfile()
  {
    System.out.println("Name: " + Name);
    System.out.println("Email: " + Email);
    System.out.println("Role: " + role);
    System.out.println("ID: " + Id);
    System.out.println("Account Status: " + (AccountStatus ? "Active" : "Inactive"));
  }

  public String getRoleMessage() 
  {
    return "Welcome " + Name + "! You are logged in as " + role;
  }

  public String getName()
  {
    return Name;
  }
  public String getEmail()
  {
    return Email;
  }
  public Role getRole()
  {
    return role;
  }
  public boolean isAccountActive()
  { 
    return AccountStatus;
  }
 
  public void deactivateAccount() 
  { 
    this.AccountStatus = false;
  }

  public void activateAccount()
 { 
  this.AccountStatus = true;
 }

}