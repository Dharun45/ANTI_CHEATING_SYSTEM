// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AntiCheatingSystem {
   static String loggedUser = "";
   static final String GOOGLE_FORM_URL = "https://forms.gle/C5TBxb5nbUH8PWke6";

   public AntiCheatingSystem() {
   }

   public static void main(String[] var0) {
      SwingUtilities.invokeLater(LoginFrame::new);
   }

   static class ExamFrame extends JFrame {
      List<Question> questions = new ArrayList();
      int index = 0;
      int score = 0;
      String examId;
      String startTime;
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
      DateTimeFormatter idFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      JLabel questionLabel;
      JRadioButton[] options = new JRadioButton[4];
      ButtonGroup group = new ButtonGroup();

      ExamFrame() {
         this.setTitle("Online Exam");
         this.setSize(600, 300);
         this.setLayout(new BorderLayout());
         this.setDefaultCloseOperation(3);
         this.examId = "EXAM-" + LocalDateTime.now().format(this.idFormatter);
         this.startTime = LocalDateTime.now().format(this.timeFormatter);
         this.loadQuestions();
         this.questionLabel = new JLabel();
         this.questionLabel.setFont(new Font("Arial", 1, 14));
         this.add(this.questionLabel, "North");
         JPanel var1 = new JPanel(new GridLayout(4, 1));

         for(int var2 = 0; var2 < 4; ++var2) {
            this.options[var2] = new JRadioButton();
            this.group.add(this.options[var2]);
            var1.add(this.options[var2]);
         }

         this.add(var1, "Center");
         JButton var5 = new JButton("Next");
         JButton var3 = new JButton("Submit");
         var5.addActionListener((var1x) -> {
            this.next();
         });
         var3.addActionListener((var1x) -> {
            this.submit();
         });
         JPanel var4 = new JPanel();
         var4.add(var5);
         var4.add(var3);
         this.add(var4, "South");
         this.addWindowFocusListener(new WindowFocusListener() {
            {
               Objects.requireNonNull(ExamFrame.this);
            }

            public void windowLostFocus(WindowEvent var1) {
               AntiCheatingSystem.AntiCheat.warn(ExamFrame.this);
            }

            public void windowGainedFocus(WindowEvent var1) {
            }
         });
         this.showQuestion();
         this.setLocationRelativeTo((Component)null);
         this.setVisible(true);
      }

      void loadQuestions() {
         try {
            Scanner var1 = new Scanner(new File("questions.txt"));

            try {
               while(var1.hasNextLine()) {
                  String[] var2 = var1.nextLine().split("\\|");
                  this.questions.add(new Question(var2[1], new String[]{var2[2], var2[3], var2[4], var2[5]}, Integer.parseInt(var2[6])));
               }
            } catch (Throwable var5) {
               try {
                  var1.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            var1.close();
         } catch (Exception var6) {
            JOptionPane.showMessageDialog(this, "questions.txt not found");
            System.exit(0);
         }

      }

      void showQuestion() {
         if (this.index < this.questions.size()) {
            Question var1 = (Question)this.questions.get(this.index);
            int var10001 = this.index + 1;
            this.questionLabel.setText("Q" + var10001 + ": " + var1.text);
            this.group.clearSelection();

            for(int var2 = 0; var2 < 4; ++var2) {
               this.options[var2].setText(var1.options[var2]);
            }

         }
      }

      void next() {
         this.checkAnswer();
         ++this.index;
         if (this.index < this.questions.size()) {
            this.showQuestion();
         } else {
            this.submit();
         }

      }

      void checkAnswer() {
         Question var1 = (Question)this.questions.get(this.index);

         for(int var2 = 0; var2 < 4; ++var2) {
            if (this.options[var2].isSelected() && var2 + 1 == var1.correct) {
               ++this.score;
            }
         }

      }

      void submit() {
         String var1 = LocalDateTime.now().format(this.timeFormatter);
         String var2 = "USER-" + LocalDateTime.now().format(this.idFormatter);

         try {
            FileWriter var3 = new FileWriter("scores.txt", true);

            try {
               var3.write("Exam ID: " + this.examId + "\n");
               var3.write("User ID: " + var2 + "\n");
               var3.write("Username: " + AntiCheatingSystem.loggedUser + "\n");
               var3.write("Start Time: " + this.startTime + "\n");
               var3.write("End Time: " + var1 + "\n");
               var3.write("Score: " + this.score + "\n");
               var3.write("Total Questions: " + this.questions.size() + "\n");
               var3.write("Warnings: " + AntiCheatingSystem.AntiCheat.warnings + "\n");
               var3.write("--------------------------------\n");
            } catch (Throwable var8) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var3.close();
         } catch (Exception var9) {
            System.out.println("Error saving score");
         }

         String var10001 = this.examId;
         JOptionPane.showMessageDialog(this, "Exam ID: " + var10001 + "\nUser ID: " + var2 + "\nUsername: " + AntiCheatingSystem.loggedUser + "\nStart Time: " + this.startTime + "\nEnd Time: " + var1 + "\nScore: " + this.score + "\nTotal Questions: " + this.questions.size() + "\nWarnings: " + AntiCheatingSystem.AntiCheat.warnings, "Exam Result", 1);

         try {
            Desktop.getDesktop().browse(new URI("https://forms.gle/C5TBxb5nbUH8PWke6"));
         } catch (Exception var6) {
            System.out.println("Could not open Google Form");
         }

         System.exit(0);
      }
   }

   static class AntiCheat {
      static int warnings = 0;

      AntiCheat() {
      }

      static void warn(JFrame var0) {
         ++warnings;
         JOptionPane.showMessageDialog(var0, "Warning " + warnings + ": Window switched");
         if (warnings >= 3) {
            JOptionPane.showMessageDialog(var0, "Exam auto-submitted due to cheating");
            System.exit(0);
         }

      }
   }

   static class Question {
      String text;
      String[] options;
      int correct;

      Question(String var1, String[] var2, int var3) {
         this.text = var1;
         this.options = var2;
         this.correct = var3;
      }
   }

   static class LoginFrame extends JFrame {
      JTextField usernameField;
      JPasswordField passwordField;

      LoginFrame() {
         this.setTitle("Online Exam Login");
         this.setSize(300, 170);
         this.setLayout(new GridLayout(3, 2, 10, 10));
         this.setDefaultCloseOperation(3);
         this.add(new JLabel("Username:"));
         this.usernameField = new JTextField();
         this.add(this.usernameField);
         this.add(new JLabel("Password:"));
         this.passwordField = new JPasswordField();
         this.add(this.passwordField);
         JButton var1 = new JButton("Login");
         this.add(new JLabel());
         this.add(var1);
         var1.addActionListener((var1x) -> {
            this.authenticate();
         });
         this.setLocationRelativeTo((Component)null);
         this.setVisible(true);
      }

      void authenticate() {
         String var1 = this.usernameField.getText().trim();
         String var2 = (new String(this.passwordField.getPassword())).trim();

         try {
            Scanner var3 = new Scanner(new File("users.txt"));

            label60: {
               try {
                  while(var3.hasNextLine()) {
                     String[] var4 = var3.nextLine().split(",");
                     if (var4[0].equals(var1) && var4[1].equals(var2)) {
                        AntiCheatingSystem.loggedUser = var1;
                        JOptionPane.showMessageDialog(this, "Login Successful");
                        this.dispose();
                        new ExamFrame();
                        break label60;
                     }
                  }

                  JOptionPane.showMessageDialog(this, "Invalid Username or Password");
               } catch (Throwable var7) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }

                  throw var7;
               }

               var3.close();
               return;
            }

            var3.close();
         } catch (Exception var8) {
            JOptionPane.showMessageDialog(this, "users.txt not found");
         }
      }
   }
}
