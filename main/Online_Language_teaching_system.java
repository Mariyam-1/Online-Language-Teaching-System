package main;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Online_Language_teaching_system extends Application {
    private AccountManager accountManager = new AccountManager();
    private Stage primaryStage;
    private Users currentUser;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Learning Management System");
        showLoginScreen();
        primaryStage.show();
    }

    // Login Screen
    private void showLoginScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 0);
        TextField emailField = new TextField();
        grid.add(emailField, 1, 0);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");
        HBox hbButtons = new HBox(10, loginButton, signupButton);
        grid.add(hbButtons, 1, 2);

        loginButton.setOnAction(e -> {
            try {
                currentUser = accountManager.login(emailField.getText(), passwordField.getText());
                showDashboard();
            } catch (InvalidAccess ex) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", ex.getMessage());
            }
        });

        signupButton.setOnAction(e -> showSignupScreen());

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
    }

    // Sign-up Screen
    private void showSignupScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Name:");
        grid.add(nameLabel, 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 1);
        TextField emailField = new TextField();
        grid.add(emailField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Label roleLabel = new Label("Role:");
        // Create ComboBox with only student and professor roles
        ComboBox<Role> roleCombo = new ComboBox<>(FXCollections.observableArrayList(Role.student, Role.professor));
        roleCombo.setPromptText("Select a role");
        // Set cell factory for better display
        roleCombo.setCellFactory(lv -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString().substring(0, 1).toUpperCase() + item.toString().substring(1));
                }
            }
        });
        // Set button cell for selected value display
        roleCombo.setButtonCell(new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select a role");
                } else {
                    setText(item.toString().substring(0, 1).toUpperCase() + item.toString().substring(1));
                }
            }
        });
        grid.add(roleLabel, 0, 3);
        grid.add(roleCombo, 1, 3);

        Label walletLabel = new Label("Wallet:");
        grid.add(walletLabel, 0, 4);
        TextField walletField = new TextField("0.0");
        grid.add(walletField, 1, 4);
        // Initially hide wallet field
        walletLabel.setVisible(false);
        walletField.setVisible(false);

        Label bioLabel = new Label("Bio:");
        grid.add(bioLabel, 0, 5);
        TextField bioField = new TextField();
        grid.add(bioField, 1, 5);
        // Initially hide bio field
        bioLabel.setVisible(false);
        bioField.setVisible(false);

        // Show/hide fields based on selected role
        roleCombo.setOnAction(e -> {
            Role selectedRole = roleCombo.getValue();
            boolean isStudent = selectedRole == Role.student;
            boolean isProfessor = selectedRole == Role.professor;
            
            walletLabel.setVisible(isStudent);
            walletField.setVisible(isStudent);
            bioLabel.setVisible(isProfessor);
            bioField.setVisible(isProfessor);
            
            // Clear fields when hidden
            if (!isStudent) walletField.setText("0.0");
            if (!isProfessor) bioField.clear();
        });

        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back");
        HBox hbButtons = new HBox(10, signupButton, backButton);
        grid.add(hbButtons, 1, 6);

        signupButton.setOnAction(e -> {
            try {
                Role selectedRole = roleCombo.getValue();
                if (selectedRole == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please select a role");
                    return;
                }

                double wallet = selectedRole == Role.student ? Double.parseDouble(walletField.getText()) : 0.0;
                String bio = selectedRole == Role.professor ? bioField.getText() : "";
                
                Users user = accountManager.signUp(
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    selectedRole,
                    wallet,
                    bio
                );
                showAlert(Alert.AlertType.INFORMATION, "Success", "User " + user.getName() + " created!");
                showLoginScreen();
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Sign-up Failed", ex.getMessage());
            }
        });

        backButton.setOnAction(e -> showLoginScreen());

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
    }

    // Dashboard Router
    private void showDashboard() {
        if (currentUser instanceof Admin) {
            showAdminDashboard((Admin) currentUser);
        } else if (currentUser instanceof Professor) {
            showProfessorDashboard((Professor) currentUser);
        } else if (currentUser instanceof Student) {
            showStudentDashboard((Student) currentUser);
        }
    }

    // Admin Dashboard
    private void showAdminDashboard(Admin admin) {
        BorderPane borderPane = new BorderPane();
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));

        Button profileButton = new Button("View Profile");
        Button logoutButton = new Button("Logout");
        HBox topButtons = new HBox(10, profileButton, logoutButton);
        topBox.getChildren().add(topButtons);

        profileButton.setOnAction(e -> {
            StringBuilder profile = new StringBuilder();
            admin.ViewProfile();
            profile.append("Name: ").append(admin.getName()).append("\n")
                   .append("Email: ").append(admin.getEmail()).append("\n")
                   .append("Role: ").append(admin.getRole()).append("\n")
                   .append("Managing Users: ").append(admin.getUsers().size()).append("\n")
                   .append("Managing Courses: ").append(admin.getCourses().size());
            showAlert(Alert.AlertType.INFORMATION, "Profile", profile.toString());
        });

        logoutButton.setOnAction(e -> {
            currentUser = null;
            showLoginScreen();
        });

        TabPane tabPane = new TabPane();

        // Manage Users Tab
        Tab usersTab = new Tab("Manage Users", createUsersTab(admin));
        usersTab.setClosable(false);

        // Manage Courses Tab
        Tab coursesTab = new Tab("Manage Courses", createCoursesTab(admin));
        coursesTab.setClosable(false);

        // Reports Tab
        Tab reportsTab = new Tab("Reports", createReportsTab(admin));
        reportsTab.setClosable(false);

        tabPane.getTabs().addAll(usersTab, coursesTab, reportsTab);
        borderPane.setTop(topBox);
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    private VBox createUsersTab(Admin admin) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // Users Table
        TableView<Users> usersTable = new TableView<>();
        TableColumn<Users, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Users, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Users, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));
        
        // Add wallet and bio columns
        TableColumn<Users, String> walletCol = new TableColumn<>("Wallet");
        walletCol.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Student) {
                return new SimpleStringProperty(String.format("$%.2f", ((Student) cellData.getValue()).getWallet()));
            }
            return new SimpleStringProperty("");
        });

        TableColumn<Users, String> bioCol = new TableColumn<>("Bio");
        bioCol.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Professor) {
                return new SimpleStringProperty(((Professor) cellData.getValue()).getBio());
            }
            return new SimpleStringProperty("");
        });

        usersTable.getColumns().addAll(nameCol, emailCol, roleCol, walletCol, bioCol);
        usersTable.setItems(FXCollections.observableArrayList(accountManager.getUsers()));

        // Add User Form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        formGrid.add(emailLabel, 0, 1);
        formGrid.add(emailField, 1, 1);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        formGrid.add(passwordLabel, 0, 2);
        formGrid.add(passwordField, 1, 2);

        Label roleLabel = new Label("Role:");
        // Create ComboBox with only student and professor roles for adding users
        ComboBox<Role> roleCombo = new ComboBox<>(FXCollections.observableArrayList(Role.student, Role.professor));
        roleCombo.setPromptText("Select a role");
        // Set cell factory for better display
        roleCombo.setCellFactory(lv -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString().substring(0, 1).toUpperCase() + item.toString().substring(1));
                }
            }
        });
        // Set button cell for selected value display
        roleCombo.setButtonCell(new ListCell<Role>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select a role");
                } else {
                    setText(item.toString().substring(0, 1).toUpperCase() + item.toString().substring(1));
                }
            }
        });
        formGrid.add(roleLabel, 0, 3);
        formGrid.add(roleCombo, 1, 3);

        // Wallet field (for students)
        Label walletLabel = new Label("Wallet:");
        TextField walletField = new TextField("0.0");
        walletLabel.setVisible(false);
        walletField.setVisible(false);
        formGrid.add(walletLabel, 0, 4);
        formGrid.add(walletField, 1, 4);

        // Bio field (for professors)
        Label bioLabel = new Label("Bio:");
        TextField bioField = new TextField();
        bioLabel.setVisible(false);
        bioField.setVisible(false);
        formGrid.add(bioLabel, 0, 5);
        formGrid.add(bioField, 1, 5);

        // Show/hide fields based on selected role
        roleCombo.setOnAction(e -> {
            Role selectedRole = roleCombo.getValue();
            boolean isStudent = selectedRole == Role.student;
            boolean isProfessor = selectedRole == Role.professor;
            
            walletLabel.setVisible(isStudent);
            walletField.setVisible(isStudent);
            bioLabel.setVisible(isProfessor);
            bioField.setVisible(isProfessor);
            
            // Clear fields when hidden
            if (!isStudent) walletField.setText("0.0");
            if (!isProfessor) bioField.clear();
        });

        Button addUserButton = new Button("Add User");
        Button removeUserButton = new Button("Remove Selected User");
        HBox buttonBox = new HBox(10, addUserButton, removeUserButton);

        addUserButton.setOnAction(e -> {
            try {
                Role selectedRole = roleCombo.getValue();
                if (selectedRole == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please select a role");
                    return;
                }

                double wallet = selectedRole == Role.student ? Double.parseDouble(walletField.getText()) : 0.0;
                String bio = selectedRole == Role.professor ? bioField.getText() : "";
                
                Users user = accountManager.signUp(
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    selectedRole,
                    wallet,
                    bio
                );
                
                admin.addUser(user);
                usersTable.getItems().setAll(accountManager.getUsers()); // Refresh with all users
                
                // Clear form
                nameField.clear();
                emailField.clear();
                passwordField.clear();
                roleCombo.setValue(null);
                walletField.setText("0.0");
                bioField.clear();
                
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        removeUserButton.setOnAction(e -> {
            Users selected = usersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getRole() == Role.admin) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Cannot remove admin user");
                    return;
                }
                admin.removeUser(selected);
                usersTable.getItems().setAll(accountManager.getUsers()); // Refresh with all users
                showAlert(Alert.AlertType.INFORMATION, "Success", "User removed successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a user to remove.");
            }
        });

        vbox.getChildren().addAll(
            new Label("All Users"),
            usersTable,
            new Separator(),
            new Label("Add New User"),
            formGrid,
            buttonBox
        );
        
        return vbox;
    }

    private VBox createCoursesTab(Admin admin) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // Course Table
        TableView<Course> coursesTable = new TableView<>();
        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Course, String> languageCol = new TableColumn<>("Language");
        languageCol.setCellValueFactory(new PropertyValueFactory<>("languageName"));
        TableColumn<Course, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty("$" + cellData.getValue().getPrice()));
        TableColumn<Course, String> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLevel().toString()));
        TableColumn<Course, String> profCol = new TableColumn<>("Professor");
        profCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfessor().getName()));
        TableColumn<Course, String> studentsCol = new TableColumn<>("Enrolled Students");
        studentsCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumberOfStudents())));
        
        coursesTable.getColumns().addAll(titleCol, languageCol, levelCol, priceCol, profCol, studentsCol);
        coursesTable.setItems(FXCollections.observableArrayList(admin.getCourses()));

        // Create Course Form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        // Course Title
        Label titleLabel = new Label("Course Title:");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter course title");
        formGrid.add(titleLabel, 0, 0);
        formGrid.add(titleField, 1, 0);

        // Language
        Label languageLabel = new Label("Language:");
        TextField languageField = new TextField();
        languageField.setPromptText("Enter language name");
        formGrid.add(languageLabel, 0, 1);
        formGrid.add(languageField, 1, 1);

        // Level
        Label levelLabel = new Label("Level:");
        ComboBox<Level> levelCombo = new ComboBox<>(FXCollections.observableArrayList(Level.values()));
        levelCombo.setPromptText("Select course level");
        formGrid.add(levelLabel, 0, 2);
        formGrid.add(levelCombo, 1, 2);

        // Price
        Label priceLabel = new Label("Price ($):");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter course price");
        formGrid.add(priceLabel, 0, 3);
        formGrid.add(priceField, 1, 3);

        // Professor
        Label professorLabel = new Label("Professor:");
        ComboBox<Professor> professorCombo = new ComboBox<>();
        professorCombo.setPromptText("Select course professor");
        List<Professor> professors = new ArrayList<>();
        for (Users user : accountManager.getUsers()) {
            if (user instanceof Professor) {
                professors.add((Professor) user);
            }
        }
        professorCombo.setItems(FXCollections.observableArrayList(professors));

        formGrid.add(professorLabel, 0, 4);
        formGrid.add(professorCombo, 1, 4);

        // Buttons
        Button createButton = new Button("Create Course");
        Button clearButton = new Button("Clear Form");
        HBox buttonBox = new HBox(10, createButton, clearButton);

        clearButton.setOnAction(e -> {
            titleField.clear();
            languageField.clear();
            levelCombo.setValue(null);
            priceField.clear();
            professorCombo.setValue(null);
        });

        createButton.setOnAction(e -> {
            try {
                if (titleField.getText().isEmpty() || languageField.getText().isEmpty() || 
                    levelCombo.getValue() == null || priceField.getText().isEmpty() || 
                    professorCombo.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields");
                    return;
                }

                double price = Double.parseDouble(priceField.getText());
                admin.createCourse(
                    titleField.getText(),
                    languageField.getText(),
                    levelCombo.getValue(),
                    price,
                    professorCombo.getValue()
                );
                
                // Clear form
                clearButton.fire();
                
                // Refresh table
                coursesTable.setItems(FXCollections.observableArrayList(admin.getCourses()));
                showAlert(Alert.AlertType.INFORMATION, "Success", "Course created successfully!");
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        vbox.getChildren().addAll(
            new Label("Existing Courses"),
            coursesTable,
            new Separator(),
            new Label("Create New Course"),
            formGrid,
            buttonBox
        );
        
        return vbox;
    }

    private VBox createReportsTab(Admin admin) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Button sortedCoursesButton = new Button("Show Courses Sorted by Price");
        Button studentRankingsButton = new Button("Show Student Rankings");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefRowCount(20);
        reportArea.setPromptText("Report results will appear here");

        sortedCoursesButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Courses Sorted by Price (Ascending) ===\n\n");
            for (Course course : Course.sortByPrice(admin.getCourses())) {
                sb.append(course.getTitle())
                  .append(" - $").append(String.format("%.2f", course.getPrice()))
                  .append("\n• Level: ").append(course.getLevel())
                  .append("\n• Language: ").append(course.getLanguageName())
                  .append("\n• Professor: ").append(course.getProfessor().getName())
                  .append("\n• Enrolled Students: ").append(course.getNumberOfStudents())
                  .append("\n\n");
            }
            reportArea.setText(sb.toString());
        });

        studentRankingsButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Student Rankings by Average Grade ===\n\n");
            ArrayList<Student> students = new ArrayList<>();
            
            for (Users user : accountManager.getUsers()) {
                if (user instanceof Student) {
                    Student student = (Student) user;
                    students.add(student);
                }
            }
            
            if (students.isEmpty()) {
                sb.append("No students found in the system.");
            } else {
                ArrayList<Student> sortedStudents = Student.sortByGrades(students);
                sb.append("Found ").append(students.size()).append(" students\n\n");
                sb.append("Rankings:\n");
                for (int i = 0; i < sortedStudents.size(); i++) {
                    Student student = sortedStudents.get(i);
                    sb.append("\n").append(i + 1).append(". ").append(student.getName())
                      .append("\n• Quiz Result: ").append(String.format("%.1f", student.getQuizResult()))
                      .append("\n• Exam Result: ").append(String.format("%.1f", student.getExamResult()))
                      .append("\n• Average Grade: ").append(String.format("%.1f", student.getAverageGrade()))
                      .append("\n• Enrolled Courses: ").append(student.getEnrolledCourses().size())
                      .append("\n• Certificates: ").append(student.getCertificates().size())
                      .append("\n");
                }
            }
            reportArea.setText(sb.toString());
        });

        vbox.getChildren().addAll(
            new Label("Generate Reports"),
            new HBox(10, sortedCoursesButton, studentRankingsButton),
            new Label("Report Results"),
            reportArea
        );
        
        return vbox;
    }

    // Professor Dashboard
    private void showProfessorDashboard(Professor professor) {
        BorderPane borderPane = new BorderPane();
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));

        Button profileButton = new Button("View Profile");
        Button logoutButton = new Button("Logout");
        HBox topButtons = new HBox(10, profileButton, logoutButton);
        topBox.getChildren().add(topButtons);

        profileButton.setOnAction(e -> {
            StringBuilder profile = new StringBuilder();
            profile.append("Name: ").append(professor.getName()).append("\n")
                   .append("Email: ").append(professor.getEmail()).append("\n")
                   .append("Role: ").append(professor.getRole()).append("\n")
                   .append("Bio: ").append(professor.getBio()).append("\n")
                   .append("Assigned Courses: ").append(professor.getAssignedCourses().size()).append("\n")
                   .append("Specialties: ").append(professor.getSpecialties().size()).append("\n")
                   .append("Wallet: $").append(professor.getWallet());
            showAlert(Alert.AlertType.INFORMATION, "Profile", profile.toString());
        });

        logoutButton.setOnAction(e -> {
            currentUser = null;
            showLoginScreen();
        });

        TabPane tabPane = new TabPane();

        // Manage Content Tab
        Tab contentTab = new Tab("Manage Content", createProfessorContentTab(professor));
        contentTab.setClosable(false);

        // Specialties Tab
        Tab specialtiesTab = new Tab("Specialties", createSpecialtiesTab(professor));
        specialtiesTab.setClosable(false);

        tabPane.getTabs().addAll(contentTab, specialtiesTab);
        borderPane.setTop(topBox);
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    private VBox createProfessorContentTab(Professor professor) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<Course> courseCombo = new ComboBox<>(FXCollections.observableArrayList(professor.getAssignedCourses()));
        courseCombo.setPromptText("Select a course");
        courseCombo.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getTitle());
            }
        });
        courseCombo.setButtonCell(new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "Select a course" : item.getTitle());
            }
        });

        // Create Lesson/Assessment
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField descField = new TextField();
        descField.setPromptText("Description");
        ComboBox<ContentType> typeCombo = new ComboBox<>(FXCollections.observableArrayList(ContentType.values()));
        typeCombo.setPromptText("Select content type");
        typeCombo.setCellFactory(lv -> new ListCell<ContentType>() {
            @Override
            protected void updateItem(ContentType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString().substring(0, 1).toUpperCase() + item.toString().substring(1).toLowerCase());
                }
            }
        });
        typeCombo.setButtonCell(new ListCell<ContentType>() {
            @Override
            protected void updateItem(ContentType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select content type");
                } else {
                    setText(item.toString().substring(0, 1).toUpperCase() + item.toString().substring(1).toLowerCase());
                }
            }
        });
        TextField urlField = new TextField();
        urlField.setPromptText("Attachment URL");
        Button createContentButton = new Button("Create Content");

        createContentButton.setOnAction(e -> {
            try {
                professor.createContent(
                    courseCombo.getValue(),
                    titleField.getText(),
                    descField.getText(),
                    typeCombo.getValue(),
                    urlField.getText()
                );
                showAlert(Alert.AlertType.INFORMATION, "Success", "Content created!");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        // Add Question to Assessment
        ComboBox<Assessment> assessmentCombo = new ComboBox<>();
        assessmentCombo.setPromptText("Select an assessment");
        assessmentCombo.setCellFactory(lv -> new ListCell<Assessment>() {
            @Override
            protected void updateItem(Assessment item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getTitle() + " (" + item.getType() + ")");
            }
        });
        assessmentCombo.setButtonCell(new ListCell<Assessment>() {
            @Override
            protected void updateItem(Assessment item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "Select an assessment" : item.getTitle() + " (" + item.getType() + ")");
            }
        });
        TextField questionTextField = new TextField();
        questionTextField.setPromptText("Question Text");
        TextField answerField = new TextField();
        answerField.setPromptText("Correct Answer");
        TextField pointsField = new TextField();
        pointsField.setPromptText("Points");
        ComboBox<QuestionType> questionTypeCombo = new ComboBox<>(FXCollections.observableArrayList(QuestionType.values()));
        questionTypeCombo.setPromptText("Select question type");
        questionTypeCombo.setCellFactory(lv -> new ListCell<QuestionType>() {
            @Override
            protected void updateItem(QuestionType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Convert MULTIPLE_CHOICE to "Multiple Choice"
                    String text = item.toString().toLowerCase().replace('_', ' ');
                    setText(text.substring(0, 1).toUpperCase() + text.substring(1));
                }
            }
        });
        questionTypeCombo.setButtonCell(new ListCell<QuestionType>() {
            @Override
            protected void updateItem(QuestionType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select question type");
                } else {
                    String text = item.toString().toLowerCase().replace('_', ' ');
                    setText(text.substring(0, 1).toUpperCase() + text.substring(1));
                }
            }
        });
        Button addQuestionButton = new Button("Add Question");

        addQuestionButton.setOnAction(e -> {
            try {
                professor.addQuestionToAssessment(
                    courseCombo.getValue(),
                    assessmentCombo.getValue(),
                    questionTextField.getText(),
                    answerField.getText(),
                    Double.parseDouble(pointsField.getText()),
                    questionTypeCombo.getValue()
                );
                showAlert(Alert.AlertType.INFORMATION, "Success", "Question added!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid input");
            }
        });

        // View Assessment Questions
        Button viewQuestionsButton = new Button("View Assessment Questions");
        TextArea questionsArea = new TextArea();
        questionsArea.setEditable(false);

 viewQuestionsButton.setOnAction(e -> {
    Assessment assessment = assessmentCombo.getValue();
    if (assessment != null) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(assessment.getType()).append(": ").append(assessment.getTitle()).append(" ===\n")
          .append("Time limit: ").append(assessment.getassessmentDuration()).append(" minutes\n")
          .append("Total questions: ").append(assessment.getQuestions().size()).append("\n\n");
        
        for (int i = 0; i < assessment.getQuestions().size(); i++) {
            Question q = assessment.getQuestions().get(i);
            sb.append("Question ").append(i + 1).append("\n")
              .append("Question: ").append(q.getQuestionText()).append("\n")
              .append("Points: ").append(q.getPoints()).append("\n");
            
            if (q.getType().equals(QuestionType.MULTIPLE_CHOICE)) {
                sb.append("Options:\n");
                q.getOptions();  // Sort once before displaying
                for (int j = 0; j < q.getOptions().size(); j++) {
                    sb.append("  ").append(q.getOptions().get(j)).append("\n");
                }
                sb.append("Correct Answer: ").append(q.getCorrectAnswer()).append("\n\n");
            }
        }
        questionsArea.setText(sb.toString());
    }
});

        vbox.getChildren().addAll(
            new Label("Create Lesson/Assessment"),
            courseCombo, titleField, descField, typeCombo, urlField, createContentButton,
            new Label("Add Question to Assessment"),
            assessmentCombo, questionTextField, answerField, pointsField, questionTypeCombo, addQuestionButton,
            viewQuestionsButton, questionsArea
        );
        return vbox;
    }

    private VBox createSpecialtiesTab(Professor professor) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField specialtyField = new TextField();
        specialtyField.setPromptText("New Specialty");
        Button addSpecialtyButton = new Button("Add Specialty");

        ListView<String> specialtiesList = new ListView<>(FXCollections.observableArrayList(professor.getSpecialties()));

        addSpecialtyButton.setOnAction(e -> {
            String specialty = specialtyField.getText();
            if (!specialty.isEmpty()) {
                professor.addSpecialty(specialty);
                specialtiesList.getItems().add(specialty);
                specialtyField.clear();
            }
        });

        vbox.getChildren().addAll(new Label("Specialties"), specialtyField, addSpecialtyButton, specialtiesList);
        return vbox;
    }

    // Student Dashboard
    private void showStudentDashboard(Student student) {
        BorderPane borderPane = new BorderPane();
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));

        Button profileButton = new Button("View Profile");
        Button logoutButton = new Button("Logout");
        HBox topButtons = new HBox(10, profileButton, logoutButton);
        topBox.getChildren().add(topButtons);

        profileButton.setOnAction(e -> {
            StringBuilder profile = new StringBuilder();
            profile.append("Name: ").append(student.getName()).append("\n")
                   .append("Email: ").append(student.getEmail()).append("\n")
                   .append("Role: ").append(student.getRole()).append("\n")
                   .append("Certificates: ").append(student.getCertificates().size()).append("\n")
                   .append("Wallet: $").append(student.getWallet()).append("\n")
                   .append("Enrolled Courses: ").append(student.getEnrolledCourses().size());
            showAlert(Alert.AlertType.INFORMATION, "Profile", profile.toString());
        });

        logoutButton.setOnAction(e -> {
            currentUser = null;
            showLoginScreen();
        });

        TabPane tabPane = new TabPane();

        // Create tabs
        Tab enrollTab = new Tab("Courses");
        Tab lessonsTab = new Tab("Lessons");
        Tab assessmentsTab = new Tab("Assessments");

        // Create content for tabs
        VBox enrollContent = createEnrollTab(student, () -> {
            // Refresh lessons and assessments tabs after enrollment changes
            lessonsTab.setContent(createLessonsTab(student));
            assessmentsTab.setContent(createAssessmentsTab(student));
        });
        
        enrollTab.setContent(enrollContent);
        lessonsTab.setContent(createLessonsTab(student));
        assessmentsTab.setContent(createAssessmentsTab(student));

        enrollTab.setClosable(false);
        lessonsTab.setClosable(false);
        assessmentsTab.setClosable(false);

        tabPane.getTabs().addAll(enrollTab, lessonsTab, assessmentsTab);
        borderPane.setTop(topBox);
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    private VBox createEnrollTab(Student student, Runnable onEnrollmentChange) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TableView<Course> coursesTable = new TableView<>();
        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Course, String> languageCol = new TableColumn<>("Language");
        languageCol.setCellValueFactory(new PropertyValueFactory<>("languageName"));
        TableColumn<Course, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new SimpleStringProperty("$" + cellData.getValue().getPrice()));
        TableColumn<Course, String> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLevel().toString()));
        
        coursesTable.getColumns().addAll(titleCol, languageCol, priceCol, levelCol);
        List<Course> allCourses = new ArrayList<>();
        for (Users user : accountManager.getUsers()) {
            if (user instanceof Admin) {
                allCourses.addAll(((Admin) user).getCourses());
            }
        }
        coursesTable.setItems(FXCollections.observableArrayList(allCourses));

        // Wallet balance display
        Label walletLabel = new Label(String.format("Your Wallet Balance: $%.2f", student.getWallet()));
        walletLabel.setStyle("-fx-font-weight: bold;");

        Button enrollButton = new Button("Enroll");
        Button unenrollButton = new Button("Unenroll");

        enrollButton.setOnAction(e -> {
            Course selected = coursesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    student.enrollInCourse(selected);
                    walletLabel.setText(String.format("Your Wallet Balance: $%.2f", student.getWallet()));
                    coursesTable.refresh();
                    onEnrollmentChange.run();
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Successfully enrolled in: " + selected.getTitle() + "\n" +
                        String.format("Remaining balance: $%.2f", student.getWallet()));
                } catch (IllegalStateException ex) {
                    showAlert(Alert.AlertType.ERROR, "Enrollment Failed", ex.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a course to enroll in.");
            }
        });

        unenrollButton.setOnAction(e -> {
            Course selected = coursesTable.getSelectionModel().getSelectedItem();
            if (selected != null && student.getEnrolledCourses().contains(selected)) {
                student.unenrollFromCourse(selected);
                coursesTable.refresh();
                onEnrollmentChange.run();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Unenrolled from course: " + selected.getTitle());
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Required", 
                    "Please select a course you are enrolled in to unenroll from.");
            }
        });

        vbox.getChildren().addAll(
            walletLabel,
            new Label("Available Courses"), 
            coursesTable, 
            new HBox(10, enrollButton, unenrollButton)
        );
        return vbox;
    }

    private VBox createLessonsTab(Student student) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<Course> courseCombo = new ComboBox<>(FXCollections.observableArrayList(student.getEnrolledCourses()));
        courseCombo.setPromptText("Select Course");

        TableView<Lesson> lessonsTable = new TableView<>();
        TableColumn<Lesson, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Lesson, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Lesson lesson = cellData.getValue();
            return new SimpleStringProperty(
                lesson.isCompleted() ? "Completed" : lesson.isStarted() ? "In Progress" : "Not Started"
            );
        });
        TableColumn<Lesson, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDurationMinutes() + " minutes"));
        
        lessonsTable.getColumns().addAll(titleCol, statusCol, durationCol);

        // Lesson content display
        VBox lessonContentBox = new VBox(10);
        lessonContentBox.setPadding(new Insets(10));
        lessonContentBox.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);
        TextArea contentArea = new TextArea();
        contentArea.setWrapText(true);
        contentArea.setEditable(false);
        contentArea.setPrefRowCount(10);

        Hyperlink attachmentLink = new Hyperlink();
        attachmentLink.setVisible(false);
        
        Button completeButton = new Button("Mark as Complete");
        completeButton.setDisable(true);

        lessonContentBox.getChildren().addAll(
            titleLabel, descriptionLabel, contentArea, attachmentLink, completeButton
        );

        courseCombo.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                lessonsTable.setItems(FXCollections.observableArrayList(newValue.getLessons()));
                clearLessonContent(titleLabel, descriptionLabel, contentArea, attachmentLink, completeButton);
            }
        });

        lessonsTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                // Display lesson content
                titleLabel.setText(newValue.getTitle());
                descriptionLabel.setText(newValue.getDescription());
                contentArea.setText(newValue.getContent());
                
                String attachmentUrl = newValue.getAttachmentUrl();
                if (attachmentUrl != null && !attachmentUrl.isEmpty()) {
                    attachmentLink.setText("View Attachment");
                    attachmentLink.setVisible(true);
                    attachmentLink.setOnAction(e -> showLessonAttachment());
                } else {
                    attachmentLink.setVisible(false);
                }
                
                completeButton.setDisable(false);
                Course selectedCourse = courseCombo.getValue();
                
                completeButton.setOnAction(e -> {
                    student.completeLesson(selectedCourse, newValue);
                    lessonsTable.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Lesson marked as completed!");
                });
            } else {
                clearLessonContent(titleLabel, descriptionLabel, contentArea, attachmentLink, completeButton);
            }
        });

        // Split the view into two parts
        SplitPane splitPane = new SplitPane();
        VBox leftPane = new VBox(10, new Label("Available Lessons"), lessonsTable);
        leftPane.setPadding(new Insets(5));
        
        VBox rightPane = new VBox(10, new Label("Lesson Content"), lessonContentBox);
        rightPane.setPadding(new Insets(5));
        
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.4);
        
        vbox.getChildren().addAll(
            new Label("Select Course"),
            courseCombo,
            splitPane
        );
        
        return vbox;
    }

    private void clearLessonContent(Label titleLabel, Label descriptionLabel, 
                                  TextArea contentArea, Hyperlink attachmentLink, Button completeButton) {
        titleLabel.setText("");
        descriptionLabel.setText("");
        contentArea.setText("");
        attachmentLink.setVisible(false);
        completeButton.setDisable(true);
    }

    private void showLessonAttachment() {
        Stage attachmentStage = new Stage();
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/graduation_globe.png")));
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label("Lesson Resources");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        content.getChildren().addAll(titleLabel, imageView);
        
        Scene scene = new Scene(content, 400, 400);
        attachmentStage.setScene(scene);
        attachmentStage.setTitle("Lesson Attachment");
        attachmentStage.show();
    }

    private VBox createAssessmentsTab(Student student) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ComboBox<Course> courseCombo = new ComboBox<>(FXCollections.observableArrayList(student.getEnrolledCourses()));
        courseCombo.setPromptText("Select Course");

        ComboBox<Assessment> assessmentCombo = new ComboBox<>();
        assessmentCombo.setPromptText("Select Assessment");

        // Question display area
        VBox questionsBox = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(questionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        
        Button submitAssessmentButton = new Button("Submit Assessment");
        submitAssessmentButton.setDisable(true);

        List<TextField> answerFields = new ArrayList<>();

        courseCombo.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                assessmentCombo.setItems(FXCollections.observableArrayList(newValue.getAssessments()));
                questionsBox.getChildren().clear();
                answerFields.clear();
                submitAssessmentButton.setDisable(true);
            }
        });

        assessmentCombo.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            questionsBox.getChildren().clear();
            answerFields.clear();
            
            if (newValue != null) {
                List<Question> questions = newValue.getQuestions();
                
                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    VBox questionBox = new VBox(5);
                    questionBox.setPadding(new Insets(10));
                    questionBox.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

                    // Question header
                    Label questionLabel = new Label((i + 1) + ". " + question.getQuestionText());
                    questionLabel.setWrapText(true);
                    questionLabel.setStyle("-fx-font-weight: bold;");
                    
                    // Points
                    Label pointsLabel = new Label("Points: " + question.getPoints());
                    
                    questionBox.getChildren().addAll(questionLabel, pointsLabel);

                    // Handle different question types
                    TextField questionAnswerField = new TextField();
                    questionAnswerField.setPromptText("Your answer");
                    
                    if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
                        VBox optionsBox = new VBox(5);
                        ToggleGroup optionsGroup = new ToggleGroup();
                        
                        for (String option : question.getOptions()) {
                            RadioButton rb = new RadioButton(option);
                            rb.setToggleGroup(optionsGroup);
                            rb.setWrapText(true);
                            optionsBox.getChildren().add(rb);
                            
                            // Update answer field when radio button is selected
                            rb.selectedProperty().addListener((obs2, old2, selected) -> {
                                if (selected) {
                                    questionAnswerField.setText(option);
                                }
                            });
                        }
                        questionBox.getChildren().add(optionsBox);
                    } else if (question.getType() == QuestionType.TRUE_FALSE) {
                        HBox tfBox = new HBox(10);
                        ToggleGroup tfGroup = new ToggleGroup();
                        
                        RadioButton trueBtn = new RadioButton("True");
                        RadioButton falseBtn = new RadioButton("False");
                        trueBtn.setToggleGroup(tfGroup);
                        falseBtn.setToggleGroup(tfGroup);
                        
                        tfBox.getChildren().addAll(trueBtn, falseBtn);
                        questionBox.getChildren().add(tfBox);
                        
                        // Update answer field when true/false is selected
                        trueBtn.selectedProperty().addListener((obs2, old2, selected) -> {
                            if (selected) questionAnswerField.setText("True");
                        });
                        falseBtn.selectedProperty().addListener((obs2, old2, selected) -> {
                            if (selected) questionAnswerField.setText("False");
                        });
                    }
                    
                    questionBox.getChildren().add(questionAnswerField);
                    answerFields.add(questionAnswerField);
                    questionsBox.getChildren().add(questionBox);
                }
                
                submitAssessmentButton.setDisable(false);
            }
        });

        submitAssessmentButton.setOnAction(e -> {
            Assessment selected = assessmentCombo.getValue();
            Course course = courseCombo.getValue();
            
            // Collect all answers
            List<String> answers = answerFields.stream()
                .map(TextField::getText)
                .toList();
            
            // Check if all questions are answered
            boolean allAnswered = answers.stream().noneMatch(String::isEmpty);
            
            if (selected != null && course != null && allAnswered) {
                if (selected.getType() == AssessmentType.QUIZ) {
                    student.takeQuiz(course, selected, new ArrayList<>(answers));
                } else {
                    student.takeFinalExam(course, selected, new ArrayList<>(answers));
                }
                
                // Show results
                showAlert(Alert.AlertType.INFORMATION, "Assessment Results", 
                    "Assessment submitted!\nScore: " + selected.getGrade() + "\n" +
                    (selected.isPassed() ? "Passed!" : "Failed"));
                
                // Clear form
                questionsBox.getChildren().clear();
                answerFields.clear();
                assessmentCombo.getSelectionModel().clearSelection();
                
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please answer all questions before submitting.");
            }
        });

        vbox.getChildren().addAll(
            new Label("Take Assessment"),
            courseCombo,
            assessmentCombo,
            scrollPane,
            new HBox(10, submitAssessmentButton)
        );
        
        return vbox;
    }

    // Utility Method for Alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }}