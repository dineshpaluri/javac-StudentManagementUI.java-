import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Student implements Serializable {
    int rollNo;
    String name;
    int marks;

    Student(int rollNo, String name, int marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
    }

    public String toString() {
        return rollNo + " - " + name + " - " + marks;
    }
}

public class StudentManagementUI {

    private ArrayList<Student> students = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private final String FILE_NAME = "students.dat";

    public StudentManagementUI() {
        JFrame frame = new JFrame("Advanced Student Management System");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("Student Management Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(title, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 3, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Details"));

        JTextField rollField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField marksField = new JTextField();

        inputPanel.add(new JLabel("Roll No:"));
        inputPanel.add(rollField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Marks:"));
        inputPanel.add(marksField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");

        inputPanel.add(addBtn);
        inputPanel.add(updateBtn);

        // List Panel
        JList<String> studentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));

        // Bottom Panel
        JPanel bottomPanel = new JPanel();

        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        bottomPanel.add(deleteBtn);
        bottomPanel.add(searchBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);

        // Add Action
        addBtn.addActionListener(e -> {
            try {
                int roll = Integer.parseInt(rollField.getText());
                String name = nameField.getText();
                int marks = Integer.parseInt(marksField.getText());

                for (Student s : students) {
                    if (s.rollNo == roll) {
                        JOptionPane.showMessageDialog(frame, "Duplicate Roll No!");
                        return;
                    }
                }

                Student s = new Student(roll, name, marks);
                students.add(s);
                listModel.addElement(s.toString());

                rollField.setText("");
                nameField.setText("");
                marksField.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input!");
            }
        });

        // Delete Action
        deleteBtn.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index != -1) {
                students.remove(index);
                listModel.remove(index);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a student!");
            }
        });

        // Update Action
        updateBtn.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index != -1) {
                try {
                    int roll = Integer.parseInt(rollField.getText());
                    String name = nameField.getText();
                    int marks = Integer.parseInt(marksField.getText());

                    Student s = new Student(roll, name, marks);
                    students.set(index, s);
                    listModel.set(index, s.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Input!");
                }
            }
        });

        // Search Action
        searchBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter Roll No:");
            try {
                int roll = Integer.parseInt(input);
                for (Student s : students) {
                    if (s.rollNo == roll) {
                        JOptionPane.showMessageDialog(frame, s.toString());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Not Found!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input!");
            }
        });

        // Save to File
        saveBtn.addActionListener(e -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(students);
                JOptionPane.showMessageDialog(frame, "Saved Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error Saving!");
            }
        });

        // Load from File
        loadBtn.addActionListener(e -> {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                students = (ArrayList<Student>) ois.readObject();
                listModel.clear();
                for (Student s : students) {
                    listModel.addElement(s.toString());
                }
                JOptionPane.showMessageDialog(frame, "Loaded Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "No Data Found!");
            }
        });

        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementUI::new);
    }
}
