# ANTI CHEATING SYSTEM
The Online Exam Anti-Cheating System is a Java-based application that allows users to take online exams securely. Users log in, answer multiple-choice questions, and their actions are monitored. Window switching is detected to prevent cheating, and the final score is displayed automatically.

This project aims to improve exam integrity and provide a fair evaluation environment.

## How the Project Works

1. **User Login Window**
   - Built using Java Swing forms.
   - Validates user credentials before starting the exam.

2. **Camera Monitoring**
   - Webcam captures live frames.
   - Detects:
     - Multiple faces
     - No face presence
     - Head turning or unusual movement

3. **System Activity Monitoring**
   - Detects window switching or minimizing.
   - Tracks keyboard and mouse irregularities.

4. **Event Handling**
   - Implemented using **ActionListener**, **KeyListener**, and **WindowListener**.
   - Every suspicious action is recorded.

5. **Admin Panel**
   - Displays logs and cheating alerts.
   - Allows monitoring of all participants.

## Features

- Java Swing GUI Interface
- Real-Time Camera Monitoring
- Multiple Face Detection Alert
- Window Switch Detection
- Activity Logging System
- Admin Control Panel
- Secure Login Authentication

## Technologies Used

- **Programming Language:** Java
- **GUI Framework:** Swing, AWT
- **Event Handling:** ActionListener, MouseListener, KeyListener
- **Database:** MySQL / SQLite
- **IDE:** IntelliJ / Eclipse

## Project Structure

Anti-Cheating-System/
│
├── src/
│ ├── login/ # Login UI and authentication
│ ├── dashboard/ # Admin & user panels
│ ├── monitoring/ # Camera & activity detection
│ ├── database/ # DB connection classes
│ └── utils/ # Helper utilities
│
├── assets/ # Images, icons
├── logs/ # Generated activity logs
├── lib/ # External JAR libraries
└── README.md

## How to Run the Project

# 1. Install Requirements
- Java JDK 8 or higher
- MySQL (if database is used)
- IDE (IntelliJ / Eclipse / NetBeans)

### 2. Clone or Download Project using this url : https://github.com/Dharun45/ANTI_CHEATING_SYSTEM

### 3. Open in IDE
- Open project folder.
- Ensure all **JAR libraries** are added to *Build Path*.

### 4. Configure Database
- Create database in MySQL.
- Update DB credentials in `DBConnection.java`.

### 5. Run the Application
- Navigate to `Main.java`.
- Right click → **Run as Java Application**

## Conclusion
The Java-based Anti-Cheating System provides a lightweight and efficient desktop solution for monitoring exam integrity. By using Swing GUI components and Java event handling mechanisms, the system ensures real-time detection and reporting of suspicious activities.
