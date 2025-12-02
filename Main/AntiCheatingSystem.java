import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AntiCheatingSystem{

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }

    // ================= LOGIN FRAME =================
    static class LoginFrame extends JFrame {

        JTextField usernameField;
        JPasswordField passwordField;

        LoginFrame() {
            setTitle("Online Exam Login");
            setSize(300, 170);
            setLayout(new GridLayout(3, 2, 10, 10));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            add(new JLabel("Username:"));
            usernameField = new JTextField();
            add(usernameField);

            add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            add(passwordField);

            JButton loginButton = new JButton("Login");
            add(new JLabel());
            add(loginButton);

            loginButton.addActionListener(e -> authenticate());

            setLocationRelativeTo(null);
            setVisible(true);
        }

        void authenticate() {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            try (Scanner sc = new Scanner(new File("users.txt"))) {
                while (sc.hasNextLine()) {
                    String[] data = sc.nextLine().split(",");
                    if (data.length >= 2 &&
                            data[0].trim().equals(user) &&
                            data[1].trim().equals(pass)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Login Successful",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        dispose();
                        new ExamFrame();
                        return;
                    }
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "users.txt not found",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // ================= QUESTION CLASS =================
    static class Question {
        String text;
        String[] options;
        int correct;

        Question(String t, String[] o, int c) {
            text = t;
            options = o;
            correct = c;
        }
    }

    // ================= ANTI CHEAT =================
    static class AntiCheat {
        static int warnings = 0;

        static void warn(JFrame frame) {
            warnings++;
            JOptionPane.showMessageDialog(
                    frame,
                    "Warning " + warnings + ": Window switched",
                    "Cheating Alert",
                    JOptionPane.WARNING_MESSAGE
            );

            if (warnings >= 3) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Exam auto-submitted due to cheating",
                        "Exam Over",
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(0);
            }
        }
    }

    // ================= EXAM FRAME =================
    static class ExamFrame extends JFrame {

        List<Question> questions = new ArrayList<>();
        int index = 0, score = 0;

        JLabel questionLabel;
        JRadioButton[] options = new JRadioButton[4];
        ButtonGroup group = new ButtonGroup();

        ExamFrame() {
            setTitle("Online Exam");
            setSize(600, 300);
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            loadQuestions();

            questionLabel = new JLabel();
            questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
            add(questionLabel, BorderLayout.NORTH);

            JPanel center = new JPanel(new GridLayout(4, 1));
            for (int i = 0; i < 4; i++) {
                options[i] = new JRadioButton();
                group.add(options[i]);
                center.add(options[i]);
            }
            add(center, BorderLayout.CENTER);

            JButton nextBtn = new JButton("Next");
            JButton submitBtn = new JButton("Submit");

            JPanel bottom = new JPanel();
            bottom.add(nextBtn);
            bottom.add(submitBtn);
            add(bottom, BorderLayout.SOUTH);

            nextBtn.addActionListener(e -> next());
            submitBtn.addActionListener(e -> submit());

            addWindowFocusListener(new WindowFocusListener() {
                public void windowLostFocus(WindowEvent e) {
                    AntiCheat.warn(ExamFrame.this);
                }
                public void windowGainedFocus(WindowEvent e) { }
            });

            showQuestion();
            setLocationRelativeTo(null);
            setVisible(true);
        }

        void loadQuestions() {
            try (Scanner sc = new Scanner(new File("questions.txt"))) {
                while (sc.hasNextLine()) {
                    String[] p = sc.nextLine().split("\\|");
                    if (p.length >= 7) {
                        questions.add(new Question(
                                p[1],
                                new String[]{p[2], p[3], p[4], p[5]},
                                Integer.parseInt(p[6])
                        ));
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "questions.txt not found",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        void showQuestion() {
            if (index >= questions.size()) return;

            Question q = questions.get(index);
            questionLabel.setText("Q" + (index + 1) + ": " + q.text);
            group.clearSelection();

            for (int i = 0; i < 4; i++) {
                options[i].setText(q.options[i]);
            }
        }

        void next() {
            checkAnswer();
            index++;
            if (index < questions.size()) {
                showQuestion();
            } else {
                submit();
            }
        }

        void checkAnswer() {
            Question q = questions.get(index);
            for (int i = 0; i < 4; i++) {
                if (options[i].isSelected() && i + 1 == q.correct) {
                    score++;
                }
            }
        }

        void submit() {
            JOptionPane.showMessageDialog(
                    this,
                    "Exam Finished\nScore: " + score + "/" + questions.size() +
                            "\nWarnings: " + AntiCheat.warnings,
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
            System.exit(0);
        }
    }
}
