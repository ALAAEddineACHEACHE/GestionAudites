package Audit_management;

public class Processus {
	private int id;
    private String nom;                    
    private String description;              
    private String siteassociee;  
	public Processus(int id, String nom, String description, String siteassociee) {
		super();
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.siteassociee =siteassociee;
	}
	public Processus() {
		super();
		// TODO Auto-generated constructor stub
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
	public String getSiteassociee() {
		return siteassociee;
	}
	public void setSiteassociee(String siteassociee) {
		this.siteassociee = siteassociee;
	}
	@Override
	public String toString() {
		return "Processus [id=" + id + ", nom=" + nom + ", description=" + description + ",siteassociee="
				+ siteassociee + "]";
	}

    
    
}
