package Audit_management;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.*;

public class Panneau extends JPanel implements ActionListener {
    private JLabel lblLogin, lblPwd;
    private JTextField txtLogin;
    private JPasswordField txtPwd;
    private JButton btnCnx;

    public Panneau() {
        // Définir un layout pour centrer les composants
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants

        // Initialiser les composants
        lblLogin = new JLabel("Login: ");
        lblPwd = new JLabel("Password: ");
        txtLogin = new JTextField(15); // Largeur par défaut
        txtPwd = new JPasswordField(15);
        btnCnx = new JButton("Se connecter");

        // Ajouter des styles pour le design
        lblLogin.setForeground(new Color(0, 51, 102));
        lblPwd.setForeground(new Color(0, 51, 102));
        btnCnx.setBackground(new Color(51, 153, 255)); 
        btnCnx.setForeground(Color.WHITE); // Texte blanc sur le bouton

        // Ajouter les composants au panneau
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(lblLogin, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(lblPwd, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(txtPwd, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(btnCnx, gbc);

        // Ajouter un écouteur au bouton
        btnCnx.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	String login = txtLogin.getText();
        String pwd = new String(txtPwd.getPassword());

        try {
            // Fichiers contenant les identifiants
            File userFile = new File("users.txt");
            File adminFile = new File("admin.txt");

            // Créer les fichiers par défaut si nécessaires
            createDefaultFiles(userFile, adminFile);

            // Vérification dans les deux fichiers
            boolean isAdmin = authenticateUser(login, pwd, adminFile);
            boolean isUser = authenticateUser(login, pwd, userFile);

            if (isAdmin) {
                JOptionPane.showMessageDialog(this, "Bienvenue, Administrateur !", "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Ouvrir l'interface administrateur
                SwingUtilities.invokeLater(() -> new AdminInterface().setVisible(true));

                // Fermer la fenêtre actuelle
                SwingUtilities.getWindowAncestor(this).dispose();
            } else if (isUser) {
                JOptionPane.showMessageDialog(this, "Bienvenue, M.l'Auditeur !", "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Ouvrir une interface utilisateur
                SwingUtilities.invokeLater(() -> new AuditInterface().setVisible(true));

                // Fermer la fenêtre actuelle
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crée les fichiers par défaut si nécessaire avec des identifiants par défaut.
     */
    private void createDefaultFiles(File userFile, File adminFile) throws Exception {
        if (!adminFile.exists()) {
            try (PrintWriter writer = new PrintWriter(adminFile)) {
                writer.println("admin:admin"); // Identifiant par défaut
            }
        }

        if (!userFile.exists()) {
            try (PrintWriter writer = new PrintWriter(userFile)) {
                writer.println("alae:123"); // Identifiant par défaut
            }
        }
    }

    /**
     * Authentifie l'utilisateur en vérifiant ses identifiants dans un fichier.
     *
     * @param login      Le login de l'utilisateur.
     * @param pwd        Le mot de passe de l'utilisateur.
     * @param authFile   Le fichier contenant les identifiants.
     * @return           True si l'authentification est réussie, sinon false.
     */
    private boolean authenticateUser(String login, String pwd, File authFile) throws Exception {
        try (Scanner scanner = new Scanner(authFile)) {
            while (scanner.hasNextLine()) {
                String[] credentials = scanner.nextLine().split(":");
                if (credentials.length == 2 && credentials[0].equals(login) && credentials[1].equals(pwd)) {
                    return true;
                }
            }
        }
        return false;
    }
}
