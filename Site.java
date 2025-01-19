package Audit_management;

import java.util.ArrayList;
import java.util.List;

public class Site {
	private int id;
    private String nom;                     
    private String adresse;                 
    private String telephone;               
    private String organisationAssociee;   

    // Constructeur
	public Site(int id, String nom, String adresse, String telephone, String organisationAssociee) {
		super();
		this.id = id;
		this.nom = nom;
		this.adresse = adresse;
		this.telephone = telephone;
		this.organisationAssociee = organisationAssociee;
	}


	public String getNom() {
        return nom;
    }


	public int getId() {
		return id;
	}
	public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

   

	public String getOrganisationAssociee() {
		return organisationAssociee;
	}


	public void setOrganisationAssociee(String organisationAssociee) {
		this.organisationAssociee = organisationAssociee;
	}


	@Override
	public String toString() {
		return "Site [id=" + id + ", nom=" + nom + ", adresse=" + adresse + ", telephone=" + telephone
				+ ", organisationAssociee=" + organisationAssociee + "]";
	}


	


   

	
}
