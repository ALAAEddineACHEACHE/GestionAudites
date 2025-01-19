package Audit_management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Classe principale pour la gestion des standards héritant de JFrame
public class GestionStandards extends JFrame {
    private List<Standards> standards = new ArrayList<>(); // Liste des standards
    private int nextId = 1; // Compteur d'ID pour les standards ajoutés

    // Constructeur de la classe
    public GestionStandards() {
        setTitle("Gestion des Standards"); // Titre de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setSize(800, 600); 
        setLocationRelativeTo(null); // Centrer la fenêtre
        getContentPane().setBackground(new Color(230, 240, 250)); 

        // Charger les standards existants depuis le fichier
        loadStandardsFromFile();

        // Barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu standardMenu = new JMenu("Standard");
        JMenuItem addStandardItem = new JMenuItem("Ajouter Standard");
        JMenuItem listStandardItem = new JMenuItem("Lister Standards");

        // Ajout des actions dans le menu
        standardMenu.add(addStandardItem);
        standardMenu.add(listStandardItem);
        menuBar.add(standardMenu);
        setJMenuBar(menuBar);

        // Associer les actions aux boutons du menu
        addStandardItem.addActionListener(e -> openAddStandardWindow());
        listStandardItem.addActionListener(e -> openListStandardWindow());
    }

    /**
     * Charger les standards depuis un fichier texte
     */
    private void loadStandardsFromFile() {
        standards.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("standards.txt"))) {
            String line;
            nextId = 1; 
            while ((line = reader.readLine()) != null) { 
                String[] parts = line.split(","); // Séparation des données par virgule
                if (parts.length == 3) { 
                    int id = Integer.parseInt(parts[0].trim());
                    String nom = parts[1].trim();
                    String description = parts[2].trim();
                    standards.add(new Standards(id, nom, description));
                    nextId = Math.max(nextId, id + 1); // Calcul du prochain ID
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier standards.txt non trouvé.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + e.getMessage());
        }
    }

    /**
     * Sauvegarder les standards dans un fichier texte
     */
    private void saveStandardsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("standards.txt"))) {
            for (Standards standard : standards) {
                writer.write(standard.getId() + "," + standard.getNom() + "," + standard.getDescription());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur de sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Fenêtre d'ajout d'un standard
     */
    private void openAddStandardWindow() {
        JFrame addStandardFrame = new JFrame("Ajouter Standard");
        addStandardFrame.setSize(400, 300);
        addStandardFrame.setLocationRelativeTo(null);
        addStandardFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        JLabel lblDescription = new JLabel("Description:");
        JTextField txtDescription = new JTextField(15);
        JButton btnSave = new JButton("Enregistrer");

        // Placement des composants dans la fenêtre
        gbc.gridx = 0; gbc.gridy = 0;
        addStandardFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        addStandardFrame.add(txtNom, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addStandardFrame.add(lblDescription, gbc);
        gbc.gridx = 1;
        addStandardFrame.add(txtDescription, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        addStandardFrame.add(btnSave, gbc);

        // Ajout d'un listener sur le bouton
        btnSave.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String description = txtDescription.getText().trim();
            if (!nom.isEmpty() && !description.isEmpty()) {
                standards.add(new Standards(nextId++, nom, description));
                saveStandardsToFile();
                JOptionPane.showMessageDialog(addStandardFrame, "Standard ajouté !");
                addStandardFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addStandardFrame, "Veuillez remplir tous les champs.");
            }
        });

        addStandardFrame.setVisible(true);
    }

    /**
     * Fenêtre pour lister les standards et proposer des actions
     */
    private void openListStandardWindow() {
        JFrame listStandardFrame = new JFrame("Liste des Standards");
        listStandardFrame.setSize(600, 400);
        listStandardFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Description", "Modifier", "Supprimer"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        updateTable(tableModel); 

        JScrollPane scrollPane = new JScrollPane(table);
        listStandardFrame.add(scrollPane, BorderLayout.CENTER);

        // Gestion des actions (modifier/supprimer)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 3) {
                    editStandard(row, tableModel);
                } else if (column == 4) {
                    deleteStandard(row, tableModel);
                }
            }
        });

        listStandardFrame.setVisible(true);
    }

    /**
     * Met à jour la table des standards affichés
     */
    private void updateTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Standards standard : standards) {
            tableModel.addRow(new Object[]{standard.getId(), standard.getNom(), standard.getDescription(), "Modifier", "Supprimer"});
        }
    }

    /**
     * Fenêtre de modification d'un standard
     */
    private void editStandard(int row, DefaultTableModel tableModel) {
        Standards standard = standards.get(row);

        JFrame editStandardFrame = new JFrame("Modifier Standard");
        editStandardFrame.setSize(400, 300);
        editStandardFrame.setLocationRelativeTo(null);
        editStandardFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        txtNom.setText(standard.getNom());

        JLabel lblDescription = new JLabel("Description:");
        JTextField txtDescription = new JTextField(15);
        txtDescription.setText(standard.getDescription());

        JButton btnUpdate = new JButton("Mettre à jour");

        btnUpdate.addActionListener(e -> {
            standard.setNom(txtNom.getText().trim());
            standard.setDescription(txtDescription.getText().trim());
            saveStandardsToFile();
            updateTable(tableModel);
            JOptionPane.showMessageDialog(editStandardFrame, "Standard modifié !");
            editStandardFrame.dispose();
        });

        editStandardFrame.setVisible(true);
    }

    /**
     * Supprimer un standard
     */
    private void deleteStandard(int row, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous supprimer ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            standards.remove(row);
            saveStandardsToFile();
            updateTable(tableModel);
        }
    }

    // Point d'entrée principal
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionStandards().setVisible(true));
    }
}
