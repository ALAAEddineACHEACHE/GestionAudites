package Audit_management;

public class Systeme_management {
		private int id;
		 private String nom;  
	    private String description;       
	   private int nbr_personnes;  
	   private int organisation_id; 
	   public Systeme_management() {
		// TODO Auto-generated constructor stub
	}
	public Systeme_management(int id, String nom, String description, int nbr_personnes, int organisation_id) {
		super();
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.nbr_personnes = nbr_personnes;
		this.organisation_id = organisation_id;
	}
	public int getId() {
		return id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNbr_personnes() {
		return nbr_personnes;
	}
	public void setNbr_personnes(int nbr_personnes) {
		this.nbr_personnes = nbr_personnes;
	}
	public int getOrganisation_id() {
		return organisation_id;
	}
	public void setOrganisation_id(int organisation_id) {
		this.organisation_id = organisation_id;
	}
	   
		

}
