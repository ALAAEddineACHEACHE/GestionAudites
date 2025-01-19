package Audit_management;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionProcessus extends JFrame {
    private List<Processus> processusList = new ArrayList<>();
    private int nextId = 1;

    public GestionProcessus() {
        setTitle("Gestion des Processus");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 240, 250));

        // Charger les processus depuis le fichier
        loadProcessusFromFile();

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu processusMenu = new JMenu("Processus");
        JMenuItem addProcessusItem = new JMenuItem("Ajouter Processus");
        JMenuItem listProcessusItem = new JMenuItem("Lister Processus");

        processusMenu.add(addProcessusItem);
        processusMenu.add(listProcessusItem);
        menuBar.add(processusMenu);
        setJMenuBar(menuBar);

        // Actions du menu
        addProcessusItem.addActionListener(e -> openAddProcessusWindow());
        listProcessusItem.addActionListener(e -> openListProcessusWindow());
    }

    private void loadProcessusFromFile() {
        processusList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("processus.txt"))) {
        	/*
        	 * Lire un fichier caractère par caractère ou ligne par ligne directement depuis un flux comme FileReader peut être lent.
BufferedReader lit un bloc de données en une seule opération et le place dans un tampon,
 ce qui réduit le nombre d'appels I/O.
 Réduction de la surcharge I/O :

Les tampons permettent de réduire les appels à des opérations d'entrée/sortie,
 ce qui améliore les performances globales.
        	 */
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0].trim());
                    String nom = parts[1].trim();
                    String description = parts[2].trim();
                    String siteassociee = parts[3].trim();
                    processusList.add(new Processus(id, nom, description, siteassociee));
                    nextId = Math.max(nextId, id + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier processus.txt non trouvé. Un nouveau fichier sera créé.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des processus : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveProcessusToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("processus.txt"))) {
        	/*
        	 * FileWriter écrit directement sur le disque à chaque appel de méthode comme write(), 
        	 * ce qui peut être lent si on effectue de nombreuses écritures.
BufferedWriter utilise un tampon pour stocker temporairement les données et n'écrit sur le disque qu'en blocs
 ou lorsque le tampon est plein. Cela réduit le nombre d'opérations I/O coûteuses.
        	 */
            for (Processus processus : processusList) {
                writer.write(processus.getId() + "," + processus.getNom() + "," + processus.getDescription() + "," + processus.getSiteassociee());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddProcessusWindow() {
        JFrame addProcessusFrame = new JFrame("Ajouter Processus");
        addProcessusFrame.setSize(400, 300);
        addProcessusFrame.setLocationRelativeTo(null);
        addProcessusFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        JLabel lblDescription = new JLabel("Description:");
        JTextField txtDescription = new JTextField(15);
        JLabel lblSite = new JLabel("Site associé:");
        JTextField txtSite = new JTextField(15);
        JButton btnSave = new JButton("Enregistrer");

        gbc.gridx = 0;
        gbc.gridy = 0;
        addProcessusFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        addProcessusFrame.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addProcessusFrame.add(lblDescription, gbc);
        gbc.gridx = 1;
        addProcessusFrame.add(txtDescription, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addProcessusFrame.add(lblSite, gbc);
        gbc.gridx = 1;
        addProcessusFrame.add(txtSite, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addProcessusFrame.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String description = txtDescription.getText().trim();
            String site = txtSite.getText().trim();

            if (!nom.isEmpty() && !description.isEmpty() && !site.isEmpty()) {
                processusList.add(new Processus(nextId++, nom, description, site));
                saveProcessusToFile();
                JOptionPane.showMessageDialog(addProcessusFrame, "Processus ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                addProcessusFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(addProcessusFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        addProcessusFrame.setVisible(true);
    }

    private void openListProcessusWindow() {
        JFrame listProcessusFrame = new JFrame("Liste des Processus");
        listProcessusFrame.setSize(800, 400);
        listProcessusFrame.setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Description", "Site associé", "ActionEdit", "ActionDelete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 4) {
                    c.setForeground(Color.BLUE);
                } else if (column == 5) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        updateTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        listProcessusFrame.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (column == 4) {
                    editProcessus(row, tableModel);
                } else if (column == 5) {
                    deleteProcessus(row, tableModel);
                }
            }
        });

        listProcessusFrame.setVisible(true);
    }

    private void updateTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Processus processus : processusList) {
            tableModel.addRow(new Object[]{processus.getId(), processus.getNom(), processus.getDescription(), processus.getSiteassociee(), "Modifier", "Supprimer"});
        }
    }

    private void editProcessus(int row, DefaultTableModel tableModel) {
        Processus processus = processusList.get(row);

        JFrame editProcessusFrame = new JFrame("Modifier Processus");
        editProcessusFrame.setSize(400, 400);
        editProcessusFrame.setLocationRelativeTo(null);
        editProcessusFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblNom = new JLabel("Nom:");
        JTextField txtNom = new JTextField(15);
        txtNom.setText(processus.getNom());

        JLabel lblDescription = new JLabel("Description:");
        JTextField txtDescription = new JTextField(15);
        txtDescription.setText(processus.getDescription());

        JLabel lblSite = new JLabel("Site associé:");
        JTextField txtSite = new JTextField(15);
        txtSite.setText(processus.getSiteassociee());

        JButton btnUpdate = new JButton("Mettre à jour");

        gbc.gridx = 0;
        gbc.gridy = 0;
        editProcessusFrame.add(lblNom, gbc);
        gbc.gridx = 1;
        editProcessusFrame.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editProcessusFrame.add(lblDescription, gbc);
        gbc.gridx = 1;
        editProcessusFrame.add(txtDescription, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editProcessusFrame.add(lblSite, gbc);
        gbc.gridx = 1;
        editProcessusFrame.add(txtSite, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editProcessusFrame.add(btnUpdate, gbc);

        btnUpdate.addActionListener(e -> {
            processus.setNom(txtNom.getText().trim());
            processus.setDescription(txtDescription.getText().trim());
            processus.setSiteassociee(txtSite.getText().trim());

            saveProcessusToFile();
            loadProcessusFromFile();
            updateTable(tableModel);
            JOptionPane.showMessageDialog(editProcessusFrame, "Processus mis à jour avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            editProcessusFrame.dispose();
        });

        editProcessusFrame.setVisible(true);
    }

    private void deleteProcessus(int row, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ce processus ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            processusList.remove(row);
            tableModel.removeRow(row);
            saveProcessusToFile();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionProcessus().setVisible(true));
    }
}
