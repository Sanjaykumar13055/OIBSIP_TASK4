import java.util.*;

public class OnlineExaminationSystem {
    private Map<String, User> users;
    private String loggedInUserId;
    private Scanner scanner;
    private Timer timer;
    private List<Question> questions;
    private Map<String, Integer> userAnswers;

    public OnlineExaminationSystem() {
        users = new HashMap<>();
        scanner = new Scanner(System.in);
        questions = new ArrayList<>();
        userAnswers = new HashMap<>();
        initializeQuestions();
    }

    private class User {
        String userId;
        String password;
        String name;

        public User(String userId, String password, String name) {
            this.userId = userId;
            this.password = password;
            this.name = name;
        }
    }

    private void registerUser(String userId, String password, String name) {
        if (!users.containsKey(userId)) {
            users.put(userId, new User(userId, password, name));
            System.out.println("Registration successful.");
        } else {
            System.out.println("User ID already exists. Please try again.");
        }
    }

    private void login() {
        System.out.print("Enter your User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();

        if (users.containsKey(userId) && users.get(userId).password.equals(password)) {
            System.out.println("Login successful.");
            loggedInUserId = userId;
        } else {
            System.out.println("Invalid User ID or Password. Try again.");
        }
    }

    private void updateProfile() {
        if (loggedInUserId == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter your new name: ");
        String newName = scanner.nextLine();
        users.get(loggedInUserId).name = newName;
        System.out.println("Profile updated successfully.");
    }

    private void updatePassword() {
        if (loggedInUserId == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();

        if (users.get(loggedInUserId).password.equals(currentPassword)) {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            users.get(loggedInUserId).password = newPassword;
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Incorrect current password. Password not updated.");
        }
    }

    private void startTimer(int minutes) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\nTime's up! Auto-submitting your answers.");
                autoSubmit();
            }
        }, minutes * 60 * 1000);
        System.out.println("Timer started for " + minutes + " minutes.");
    }

    private void initializeQuestions() {
        Question question1 = new Question("What is the capital of France?",
                Arrays.asList("1. London", "2. Berlin", "3. Paris", "4. Rome"), 2);
        Question question2 = new Question("What is 2 + 2?",
                Arrays.asList("1. 3", "2. 4", "3. 5", "4. 6"), 1);

        // Add more questions as needed
        // ...

        questions.add(question1);
        questions.add(question2);
    }

    private void displayQuestions() {
        int questionNumber = 1;
        for (Question question : questions) {
            System.out.println("Question " + questionNumber + ": " + question.getQuestionText());
            List<String> options = question.getOptions();
            for (String option : options) {
                System.out.println(option);
            }
            questionNumber++;
        }
    }

    private void answerMCQs() {
        if (loggedInUserId == null) {
            System.out.println("Please log in first.");
            return;
        }

        userAnswers.clear();
        for (int i = 0; i < questions.size(); i++) {
            displayQuestions();
            System.out.print("Enter your answer for Question " + (i + 1) + ": ");
            int selectedOption = Integer.parseInt(scanner.nextLine());
            userAnswers.put(loggedInUserId + "-Q" + (i + 1), selectedOption);
        }
        System.out.println("MCQs answered successfully.");
    }

    private void autoSubmit() {
        if (loggedInUserId == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.println("Auto-submitting your answers.");
        // Perform the submission of userAnswers here
        // ... (code for submitting answers)

        timer.cancel();
    }

    private void logout() {
        loggedInUserId = null;
        System.out.println("Logged out successfully.");
    }

    public void run() {
        while (true) {
            System.out.println("\nOnline Examination System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Update Profile");
            System.out.println("4. Update Password");
            System.out.println("5. Start MCQs");
            System.out.println("6. Logout");
            System.out.println("7. Exit");

            System.out.print("Enter your choice (1-7): ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter your User ID: ");
                    String userId = scanner.nextLine();
                    System.out.print("Enter your Password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    registerUser(userId, password, name);
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    updateProfile();
                    break;
                case 4:
                    updatePassword();
                    break;
                case 5:
                    System.out.print("Enter the duration of the exam (in minutes): ");
                    int duration = Integer.parseInt(scanner.nextLine());
                    answerMCQs();
                    startTimer(duration);
                    break;
                case 6:
                    logout();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    if (timer != null) {
                        timer.cancel();
                    }
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        OnlineExaminationSystem onlineExam = new OnlineExaminationSystem();
        onlineExam.run();
    }

    private class Question {
        private String questionText;
        private List<String> options;
        private int correctOption;

        public Question(String questionText, List<String> options, int correctOption) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getOptions() {
            return options;
        }

        public int getCorrectOption() {
            return correctOption;
        }
    }
}
