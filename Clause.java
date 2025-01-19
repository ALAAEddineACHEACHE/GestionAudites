package Audit_management;

public class Clause {
	private int id;
    private String description;       
    private String standardAssociee;
	public Clause(int id, String description, String standardAssociee) {
		super();
		this.id = id;
		this.description = description;
		this.standardAssociee = standardAssociee;
	}
	public Clause() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStandardAssociee() {
		return standardAssociee;
	}
	public void setStandardAssociee(String standardAssociee) {
		this.standardAssociee = standardAssociee;
	}
	@Override
	public String toString() {
		return "Clause [id=" + id + ", description=" + description + ", standardAssociee=" + standardAssociee + "]";
	}
    
    
}
