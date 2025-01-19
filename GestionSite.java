package Audit_management;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Classe principale pour la gestion des sites
public class GestionSite extends JFrame {
    private List<Site> sites = new ArrayList<>();
    private int nextId = 1;

    public GestionSite() {
        setTitle("Gestion des Sites");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 250));

        // Charger les sites depuis le fichier
        loadSitesFromFile();

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu siteMenu = new JMenu("Site");
        JMenuItem addSiteItem = new JMenuItem("Ajouter Site");
        JMenuItem listSiteItem = new JMenuItem("Lister Sites");

        siteMenu.add(addSiteItem);
        siteMenu.add(listSiteItem);
        menuBar.add(siteMenu);
        setJMenuBar(menuBar);

        // Actions du menu
        addSiteItem.addActionListener(e -> openAddSiteWindow());
        listSiteItem.addActionListener(e -> openListSiteWindow());
    }

    private void loadSitesFromFile() {
        sites.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("sites.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0].trim());
                    String nom = parts[1].trim();
                    String adresse = parts[2].trim();
                    String telephone = parts[3].trim();
                    String organisation = parts[4].trim();
                    sites.add(new Site(id, nom, adresse, telephone, organisation));
                    nextId = Math.max(nextId, id + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier sites.txt non trouvé. Un nouveau fichier sera créé.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des sites : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSitesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sites.txt"))) {
            for (Site site : sites) {
                writer.write(site.getId() + "," + site.getNom() + "," + site.getAdresse() + "," + site.getTelephone() + "," + site.getOrganisationAssociee());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddSiteWindow() {
        JFrame addSiteFrame = new JFrame("Ajouter Site");
        addSiteFrame.setSize(400, 300);
        addSiteFrame.setLocationRelativeTo(null);
        addSiteFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        JLabel lblAdresse = new JLabel("Adresse:");
        JTextField txtAdresse = new JTextField(15);
        JLabel lblTelephone = new JLabel("Téléphone:");
        JTextField txtTelephone = new JTextField(15);
        JLabel lblOrganisation = new JLabel("Organisation:");
        JTextField txtOrganisation = new JTextField(15);
        JButton btnSave = new JButton("Enregistrer");

        gbc.gridx = 0;
        gbc.gridy = 0;
        addSiteFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        addSiteFrame.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addSiteFrame.add(lblAdresse, gbc);
        gbc.gridx = 1;
        addSiteFrame.add(txtAdresse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addSiteFrame.add(lblTelephone, gbc);
        gbc.gridx = 1;
        addSiteFrame.add(txtTelephone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addSiteFrame.add(lblOrganisation, gbc);
        gbc.gridx = 1;
        addSiteFrame.add(txtOrganisation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addSiteFrame.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String telephone = txtTelephone.getText().trim();
            String organisation = txtOrganisation.getText().trim();

            if (!nom.isEmpty() && !adresse.isEmpty() && !telephone.isEmpty() && !organisation.isEmpty()) {
                sites.add(new Site(nextId++, nom, adresse, telephone, organisation));
                saveSitesToFile();
                JOptionPane.showMessageDialog(addSiteFrame, "Site ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                addSiteFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addSiteFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        addSiteFrame.setVisible(true);
    }

    private void openListSiteWindow() {
        JFrame listSiteFrame = new JFrame("Liste des Sites");
        listSiteFrame.setSize(800, 400);
        listSiteFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Adresse", "Téléphone", "Organisation", "ActionEdit", "ActionDelete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 5) {
                    c.setForeground(Color.BLUE);
                } else if (column == 6) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        updateTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        listSiteFrame.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 5) {
                    editSite(row, tableModel);
                } else if (column == 6) {
                    deleteSite(row, tableModel);
                }
            }
        });

        listSiteFrame.setVisible(true);
    }

    private void updateTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Site site : sites) {
            tableModel.addRow(new Object[]{site.getId(), site.getNom(), site.getAdresse(), site.getTelephone(), site.getOrganisationAssociee(), "Modifier", "Supprimer"});
        }
    }

    private void editSite(int row, DefaultTableModel tableModel) {
        Site site = sites.get(row);

        // Fenêtre de modification
        JFrame editSiteFrame = new JFrame("Modifier Site");
        editSiteFrame.setSize(400, 300);
        editSiteFrame.setLocationRelativeTo(null);
        editSiteFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Champs pré-remplis avec les données du site
        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(site.getNom(), 15);
        JLabel lblAdresse = new JLabel("Adresse:");
        JTextField txtAdresse = new JTextField(site.getAdresse(), 15);
        JLabel lblTelephone = new JLabel("Téléphone:");
        JTextField txtTelephone = new JTextField(site.getTelephone(), 15);
        JLabel lblOrganisation = new JLabel("Organisation:");
        JTextField txtOrganisation = new JTextField(15);
        JButton btnSave = new JButton("Enregistrer les modifications");

        gbc.gridx = 0;
        gbc.gridy = 0;
        editSiteFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        editSiteFrame.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editSiteFrame.add(lblAdresse, gbc);
        gbc.gridx = 1;
        editSiteFrame.add(txtAdresse, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editSiteFrame.add(lblTelephone, gbc);
        gbc.gridx = 1;
        editSiteFrame.add(txtTelephone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editSiteFrame.add(lblOrganisation, gbc);
        gbc.gridx = 1;
        editSiteFrame.add(txtOrganisation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editSiteFrame.add(btnSave, gbc);

        // Action sur le bouton "Enregistrer les modifications"
        btnSave.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String telephone = txtTelephone.getText().trim();
            String organisation = txtOrganisation.getText().trim();

            if (!nom.isEmpty() && !adresse.isEmpty() && !telephone.isEmpty() && !organisation.isEmpty()) {
                // Mettre à jour les données du site
                site.setNom(nom);
                site.setAdresse(adresse);
                site.setTelephone(telephone);
                site.setOrganisationAssociee(organisation);

                // Mettre à jour le fichier et le tableau
                saveSitesToFile();
                updateTable(tableModel);
                JOptionPane.showMessageDialog(editSiteFrame, "Site modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                editSiteFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(editSiteFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        editSiteFrame.setVisible(true);
    }


    private void deleteSite(int row, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            sites.remove(row);
            tableModel.removeRow(row);
            saveSitesToFile();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionSite().setVisible(true));
    }
}
