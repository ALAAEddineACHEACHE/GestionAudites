package Audit_management;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

// Classe principale pour l'interface administrateur
public class AdminInterface extends JFrame {

    // Constructeur : Initialise l'interface utilisateur
    public AdminInterface() {
    	
        setTitle("Administrateur Interface"); // Définit le titre de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Définit la fermeture correcte de l'application
        setSize(800, 1000); // Dimensions de la fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
        getContentPane().setBackground(new Color(230, 240, 250)); // Définit une couleur de fond

        // Ajout d'un label de bienvenue
        JLabel label = new JLabel("Bienvenue dans l'interface de l'administrateur !");
        label.setHorizontalAlignment(SwingConstants.CENTER); // Centrage du texte
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Police et style
        add(label, BorderLayout.CENTER); // Placement au centre
        // Zone pour afficher les notifications
        JTextArea notificationsArea = new JTextArea(10, 50);
        notificationsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(notificationsArea);
        add(scrollPane, BorderLayout.CENTER);
        // Bouton OK pour revenir à l'écran d'accueil
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            getContentPane().removeAll();
            getContentPane().setBackground(new Color(230, 240, 250));
            getContentPane().add(label, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
        add(okButton, BorderLayout.SOUTH);
        // Charger les notifications depuis le fichier
        loadNotifications(notificationsArea);
   

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu auditMenu = new JMenu("Audit"); 
        JMenu menuOrganisation = new JMenu("Organisation");
        JMenu menuSite = new JMenu("Site");
        JMenu menuProcessus = new JMenu("Processus");
        JMenu menuStandards = new JMenu("Standards");
        JMenu menuClauses = new JMenu("Clauses");
        JMenu menuSystemes = new JMenu("Systèmes de management");
        // Ajout d'un item pour naviguer vers l'interface d'organisation
   
        JMenuItem organisationMenuItem = new JMenuItem("Gestion Organisation");
        JMenuItem SiteMenuItem = new JMenuItem("Gestion Site");
        JMenuItem ProcessusMenuItem = new JMenuItem("Gestion Processus");
        JMenuItem StandardsMenuItem = new JMenuItem("Gestion Standards");
        JMenuItem ClausesMenuItem = new JMenuItem("Gestion des clauses");
        JMenuItem SystemesMenuItem = new JMenuItem("Gestion des systèmes de management");
     

        menuOrganisation.add(organisationMenuItem);
       menuSite.add(SiteMenuItem);
       menuProcessus.add(ProcessusMenuItem);
       menuStandards.add(StandardsMenuItem);
       menuClauses.add(ClausesMenuItem);
       menuSystemes.add(SystemesMenuItem);
       
        // Création des sous-menus
        JMenuItem addAuditItem = new JMenuItem("Ajouter Auditeur");
        JMenuItem listAuditItem = new JMenuItem("Lister Auditeur");
  
        // Ajout des sous-menus au menu principal
        auditMenu.add(addAuditItem);
        auditMenu.add(listAuditItem);  
        // Ajout du menu à la barre de menu
        menuBar.add(auditMenu);
        menuBar.add(menuOrganisation);
        menuBar.add(menuSite);
        menuBar.add(menuProcessus);
        menuBar.add(menuStandards);
        menuBar.add(menuClauses);
        setJMenuBar(menuBar);
        menuBar.add(menuSystemes);
        setJMenuBar(menuBar);
        // Action pour ouvrir l'interface de gestion des organisations
        organisationMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer la fenêtre actuelle
                SwingUtilities.invokeLater(() -> {
                    GestionOrganisation gestionOrganisation = new GestionOrganisation();
                    gestionOrganisation.setVisible(true); // Ouvrir l'interface de gestion des organisations
                });
            }
        });
     // Action pour ouvrir l'interface de gestion des sites
       SiteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer la fenêtre actuelle
                SwingUtilities.invokeLater(() -> {
                  
                    GestionSite gestionSite= new GestionSite();
                    gestionSite.setVisible(true); // Ouvrir l'interface de gestion des organisations
                });
            }
        });
       //Action pour ouvrir l'interface de gestion des processus 
       ProcessusMenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose(); // Fermer la fenêtre actuelle
               SwingUtilities.invokeLater(() -> {
                 
                   GestionProcessus gestionProcessus= new GestionProcessus();
                   gestionProcessus.setVisible(true);
               });
           }
       });
       //Action pour ouvrir l'interface de standards
       StandardsMenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose(); // Fermer la fenêtre actuelle
               SwingUtilities.invokeLater(() -> {
                 
                   GestionStandards gestionStandards = new GestionStandards();
                   gestionStandards.setVisible(true); // Ouvrir l'interface de gestion des organisations
               });
           }
       });
       //Action pour ouvrir l'interface de clauses
       ClausesMenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose(); // Fermer la fenêtre actuelle
               SwingUtilities.invokeLater(() -> {
                 
                   GestionClauses gestionClause = new GestionClauses();
                   gestionClause.setVisible(true); // Ouvrir l'interface de gestion des organisations
               });
           }
       });
       //Action pour ouvrir l'interface de systemes
       SystemesMenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               dispose(); // Fermer la fenêtre actuelle
               SwingUtilities.invokeLater(() -> {
                 
                   GestionSystemeManagement GestionsystemeManagement = new GestionSystemeManagement();
                   GestionSystemeManagement gestionSystemeManagement2 = new GestionSystemeManagement();
				gestionSystemeManagement2.setVisible(true);
               });
           }
       });
        // Ajout des actions aux sous-menus
        addAuditItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddAuditWindow(); // Ouvre une nouvelle fenêtre pour ajouter un auditeur
            }
        });

        listAuditItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openListAuditWindow(); // Ouvre une fenêtre pour lister les auditeurs
            }
        });
    }

    // Méthode pour ouvrir la fenêtre "Ajouter un auditeur"
    private void openAddAuditWindow() {
        JFrame addAuditFrame = new JFrame("Ajouter Auditeur");
        addAuditFrame.setSize(400, 300);
        addAuditFrame.setLocationRelativeTo(null); // Centre la fenêtre
        addAuditFrame.setLayout(new GridBagLayout()); // Utilise un gestionnaire de disposition flexible

        // Configuration des contraintes pour le placement des composants
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Création des champs de saisie et des labels
        JLabel lblLogin = new JLabel("Login:");
        JTextField txtLogin = new JTextField(15); // Champ texte pour le login
        JLabel lblPassword = new JLabel("Mot de passe:");
        JPasswordField txtPassword = new JPasswordField(15); // Champ pour mot de passe
        JButton btnSave = new JButton("Enregistrer"); // Bouton d'enregistrement

        // Placement des composants dans la fenêtre
        gbc.gridx = 0; gbc.gridy = 0;
        addAuditFrame.add(lblLogin, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        addAuditFrame.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addAuditFrame.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        addAuditFrame.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addAuditFrame.add(btnSave, gbc);

        // Action pour le bouton "Enregistrer"
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = txtLogin.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                if (!login.isEmpty() && !password.isEmpty()) {
                    try (FileWriter writer = new FileWriter("users.txt", true)) {
                        writer.write(login + ":" + password + "\n");
                        JOptionPane.showMessageDialog(addAuditFrame, "Auditeur ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        addAuditFrame.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(addAuditFrame, "Erreur lors de l'ajout: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(addAuditFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        addAuditFrame.setVisible(true); // Affiche la fenêtre
    }

    // Méthode pour ouvrir la fenêtre "Lister les auditeurs"
    private void openListAuditWindow() {
        JFrame listAuditFrame = new JFrame("Liste des Auditeurs");
        listAuditFrame.setSize(600, 400);
        listAuditFrame.setLocationRelativeTo(null); // Centre la fenêtre
        listAuditFrame.setLayout(new BorderLayout());

        String[] columnNames = {"Login", "Mot de Passe", "Modifier", "Supprimer"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Ajout des boutons dans le tableau
        table.getColumn("Modifier").setCellRenderer(new ButtonRenderer());
        table.getColumn("Modifier").setCellEditor(new ButtonEditor(new JCheckBox()));

        table.getColumn("Supprimer").setCellRenderer(new ButtonRenderer());
        table.getColumn("Supprimer").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);

        // Lecture des données depuis le fichier
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    tableModel.addRow(new Object[]{parts[0], parts[1], "Modifier", "Supprimer"});
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(listAuditFrame, "Erreur lors de la lecture du fichier: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        listAuditFrame.add(scrollPane, BorderLayout.CENTER);
        listAuditFrame.setVisible(true);
    }


    // Classe interne pour le rendu des boutons dans le tableau
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private JTable currentTable; // Référence à la table
        private int currentRow;
        private int currentColumn;
        private AdminInterface parent;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            this.button = new JButton();
            this.button.setOpaque(true);
            this.parent = parent;

            // Ajout de l'action au clic sur le bouton
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();

                    if (currentTable == null) {
                        JOptionPane.showMessageDialog(parent, "Erreur : aucune table n'est associée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (label.equals("Modifier")) {
                        openEditWindow(currentTable, currentRow);
                    } else if (label.equals("Supprimer")) {
                        deleteUser(currentTable, currentRow);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentTable = table; // Capture de la référence de la table
            currentRow = row; // Capture de la ligne actuelle
            currentColumn = column; // Capture de la colonne actuelle

            label = (value == null) ? "" : value.toString(); // Récupération du texte du bouton
            button.setText(label); // Mise à jour du texte
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            // Retourne le texte du bouton pour s'assurer qu'il reste le même
            return label;
        }

        private void openEditWindow(JTable table, int row) {
            String currentLogin = (String) table.getValueAt(row, 0);
            String currentPassword = (String) table.getValueAt(row, 1);

            JFrame editFrame = new JFrame("Modifier Auditeur");
            editFrame.setSize(400, 300);
            editFrame.setLocationRelativeTo(null);
            editFrame.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel lblLogin = new JLabel("Login:");
            JTextField txtLogin = new JTextField(currentLogin, 15);
            JLabel lblPassword = new JLabel("Mot de passe:");
            JPasswordField txtPassword = new JPasswordField(currentPassword, 15);
            JButton btnSave = new JButton("Enregistrer");

            gbc.gridx = 0; gbc.gridy = 0;
            editFrame.add(lblLogin, gbc);

            gbc.gridx = 1; gbc.gridy = 0;
            editFrame.add(txtLogin, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            editFrame.add(lblPassword, gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            editFrame.add(txtPassword, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            editFrame.add(btnSave, gbc);

            btnSave.addActionListener(ev -> {
                String newLogin = txtLogin.getText().trim();
                String newPassword = new String(txtPassword.getPassword()).trim();

                if (!newLogin.isEmpty() && !newPassword.isEmpty()) {
                    try {
                        updateUserFile(currentLogin, newLogin, newPassword);
                        table.setValueAt(newLogin, row, 0);
                        table.setValueAt(newPassword, row, 1);

                        JOptionPane.showMessageDialog(editFrame, "Auditeur modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        editFrame.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(editFrame, "Erreur lors de la modification: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(editFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            });

            editFrame.setVisible(true);
        }

        private void deleteUser(JTable table, int row) {
            String loginToDelete = (String) table.getValueAt(row, 0);

            try {
                removeUserFromFile(loginToDelete);
                ((DefaultTableModel) table.getModel()).removeRow(row);

                JOptionPane.showMessageDialog(parent, "Auditeur supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void updateUserFile(String oldLogin, String newLogin, String newPassword) throws IOException {
            File file = new File("users.txt");
            File tempFile = new File("users_temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(file));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts[0].equals(oldLogin)) {
                        writer.write(newLogin + ":" + newPassword + "\n");
                    } else {
                        writer.write(line + "\n");
                    }
                }
            }

            if (!file.delete() || !tempFile.renameTo(file)) {
                throw new IOException("Impossible de mettre à jour le fichier.");
            }
        }

        private void removeUserFromFile(String login) throws IOException {
            File file = new File("users.txt");
            File tempFile = new File("users_temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(file));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (!parts[0].equals(login)) {
                        writer.write(line + "\n");
                    }
                }
            }

            if (!file.delete() || !tempFile.renameTo(file)) {
                throw new IOException("Impossible de supprimer l'utilisateur du fichier.");
            }
        }
    }
    private void notifyAuditorUpdate(String message) {
        try (FileWriter writer = new FileWriter("notifications.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour des notifications: " + ex.getMessage());
        }
    }
    // Méthode pour charger les notifications depuis le fichier
    private void loadNotifications(JTextArea notificationsArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader("notifications.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                notificationsArea.append(line + "\n");
            }
        } catch (IOException e) {
            notificationsArea.setText("Aucune notification disponible.");
        }
    }

    // Méthode main : Point d'entrée du programme
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminInterface frame = new AdminInterface();
            frame.setVisible(true);
        });
    }

}
