package Audit_management;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Définition de la classe principale héritant de JFrame
public class AuditInterface extends JFrame {

    private JTable auditTable; // Tableau pour afficher les audits
    private DefaultTableModel tableModel; // Modèle de table pour gérer les données dynamiquement
    private JButton sendMessageButton; // Bouton pour envoyer un message à l'admin

    // Constructeur : Initialise l'interface utilisateur
    public AuditInterface() {
        setTitle("Auditeur Interface");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 250));
        setLayout(new BorderLayout());

        // Label de bienvenue
        JLabel welcomeLabel = new JLabel("Bienvenue dans l'interface de l'auditeur!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        // Définition de la classe interne pour afficher un bouton dans la colonne de validation
        class ButtonRenderer extends JButton implements TableCellRenderer {
            public ButtonRenderer() {
                setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                setText("Valider");
                return this;
            }
        }

        // Classe interne pour gérer l'action sur le bouton de validation
        class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
            private JButton button;
            private boolean clicked;
            private int currentRow;

            public ButtonEditor(JCheckBox checkBox) {
                button = new JButton("Valider");
                button.setOpaque(true);

                // Action lorsqu'on clique sur le bouton
                button.addActionListener(e -> {
                    clicked = true;
                    fireEditingStopped();
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                currentRow = row;
                clicked = true;
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                if (clicked) {
                    // Met à jour le statut à "Terminé" lorsqu'on clique sur Valider
                    tableModel.setValueAt("Terminé", currentRow, 4);
                    JOptionPane.showMessageDialog(button, "Clause validée!");
                }
                clicked = false;
                return "Valider";
            }
        }

        // Définition des colonnes du tableau
        String[] columnNames = {"ID", "Nom de Standard", "Clause", "Date", "Statut", "Validation"};
        tableModel = new DefaultTableModel(columnNames, 0);
        auditTable = new JTable(tableModel);

        // Ajout des boutons dans la colonne "Validation"
        TableColumn actionColumn = auditTable.getColumnModel().getColumn(5);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        // Ajout du tableau dans la fenêtre avec un panneau de défilement
        JScrollPane scrollPane = new JScrollPane(auditTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton pour envoyer un message à l'admin
        sendMessageButton = new JButton("Validation du standard et envoi à l'admin");
        sendMessageButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendMessageButton.setBackground(new Color(70, 130, 180));
        sendMessageButton.setForeground(Color.WHITE);

        // Action du bouton "Envoyer un message"
        sendMessageButton.addActionListener(e -> sendMessageToAdmin());
        add(sendMessageButton, BorderLayout.SOUTH);

        // Chargement des données depuis les fichiers standards et clauses
        loadAuditData();
    }

    /**
     * Charge les données depuis les fichiers standards et clauses.
     */
    private void loadAuditData() {
        try {
            ArrayList<String> standards = loadStandards("standards.txt");
            Map<String, ArrayList<String>> clausesMap = loadClauses("clauses.txt");

            int id = 1;
            for (String standard : standards) {
                ArrayList<String> clauses = clausesMap.getOrDefault(standard, new ArrayList<>());
                for (String clause : clauses) {
                    tableModel.addRow(new Object[]{id, standard, clause, "2025-01-21", "En cours", "Valider"});
                    id++;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données : " + e.getMessage());
        }
    }

    /**
     * Méthode pour charger les standards depuis un fichier texte.
     */
    private ArrayList<String> loadStandards(String fileName) throws IOException {
        ArrayList<String> standards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    standards.add(parts[1].trim());
                }
            }
        }
        return standards;
    }

    /**
     * Méthode pour charger les clauses depuis un fichier texte.
     */
    private Map<String, ArrayList<String>> loadClauses(String fileName) throws IOException {
        Map<String, ArrayList<String>> clausesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String standard = parts[2].trim();
                    String clause = parts[1].trim();
                    clausesMap.putIfAbsent(standard, new ArrayList<>());
                    clausesMap.get(standard).add(clause);
                }
            }
        }
        return clausesMap;
    }

    /**
     * Méthode pour envoyer un message à l'admin lorsqu'un standard est validé.
     */
    private void sendMessageToAdmin() {
        try (FileWriter writer = new FileWriter("notifications.txt", true)) {
            writer.write("L'auditeur a validé le standard!\n");
            JOptionPane.showMessageDialog(this, "Message envoyé à l'admin.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuditInterface auditInterface = new AuditInterface();
            auditInterface.setVisible(true);
        });
    }
}
