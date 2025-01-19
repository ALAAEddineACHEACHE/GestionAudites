package Audit_management;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GestionOrganisation extends JFrame {
    private List<organisation> organisations = new ArrayList<>(); // Liste pour stocker les organisations
    private static AtomicInteger idCounter = new AtomicInteger(1); // Compteur pour générer des IDs uniques

    public GestionOrganisation() {
        setTitle("Gestion des Organisations");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 250));

        // Charger les organisations depuis le fichier
        loadOrganisationsFromFile();

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu organisationMenu = new JMenu("Organisation");

        // Création des sous-menus
        JMenuItem addOrganisationItem = new JMenuItem("Ajouter Organisation");
        JMenuItem listOrganisationItem = new JMenuItem("Lister Organisations");

        // Ajout des sous-menus au menu principal
        organisationMenu.add(addOrganisationItem);
        organisationMenu.add(listOrganisationItem);
        menuBar.add(organisationMenu);

        setJMenuBar(menuBar);

        // Action pour ajouter une organisation
        addOrganisationItem.addActionListener(e -> openAddOrganisationWindow());
        /* e : Représente l'événement déclenché (une instance de ActionEvent).
-> : Séparateur entre les paramètres et le corps de la fonction.
openAddOrganisationWindow() : Méthode appelée lorsque l'événement se produit, 
c'est-à-dire lorsqu'on clique sur addOrganisationItem.
*/
        // Action pour lister les organisations
        listOrganisationItem.addActionListener(e -> openListOrganisationWindow());
    }
    /*
     * addOrganisationItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        openAddOrganisationWindow();
    }
});

listOrganisationItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        openListOrganisationWindow();
    }
});

     */

    private void loadOrganisationsFromFile() {
        organisations.clear(); // Vider la liste pour éviter les doublons

        try (BufferedReader reader = new BufferedReader(new FileReader("organisations.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) { // Vérifier que la ligne contient bien tous les champs
                    int id = Integer.parseInt(parts[0].trim());
                    String nom = parts[1].trim();
                    String adresse = parts[2].trim();
                    String telephone = parts[3].trim();
                    organisations.add(new organisation(id, nom, adresse, telephone));
                    idCounter.set(Math.max(idCounter.get(), id + 1)); // Assurer que l'ID est unique
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier organisations.txt non trouvé. Un nouveau fichier sera créé.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des organisations : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveOrganisationsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("organisations.txt"))) {
            for (organisation organisation : organisations) {
                writer.write(organisation.getId() + ";" + organisation.getNom() + ";" + organisation.getAdresse() + ";" + organisation.getTelephone());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddOrganisationWindow() {
        JFrame addOrganisationFrame = new JFrame("Ajouter Organisation");
        addOrganisationFrame.setSize(400, 300);
        addOrganisationFrame.setLocationRelativeTo(null);
        addOrganisationFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        JLabel lblAdresse = new JLabel("Adresse:");
        JTextField txtAdresse = new JTextField(15);
        JLabel lblTelephone = new JLabel("Téléphone:");
        JTextField txtTelephone = new JTextField(15);
        JButton btnSave = new JButton("Enregistrer");

        gbc.gridx = 0;
        gbc.gridy = 0;
        addOrganisationFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        addOrganisationFrame.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addOrganisationFrame.add(lblAdresse, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        addOrganisationFrame.add(txtAdresse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addOrganisationFrame.add(lblTelephone, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        addOrganisationFrame.add(txtTelephone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addOrganisationFrame.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String telephone = txtTelephone.getText().trim();

            if (!nom.isEmpty() && !adresse.isEmpty() && !telephone.isEmpty()) {
                int id = idCounter.getAndIncrement();
                organisations.add(new organisation(id, nom, adresse, telephone));
                saveOrganisationsToFile();
                JOptionPane.showMessageDialog(addOrganisationFrame, "Organisation ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                addOrganisationFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addOrganisationFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        addOrganisationFrame.setVisible(true);
    }

    private void openListOrganisationWindow() {
        JFrame listOrganisationFrame = new JFrame("Liste des Organisations");
        listOrganisationFrame.setSize(600, 400);
        listOrganisationFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Adresse", "Téléphone", "ActionEdit", "ActionDelete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Ajouter les données au tableau
        for (organisation organisation : organisations) {
            tableModel.addRow(new Object[]{
                organisation.getId(),
                organisation.getNom(),
                organisation.getAdresse(),
                organisation.getTelephone(),
                "Modifier",
                "Supprimer"
            });
        }

        // Rendu des colonnes Modifier et Supprimer
        table.getColumn("ActionEdit").setCellRenderer(new ButtonColorRenderer(Color.GREEN));
        table.getColumn("ActionDelete").setCellRenderer(new ButtonColorRenderer(Color.RED));

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 4) { // Modifier
                    editOrganisation(row, table);
                } else if (column == 5) { // Supprimer
                    deleteOrganisation(row, tableModel);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        listOrganisationFrame.add(scrollPane, BorderLayout.CENTER);
        listOrganisationFrame.setVisible(true);
    }

    private void editOrganisation(int row, JTable table) {
        organisation organisation = organisations.get(row);

        JFrame editOrganisationFrame = new JFrame("Modifier Organisation");
        editOrganisationFrame.setSize(400, 300);
        editOrganisationFrame.setLocationRelativeTo(null);
        editOrganisationFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        txtNom.setText(organisation.getNom());

        JLabel lblAdresse = new JLabel("Adresse:");
        JTextField txtAdresse = new JTextField(15);
        txtAdresse.setText(organisation.getAdresse());

        JLabel lblTelephone = new JLabel("Téléphone:");
        JTextField txtTelephone = new JTextField(15);
        txtTelephone.setText(organisation.getTelephone());

        JButton btnUpdate = new JButton("Mettre à jour");

        gbc.gridx = 0;
        gbc.gridy = 0;
        editOrganisationFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        editOrganisationFrame.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editOrganisationFrame.add(lblAdresse, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        editOrganisationFrame.add(txtAdresse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editOrganisationFrame.add(lblTelephone, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        editOrganisationFrame.add(txtTelephone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editOrganisationFrame.add(btnUpdate, gbc);

        btnUpdate.addActionListener(e -> {
            organisation.setNom(txtNom.getText().trim());
            organisation.setAdresse(txtAdresse.getText().trim());
            organisation.setTelephone(txtTelephone.getText().trim());

            saveOrganisationsToFile();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setValueAt(organisation.getNom(), row, 1);
            tableModel.setValueAt(organisation.getAdresse(), row, 2);
            tableModel.setValueAt(organisation.getTelephone(), row, 3);

            JOptionPane.showMessageDialog(editOrganisationFrame, "Organisation mise à jour avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            editOrganisationFrame.dispose();
        });

        editOrganisationFrame.setVisible(true);
    }

    private void deleteOrganisation(int row, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer cette organisation ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            organisations.remove(row);
            tableModel.removeRow(row);
            saveOrganisationsToFile();
            JOptionPane.showMessageDialog(null, "Organisation supprimée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Rendu personnalisé pour les boutons
    class ButtonColorRenderer extends DefaultTableCellRenderer {
        private final Color color;

        public ButtonColorRenderer(Color color) {
            this.color = color;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton(value.toString());
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            return button;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionOrganisation frame = new GestionOrganisation();
            frame.setVisible(true);
        });
    }
}
