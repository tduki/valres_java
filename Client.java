package btssio;

import java.io.Serializable;

public class Client implements Serializable{

	// Attributs de la classe client
	private int id;
	private String nom;
	private String mail;
	private String adresse;
	private String nom_structure;	
	
	// Constructeur de la classe client
	
	public Client(int id, String nom, String mail, String adresse, String nom_structure) {
		super();
		this.id = id;
		this.nom = nom;
		this.mail = mail;
		this.adresse = adresse;
		this.nom_structure = nom_structure;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String toString() {
		return "Client [id=" + id + ", nom=" + nom + ", mail=" + mail + ", adresse=" + adresse + ", nom_structure="
				+ nom_structure + "]";
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getNom_structure() {
		return nom_structure;
	}


	public void setNom_structure(String nom_structure) {
		this.nom_structure = nom_structure;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


}
