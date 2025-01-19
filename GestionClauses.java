package Audit_management;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe GestionClauses pour gérer les clauses dans une interface graphique Swing
 */
public class GestionClauses extends JFrame {

    // Liste pour stocker les clauses et compteur d'ID unique
    private List<Clause> clauses = new ArrayList<>();
    private static AtomicInteger idCounter = new AtomicInteger(1);

    /**
     * Constructeur de la classe : initialise l'interface et charge les données
     */
    public GestionClauses() {
        setTitle("Gestion des Clauses");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 250));

        // Charger les clauses existantes depuis le fichier
        loadClausesFromFile();

        // Création de la barre de menu et des options
        JMenuBar menuBar = new JMenuBar();
        JMenu clauseMenu = new JMenu("Clause");
        JMenuItem addClauseItem = new JMenuItem("Ajouter Clause");
        JMenuItem listClauseItem = new JMenuItem("Lister Clauses");

        // Ajout des items au menu principal
        clauseMenu.add(addClauseItem);
        clauseMenu.add(listClauseItem);
        menuBar.add(clauseMenu);
        setJMenuBar(menuBar);

        // Définir les actions des boutons
        addClauseItem.addActionListener(e -> openAddClauseWindow());
        listClauseItem.addActionListener(e -> openListClauseWindow());
    }

    /**
     * Charger les clauses depuis un fichier texte
     */
    private void loadClausesFromFile() {
        clauses.clear(); // Vider la liste avant de recharger
        try (BufferedReader reader = new BufferedReader(new FileReader("clauses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) { 
                    int id = Integer.parseInt(parts[0].trim());
                    String description = parts[1].trim();
                    String standardAssociee = parts[2].trim();
                    clauses.add(new Clause(id, description, standardAssociee));
                    idCounter.set(Math.max(idCounter.get(), id + 1));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des clauses : " + e.getMessage());
        }
    }

    /**
     * Sauvegarder les clauses dans un fichier texte
     */
    private void saveClausesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clauses.txt"))) {
            for (Clause clause : clauses) {
                writer.write(clause.getId() + ";" + clause.getDescription() + ";" + clause.getStandardAssociee());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur de sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Ouvre une fenêtre pour ajouter une nouvelle clause
     */
    private void openAddClauseWindow() {
        List<String> standards = List.of("ISO 9001", "ISO 14001", "ISO 27001");

        JFrame addClauseFrame = new JFrame("Ajouter Clause");
        addClauseFrame.setSize(400, 300);
        addClauseFrame.setLocationRelativeTo(null);
        addClauseFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblClause = new JLabel("Clause:");
        JTextField txtClause = new JTextField(15);
        JLabel lblStandardAssociee = new JLabel("Standard Associé:");
        JComboBox<String> comboBoxStandards = new JComboBox<>(standards.toArray(new String[0]));
        JButton btnSave = new JButton("Enregistrer");

        // Positionnement des composants dans la fenêtre
        gbc.gridx = 0; gbc.gridy = 0;
        addClauseFrame.add(lblClause, gbc);
        gbc.gridx = 1;
        addClauseFrame.add(txtClause, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        addClauseFrame.add(lblStandardAssociee, gbc);
        gbc.gridx = 1;
        addClauseFrame.add(comboBoxStandards, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        addClauseFrame.add(btnSave, gbc);

        // Action du bouton "Enregistrer"
        btnSave.addActionListener(e -> {
            String clause = txtClause.getText().trim();
            String standard = (String) comboBoxStandards.getSelectedItem();
            if (!clause.isEmpty() && standard != null) {
                int id = idCounter.getAndIncrement();
                clauses.add(new Clause(id, clause, standard));
                saveClausesToFile();
                JOptionPane.showMessageDialog(addClauseFrame, "Clause ajoutée avec succès !");
                addClauseFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addClauseFrame, "Veuillez remplir tous les champs.");
            }
        });

        addClauseFrame.setVisible(true);
    }

    /**
     * Ouvre une fenêtre listant toutes les clauses
     */
    private void openListClauseWindow() {
        JFrame listClauseFrame = new JFrame("Liste des Clauses");
        listClauseFrame.setSize(600, 400);
        listClauseFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Clause", "Standard Associée", "Modifier", "Supprimer"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Ajout des données dans la table
        for (Clause clause : clauses) {
            tableModel.addRow(new Object[]{clause.getId(), clause.getDescription(), clause.getStandardAssociee(), "Modifier", "Supprimer"});
        }

        // Personnalisation des boutons Modifier et Supprimer
        table.getColumn("Modifier").setCellRenderer(new ButtonColorRenderer(Color.GREEN));
        table.getColumn("Supprimer").setCellRenderer(new ButtonColorRenderer(Color.RED));

        // Gestion des clics sur les boutons Modifier et Supprimer
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (column == 3) { 
                    editClause(row, table);
                } else if (column == 4) { 
                    deleteClause(row, tableModel);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        listClauseFrame.add(scrollPane, BorderLayout.CENTER);
        listClauseFrame.setVisible(true);
    }

    /**
     * Modifier une clause existante
     */
    private void editClause(int row, JTable table) {
        Clause clause = clauses.get(row);

        JFrame editClauseFrame = new JFrame("Modifier Clause");
        editClauseFrame.setSize(400, 300);
        editClauseFrame.setLocationRelativeTo(null);
        editClauseFrame.setLayout(new GridBagLayout());

        JTextField txtDescription = new JTextField(15);
        txtDescription.setText(clause.getDescription());

        JTextField txtStandard = new JTextField(15);
        txtStandard.setText(clause.getStandardAssociee());

        JButton btnUpdate = new JButton("Mettre à jour");

        btnUpdate.addActionListener(e -> {
            clause.setDescription(txtDescription.getText().trim());
            clause.setStandardAssociee(txtStandard.getText().trim());
            saveClausesToFile();
            JOptionPane.showMessageDialog(editClauseFrame, "Clause mise à jour !");
            editClauseFrame.dispose();
        });

        editClauseFrame.setVisible(true);
    }

    /**
     * Supprimer une clause
     */
    private void deleteClause(int row, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clauses.remove(row);
            tableModel.removeRow(row);
            saveClausesToFile();
        }
    }

    /**
     * Classe pour personnaliser les boutons dans la table
     */
    class ButtonColorRenderer extends DefaultTableCellRenderer {
        private final Color color;
        public ButtonColorRenderer(Color color) {
            this.color = color;
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton(value.toString());
            button.setBackground(color);
            return button;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionClauses().setVisible(true));
    }
}
