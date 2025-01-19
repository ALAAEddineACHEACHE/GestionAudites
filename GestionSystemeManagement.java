package Audit_management;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Audit_management.GestionClauses.ButtonColorRenderer;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GestionSystemeManagement extends JFrame {
    private List<Systeme_management> systemes = new ArrayList<>();
    private static AtomicInteger idCounter = new AtomicInteger(1);

    public GestionSystemeManagement() {
        setTitle("Gestion des Systèmes de Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 250));

        loadSystemesFromFile();

        JMenuBar menuBar = new JMenuBar();
        JMenu systemeMenu = new JMenu("Système de Management");

        JMenuItem addSystemeItem = new JMenuItem("Ajouter Système");
        JMenuItem listSystemeItem = new JMenuItem("Lister Systèmes");

        systemeMenu.add(addSystemeItem);
        systemeMenu.add(listSystemeItem);
        menuBar.add(systemeMenu);

        setJMenuBar(menuBar);

        addSystemeItem.addActionListener(e -> openAddSystemeWindow());
        listSystemeItem.addActionListener(e -> openListSystemeWindow());
    }



	private void loadSystemesFromFile() {
        systemes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("systemes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0].trim());
                    String nom = parts[1].trim();
                    String description = parts[2].trim();
                    int nbrPersonnes = Integer.parseInt(parts[3].trim());
                    int organisationId = Integer.parseInt(parts[4].trim());
                    systemes.add(new Systeme_management(id, nom, description, nbrPersonnes, organisationId));
                    idCounter.set(Math.max(idCounter.get(), id + 1));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des systèmes : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSystemesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("systemes.txt"))) {
            for (Systeme_management systeme : systemes) {
                writer.write(systeme.getId() + ";" + systeme.getNom() + ";" + systeme.getDescription() + ";" + systeme.getNbr_personnes() + ";" + systeme.getOrganisation_id());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void openListSystemeWindow() {
        JFrame listSystemeFrame = new JFrame("Liste des Systèmes de Management");
        listSystemeFrame.setSize(600, 400);
        listSystemeFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Description", "Nbr Personnes", "Organisation ID", "Modifier", "Supprimer"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        for (Systeme_management systeme : systemes) {
            tableModel.addRow(new Object[]{
                systeme.getId(),
                systeme.getNom(),
                systeme.getDescription(),
                systeme.getNbr_personnes(),
                systeme.getOrganisation_id(),
                "Modifier",
                "Supprimer"
            });
        }

  

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 5) {
                    editSysteme(row, table);
                } else if (column == 6) {
                    editSysteme(row, tableModel);
                }
            }

			private void editSysteme(int row, DefaultTableModel tableModel) {
				// TODO Auto-generated method stub
				
			}

			private void editSysteme(int row, JTable table) {
				// TODO Auto-generated method stub
				
			}
        });

        JScrollPane scrollPane = new JScrollPane(table);
        listSystemeFrame.add(scrollPane, BorderLayout.CENTER);
        listSystemeFrame.setVisible(true);
    }
    private void openAddSystemeWindow() {
        JFrame addSystemeFrame = new JFrame("Ajouter Système");
        addSystemeFrame.setSize(400, 300);
        addSystemeFrame.setLocationRelativeTo(null);
        addSystemeFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        JLabel lblDescription = new JLabel("Description:");
        JTextField txtDescription = new JTextField(15);
        JLabel lblNbrPersonnes = new JLabel("Nombre de Personnes:");
        JTextField txtNbrPersonnes = new JTextField(15);
        JLabel lblOrganisationId = new JLabel("ID de l'Organisation:");
        JTextField txtOrganisationId = new JTextField(15);
        JButton btnSave = new JButton("Enregistrer");

        gbc.gridx = 0; gbc.gridy = 0;
        addSystemeFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        addSystemeFrame.add(txtNom, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        addSystemeFrame.add(lblDescription, gbc);
        gbc.gridx = 1;
        addSystemeFrame.add(txtDescription, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        addSystemeFrame.add(lblNbrPersonnes, gbc);
        gbc.gridx = 1;
        addSystemeFrame.add(txtNbrPersonnes, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        addSystemeFrame.add(lblOrganisationId, gbc);
        gbc.gridx = 1;
        addSystemeFrame.add(txtOrganisationId, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addSystemeFrame.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            try {
                String nom = txtNom.getText().trim();
                String description = txtDescription.getText().trim();
                int nbrPersonnes = Integer.parseInt(txtNbrPersonnes.getText().trim());
                int organisationId = Integer.parseInt(txtOrganisationId.getText().trim());

                if (!nom.isEmpty() && !description.isEmpty()) {
                    systemes.add(new Systeme_management(idCounter.getAndIncrement(), nom, description, nbrPersonnes, organisationId));
                    saveSystemesToFile();
                    JOptionPane.showMessageDialog(addSystemeFrame, "Système ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    addSystemeFrame.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addSystemeFrame, "Erreur de format des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        addSystemeFrame.setVisible(true);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionSystemeManagement().setVisible(true));
    }
}
