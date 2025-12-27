package main;


import java.util.ArrayList;
import java.util.List;

class InvalidAccess extends Exception {
    public InvalidAccess(String message) {
        super(message);
    }
}

public class AccountManager {
    private List<Users> users = new ArrayList<>();
    
    public AccountManager() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Create professors with diverse specialties
        Professor englishProf = new Professor("John Smith", "john@email.com", "pass123", "Expert in English language teaching with 10 years experience, TESOL certified");
        Professor spanishProf = new Professor("Maria Garcia", "maria@email.com", "pass123", "Native Spanish speaker with PhD in Spanish Literature and 15 years teaching experience");
        Professor frenchProf = new Professor("Pierre Dubois", "pierre@email.com", "pass123", "Certified French language instructor with expertise in business French");
        Professor germanProf = new Professor("Hans Weber", "hans@email.com", "pass123", "Native German speaker specializing in technical and business German");
        Professor chineseProf = new Professor("Li Wei", "li@email.com", "pass123", "Mandarin Chinese instructor with expertise in Chinese business culture");
        
        users.add(englishProf);
        users.add(spanishProf);
        users.add(frenchProf);
        users.add(germanProf);
        users.add(chineseProf);

        // Add specialties for each professor
        englishProf.addSpecialty("Business English");
        englishProf.addSpecialty("TOEFL Preparation");
        englishProf.addSpecialty("Academic Writing");
        englishProf.addSpecialty("English Literature");

        spanishProf.addSpecialty("Conversational Spanish");
        spanishProf.addSpecialty("Spanish Literature");
        spanishProf.addSpecialty("Latin American Studies");
        spanishProf.addSpecialty("Spanish for Business");

        frenchProf.addSpecialty("French Culture");
        frenchProf.addSpecialty("Business French");
        frenchProf.addSpecialty("French Cinema");

        germanProf.addSpecialty("Technical German");
        germanProf.addSpecialty("Business German");
        germanProf.addSpecialty("German Culture");
        germanProf.addSpecialty("German Literature");

        chineseProf.addSpecialty("Business Mandarin");
        chineseProf.addSpecialty("Chinese Culture");
        chineseProf.addSpecialty("HSK Preparation");
        chineseProf.addSpecialty("Chinese Calligraphy");

        // Create admin
        Admin admin = new Admin("Admin User", "admin@email.com", "admin123");
        users.add(admin);

        // Create diverse courses for each language
        // English Courses
        admin.createCourse("English for Beginners", "English", Level.BEGINNER, 99.99, englishProf);
        admin.createCourse("Business English Advanced", "English", Level.PROFESSIONAL, 199.99, englishProf);
        admin.createCourse("TOEFL Preparation Course", "English", Level.INTERMEDIATE, 149.99, englishProf);
        admin.createCourse("Academic Writing in English", "English", Level.PROFESSIONAL, 179.99, englishProf);

        // Spanish Courses
        admin.createCourse("Spanish for Beginners", "Spanish", Level.BEGINNER, 89.99, spanishProf);
        admin.createCourse("Business Spanish", "Spanish", Level.PROFESSIONAL, 189.99, spanishProf);
        admin.createCourse("Spanish Literature and Culture", "Spanish", Level.PROFESSIONAL, 169.99, spanishProf);
        admin.createCourse("Latin American Spanish", "Spanish", Level.INTERMEDIATE, 139.99, spanishProf);

        // French Courses
        admin.createCourse("French for Beginners", "French", Level.BEGINNER, 99.99, frenchProf);
        admin.createCourse("Business French Advanced", "French", Level.PROFESSIONAL, 199.99, frenchProf);
        admin.createCourse("French Culture and Cinema", "French", Level.INTERMEDIATE, 159.99, frenchProf);

        // German Courses
        admin.createCourse("German for Beginners", "German", Level.BEGINNER, 99.99, germanProf);
        admin.createCourse("Technical German", "German", Level.PROFESSIONAL, 209.99, germanProf);
        admin.createCourse("Business German", "German", Level.PROFESSIONAL, 189.99, germanProf);
        admin.createCourse("German Culture Studies", "German", Level.INTERMEDIATE, 149.99, germanProf);

        // Chinese Courses
        admin.createCourse("Mandarin for Beginners", "Chinese", Level.BEGINNER, 109.99, chineseProf);
        admin.createCourse("Business Chinese", "Chinese", Level.PROFESSIONAL, 219.99, chineseProf);
        admin.createCourse("HSK Preparation Course", "Chinese", Level.INTERMEDIATE, 169.99, chineseProf);
        admin.createCourse("Chinese Culture and Customs", "Chinese", Level.PROFESSIONAL, 189.99, chineseProf);

        // Assign courses and add content
        for (Course course : admin.getCourses()) {
            course.getProfessor().assignCourse(course);
            
            // Add multiple lessons per course
            Lesson lesson1 = new Lesson(
                "Introduction to " + course.getLanguageName(),
                "Basic concepts and greetings",
                "Welcome to the course! In this lesson, we'll cover basic greetings and introductions.",
                60,
                course.getLanguageName().equals("English") ? 
                    "https://dl.tutoo.ir/upload/Book/Complete/English%20for%20Everyone/English%20for%20Everyone.%20Business%20English%201%20Course%20Book.pdf" :
                    "https://example.com/" + course.getLanguageName().toLowerCase() + "/lesson1"
            );
            
            Lesson lesson2 = new Lesson(
                course.getLanguageName() + " Grammar Basics",
                "Essential grammar rules and structures",
                "Master the fundamental grammar rules of " + course.getLanguageName(),
                90,
                course.getLanguageName().equals("Spanish") ? 
                    "https://www.kanopystreaming.com/sites/default/files/learningspanish.pdf" :
                    "https://example.com/" + course.getLanguageName().toLowerCase() + "/lesson2"
            );

            Lesson lesson3 = new Lesson(
                "Practical " + course.getLanguageName(),
                "Real-world conversations and scenarios",
                "Practice with real-world examples and dialogues",
                75,
                course.getLanguageName().equals("Chinese") ? 
                    "https://hi-static.z-dn.net/files/dfa/885fc99149c82a73e4c5f9918b4e0442.pdf" :
                    "https://example.com/" + course.getLanguageName().toLowerCase() + "/lesson3"
            );

            Lesson lesson4 = new Lesson(
                "Cultural Insights: " + course.getLanguageName(),
                "Understanding cultural context",
                "Learn about the culture and customs of " + course.getLanguageName() + "-speaking regions",
                60,
                course.getLanguageName().equals("German") ? 
                    "https://www.ircambridge.com/books/german-books/Learn-German-the-Fast-and-Fun-Way.pdf" :
                    "https://example.com/" + course.getLanguageName().toLowerCase() + "/lesson4"
            );

            course.addLesson(lesson1);
            course.addLesson(lesson2);
            course.addLesson(lesson3);
            course.addLesson(lesson4);

            // Add comprehensive quiz
            Assessment quiz = new Assessment(
                course.getLanguageName() + " Progress Quiz",
                "Test your knowledge of basic concepts",
                45,
                AssessmentType.QUIZ
            );

            // Add multiple questions to quiz
            Question q1 = new Question(
                "What is 'Hello' in " + course.getLanguageName() + "?",
                getHelloInLanguage(course.getLanguageName()),
                10,
                QuestionType.MULTIPLE_CHOICE
            );
            q1.addOption(getHelloInLanguage(course.getLanguageName()));
            q1.addOption("Incorrect Option 1");
            q1.addOption("Incorrect Option 2");
            q1.addOption("Incorrect Option 3");

            Question q2 = new Question(
                "Which of these is a formal greeting?",
                getFormalGreeting(course.getLanguageName()),
                10,
                QuestionType.MULTIPLE_CHOICE
            );
            q2.addOption(getFormalGreeting(course.getLanguageName()));
            q2.addOption("Incorrect Formal 1");
            q2.addOption("Incorrect Formal 2");
            q2.addOption("Incorrect Formal 3");

            Question q3 = new Question(
                "Is this language written from left to right?",
                getWritingDirection(course.getLanguageName()),
                5,
                QuestionType.TRUE_FALSE
            );

            quiz.addQuestion(q1);
            quiz.addQuestion(q2);
            quiz.addQuestion(q3);
            
            // Add comprehensive final exam
            Assessment finalExam = new Assessment(
                course.getLanguageName() + " Final Examination",
                "Comprehensive language assessment",
                120,
                AssessmentType.FINAL_EXAM
            );

            // Add multiple questions to final exam
            Question eq1 = new Question(
                "Write a formal business introduction in " + course.getLanguageName(),
                getBusinessIntro(course.getLanguageName()),
                20,
                QuestionType.SHORT_ANSWER
            );

            Question eq2 = new Question(
                "Translate the following phrase: 'Nice to meet you'",
                getNiceMeetYou(course.getLanguageName()),
                15,
                QuestionType.SHORT_ANSWER
            );

            Question eq3 = new Question(
                "Which greeting is appropriate for formal business settings?",
                getFormalGreeting(course.getLanguageName()),
                15,
                QuestionType.MULTIPLE_CHOICE
            );
            eq3.addOption(getFormalGreeting(course.getLanguageName()));
            eq3.addOption("Incorrect Business 1");
            eq3.addOption("Incorrect Business 2");
            eq3.addOption("Incorrect Business 3");

            Question eq4 = new Question(
                "Is this the correct way to write the date in " + course.getLanguageName() + "?",
                "True",
                10,
                QuestionType.TRUE_FALSE
            );

            finalExam.addQuestion(eq1);
            finalExam.addQuestion(eq2);
            finalExam.addQuestion(eq3);
            finalExam.addQuestion(eq4);

            course.addAssessment(quiz);
            course.addAssessment(finalExam);
        }

        // Create sample students with different levels
        Student beginnerStudent = new Student("Sample Student", "student@email.com", "pass123", 500.0);
        Student intermediateStudent = new Student("Alex Johnson", "alex@email.com", "pass123", 1000.0);
        Student advancedStudent = new Student("Maria Chen", "mchen@email.com", "pass123", 1500.0);
        
        users.add(beginnerStudent);
        users.add(intermediateStudent);
        users.add(advancedStudent);
    }

    private String getHelloInLanguage(String language) {
        return switch (language) {
            case "English" -> "Hello";
            case "Spanish" -> "Hola";
            case "French" -> "Bonjour";
            case "German" -> "Hallo";
            case "Chinese" -> "你好";
            default -> "Hello";
        };
    }

    private String getFormalGreeting(String language) {
        return switch (language) {
            case "English" -> "Good morning, sir/madam";
            case "Spanish" -> "Buenos días, señor/señora";
            case "French" -> "Bonjour, monsieur/madame";
            case "German" -> "Guten Tag, Herr/Frau";
            case "Chinese" -> "您好";
            default -> "Good morning, sir/madam";
        };
    }

    private String getWritingDirection(String language) {
        return switch (language) {
            case "Chinese" -> "False";
            default -> "True";
        };
    }

    private String getBusinessIntro(String language) {
        return switch (language) {
            case "English" -> "I am pleased to meet you. My name is [Name] from [Company].";
            case "Spanish" -> "Es un placer conocerle. Me llamo [Nombre] de [Empresa].";
            case "French" -> "Je suis ravi(e) de vous rencontrer. Je m'appelle [Nom] de [Société].";
            case "German" -> "Es freut mich Sie kennenzulernen. Mein Name ist [Name] von [Firma].";
            case "Chinese" -> "很高兴见到您。我是[公司]的[姓名]。";
            default -> "I am pleased to meet you. My name is [Name] from [Company].";
        };
    }

    private String getNiceMeetYou(String language) {
        return switch (language) {
            case "English" -> "Nice to meet you";
            case "Spanish" -> "Encantado/a de conocerte";
            case "French" -> "Enchanté(e)";
            case "German" -> "Freut mich";
            case "Chinese" -> "很高兴认识你";
            default -> "Nice to meet you";
        };
    }
    
    public Users signUp(String name, String email, String password, Role role, double wallet, String bio) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Email and password cannot be empty");
        }
        
        // Check if email already exists
        for (Users user : users) {
            if (user.getEmail().equals(email)) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        
        Users user = switch (role) {
            case student -> new Student(name, email, password, wallet);
            case professor -> new Professor(name, email, password, bio);
            case admin -> new Admin(name, email, password);
        };
        users.add(user);
        
        
        if (role != Role.admin) {
            for (Users existingUser : users) {
                if (existingUser instanceof Admin) {
                    ((Admin) existingUser).addUser(user);
                }
            }
        }
        
        return user;
    }
    
    public Users login(String email, String password) throws InvalidAccess {
        if (email == null || password == null) {
            throw new InvalidAccess("Email and password cannot be null");
        }
        
        for (Users user : users) {
            
                if (user.login(email, password)) {
                    return user;
                }
        }
        throw new InvalidAccess("User not found");
    }
    
    public List<Users> getUsers() {
        return users;
    }
}


