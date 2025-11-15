import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// Tree Node for Department or Patient
class TreeNode {
    String name;
    boolean isDepartment; // true = department, false = patient
    ArrayList<TreeNode> children;
    // Patient-specific fields
    int age;
    String complaint;

    // Constructor for department
    public TreeNode(String name, boolean isDepartment) {
        this.name = name;
        this.isDepartment = isDepartment;
        children = new ArrayList<>();
    }

    // Constructor for patient
    public TreeNode(String name, int age, String complaint) {
        this.name = name;
        this.age = age;
        this.complaint = complaint;
        this.isDepartment = false;
        children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}

// Special Tree for Hospital
class SpecialTree {
    TreeNode root;

    public SpecialTree() {
        root = new TreeNode("Hospital", true);
        root.addChild(new TreeNode("General Medicine", true));
        root.addChild(new TreeNode("Pediatrics", true));
        root.addChild(new TreeNode("Surgery", true));
        root.addChild(new TreeNode("Cardiology", true));
    }

    // Find department node by name
    public TreeNode findDepartment(String deptName) {
        for (TreeNode dept : root.children) {
            if (dept.name.equalsIgnoreCase(deptName))
                return dept;
        }
        return null;
    }

    // Add patient to department
    public boolean addPatient(String deptName, String patientName, int age, String complaint) {
        TreeNode dept = findDepartment(deptName);
        if (dept != null) {
            dept.addChild(new TreeNode(patientName, age, complaint));
            return true;
        }
        return false;
    }

    // Serve (remove) first patient from department
    public String servePatient(String deptName) {
        TreeNode dept = findDepartment(deptName);
        if (dept != null && !dept.children.isEmpty()) {
            TreeNode patient = dept.children.remove(0);
            return "Served: " + patient.name + ", Age: " + patient.age + ", Complaint: " + patient.complaint;
        }
        return "No patient in " + deptName + " queue!";
    }

    // Display all patients in hierarchical order with waiting list
    public String displayTree() {
        StringBuilder sb = new StringBuilder();
        sb.append(root.name).append("\n");
        for (TreeNode dept : root.children) {
            sb.append(" Department: ").append(dept.name).append("\n");
            if (dept.children.isEmpty()) {
                sb.append("  No patients in queue.\n");
            } else {
                int i = 1;
                for (TreeNode patient : dept.children) {
                    sb.append("  ").append(i).append(". ").append(patient.name)
                      .append(", Age: ").append(patient.age)
                      .append(", Complaint: ").append(patient.complaint).append("\n");
                    i++;
                }
            }
        }
        return sb.toString();
    }
}

// GUI
public class HospitalWise extends JFrame {
    SpecialTree tree = new SpecialTree();
    JTextField deptField = new JTextField(12);
    JTextField patientField = new JTextField(12);
    JTextField ageField = new JTextField(5);
    JTextField complaintField = new JTextField(15);
    JTextArea outputArea = new JTextArea(20, 50);

    public HospitalWise() {
        setTitle("HospitalWise - Queue Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel title = new JLabel("Hospital Queue Management System");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton addBtn = new JButton("Add Patient");
        JButton serveBtn = new JButton("Serve Patient");
        JButton displayBtn = new JButton("Display Patients");

        add(title);
        add(new JLabel("Department:"));
        add(deptField);
        add(new JLabel("Patient Name:"));
        add(patientField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("Complaint:"));
        add(complaintField);
        add(addBtn);
        add(serveBtn);
        add(displayBtn);

        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        // Add patient button
        addBtn.addActionListener(e -> {
            String dept = deptField.getText().trim();
            String patient = patientField.getText().trim();
            int age;
            try {
                age = Integer.parseInt(ageField.getText().trim());
            } catch (Exception ex) {
                outputArea.setText("Invalid age!");
                return;
            }
            String complaint = complaintField.getText().trim();

            if (dept.isEmpty() || patient.isEmpty() || complaint.isEmpty()) {
                outputArea.setText("Please fill all fields!");
                return;
            }

            if (tree.addPatient(dept, patient, age, complaint)) {
                outputArea.setText("Patient added successfully!\n\n" + tree.displayTree());
            } else {
                outputArea.setText("Department not found!");
            }
        });

        // Serve patient button
        serveBtn.addActionListener(e -> {
            String dept = deptField.getText().trim();
            if (dept.isEmpty()) {
                outputArea.setText("Enter department name!");
                return;
            }
            outputArea.setText(tree.servePatient(dept) + "\n\n" + tree.displayTree());
        });

        // Display patients button
        displayBtn.addActionListener(e -> outputArea.setText(tree.displayTree()));

        setSize(650, 550);
        setVisible(true);
    }

    public static void main(String[] args) {
        new HospitalWise();
    }
}
