package Audit_management;

public class Standards {
	private int id;
	   private String nom;                  
	    private String description;     
	    // Constructeur
	    public Standards(int id,String nom, String description) {
	       this.id=id;
	    	this.nom = nom;
	        this.description = description;
	    }
	    public Standards() {
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
		@Override
		public String toString() {
			return "Standards [id=" + id + ", nom=" + nom + ", description=" + description + "]";
		}
		
	    

}
