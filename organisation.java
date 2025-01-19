package Audit_management;

import java.util.List;

public class organisation {
	private int id;
    private String nom;
    private String adresse;
    private String telephone;
    private List<Site> sites; // Liste de sites associés
    //c'est de type list mais ses éléments sont des instances de la classe Site.

    // Constructeur
    public organisation(int id,String nom, String adresse, String telephone) {
        this.id=id;
    	this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    // Getters et setters
    
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
    

	public List<Site> getSites() {
		return sites;
	}
	/*  public void ajouterSite(Site site) {
	        sites.add(site);
	        site.setOrganisationAssociee(this); // Lien bidirectionnel
	    }

	    public void supprimerSite(Site site) {
	        sites.remove(site);
	        site.setOrganisation(null); // Supprimer l'association
	    }
	    */

	@Override
	public String toString() {
		return "organisation [id=" + id + ", nom=" + nom + ", adresse=" + adresse + ", telephone=" + telephone + "]";
	}

}
