package Audit_management;

import javax.swing.*;

import Audit_management.Panneau;

public class LoginWindow extends JFrame {
	//cette fenetre son attribut est panneau parce que dans ce cadre on va n 7oto panneau
		private JPanel pan;
		public LoginWindow() {
			pan = new Panneau();
			setTitle("Authentification dans Gestion_Audits");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(800, 600); 
			  setLocationRelativeTo(null); 
			getContentPane().add(pan);

}
}
