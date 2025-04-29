/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

//Student Name: Jacob Braddock
//Program Name: File Encryptor
//Creation Date: December 20th, 2024
//Last Modified Date: April 19th, 225
//CSCI Course:  499
//Grade Received:  100
//Design Comments: This is a file encryptor that is used to encrypt and decrypt files
package seniorproject;

/**
 *
 * @author jacobbraddock
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileEncryptorGUI extends JFrame {

    private JTextField fileField;
    private JTextField destField;
    private JPasswordField passwordField;
    private File[] selectedFiles;
    private File destinationFolder = new File(System.getProperty("user.home"));
    private boolean isSpanish = false;

    public FileEncryptorGUI() {
        setTitle("File Encryptor/Decryptor");
        setSize(600, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        // File selector
        JPanel filePanel = new JPanel(new BorderLayout());
        fileField = new JTextField();
        JButton browseButton = new JButton("Browse");
        filePanel.add(fileField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        add(filePanel);

        // Password
        JPanel passPanel = new JPanel(new BorderLayout());
        passwordField = new JPasswordField();
        JLabel passwordLabel = new JLabel("Password:");
        passPanel.add(passwordLabel, BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);
        JToggleButton toggleVisibility = new JToggleButton("Show");
        toggleVisibility.addActionListener(e -> {
            if (toggleVisibility.isSelected()) {
                passwordField.setEchoChar((char) 0);
                toggleVisibility.setText("Hide");
            } else {
                passwordField.setEchoChar('\u2022');
                toggleVisibility.setText("Show");
            }
        });
        passPanel.add(toggleVisibility, BorderLayout.EAST);
        add(passPanel);

        // Destination folder
        JPanel destPanel = new JPanel(new BorderLayout());
        destField = new JTextField(destinationFolder.getAbsolutePath());
        JButton destBrowseButton = new JButton("Browse Destination");
        destPanel.add(destField, BorderLayout.CENTER);
        destPanel.add(destBrowseButton, BorderLayout.EAST);
        add(destPanel);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        JToggleButton langToggle = new JToggleButton("Español");
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(langToggle);
        add(buttonPanel);

        // Status label
        JLabel statusLabel = new JLabel("Status: Ready");
        add(statusLabel);

        // File browse logic
        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFiles = chooser.getSelectedFiles();
                StringBuilder names = new StringBuilder();
                for (File file : selectedFiles) {
                    names.append(file.getName()).append("; ");
                }
                fileField.setText(names.toString());
            }
        });

        // Destination browse logic
        destBrowseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                destinationFolder = chooser.getSelectedFile();
                destField.setText(destinationFolder.getAbsolutePath());
            }
        });

        // Encrypt logic
        encryptButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if (selectedFiles != null && selectedFiles.length > 0 && !password.isEmpty()) {
                StringBuilder resultMsg = new StringBuilder(isSpanish ? "Encriptado: " : "Encrypted: ");
                for (File file : selectedFiles) {
                    try {
                        EncryptorUtils.encrypt(file, password, destinationFolder);
                        resultMsg.append(file.getName()).append("; ");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        resultMsg.append("[FAILED: ").append(file.getName()).append("] ");
                    }
                }
                statusLabel.setText((isSpanish ? "Estado: " : "Status: ") + resultMsg.toString());
            } else {
                statusLabel.setText(isSpanish ? "Estado: Seleccione archivos e ingrese contraseña." : "Status: Select files and enter password.");
            }
        });

        // Decrypt logic
        decryptButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if (selectedFiles != null && selectedFiles.length > 0 && !password.isEmpty()) {
                StringBuilder resultMsg = new StringBuilder(isSpanish ? "Desencriptado: " : "Decrypted: ");
                for (File file : selectedFiles) {
                    try {
                        EncryptorUtils.decrypt(file, password, destinationFolder);
                        resultMsg.append(file.getName()).append("; ");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        resultMsg.append("[FAILED: ").append(file.getName()).append("] ");
                    }
                }
                statusLabel.setText((isSpanish ? "Estado: " : "Status: ") + resultMsg.toString());
            } else {
                statusLabel.setText(isSpanish ? "Estado: Seleccione archivos e ingrese contraseña." : "Status: Select files and enter password.");
            }
        });

        // Language toggle
        langToggle.addActionListener(e -> {
            isSpanish = !isSpanish;
            langToggle.setText(isSpanish ? "English" : "Español");
            setTitle(isSpanish ? "Encriptador/Desencriptador de Archivos" : "File Encryptor/Decryptor");
            encryptButton.setText(isSpanish ? "Encriptar" : "Encrypt");
            decryptButton.setText(isSpanish ? "Desencriptar" : "Decrypt");
            passwordLabel.setText(isSpanish ? "Contraseña:" : "Password:");
            browseButton.setText(isSpanish ? "Explorar" : "Browse");
            destBrowseButton.setText(isSpanish ? "Destino" : "Browse Destination");
            statusLabel.setText(isSpanish ? "Estado: Listo" : "Status: Ready");
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileEncryptorGUI::new);
    }
}
