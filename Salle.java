package btssio;

public class Salle {

    private String nom;
    private int id;
    private double prix;

    public Salle(String nom, int id, double prix) {
        super();
        if (id <= 5) {
            this.nom = "Salle de réunion";
            this.prix = 720.0 * 1.20; // Augmentation de 20%
        } else if (id >= 10) {
            this.nom = "Amphithéâtre";
            this.prix = 1200.0 * 1.20; // Augmentation de 20%
        } else {
            this.nom = "Salle avec équipements";
            this.prix = 960.0 * 1.20; // Augmentation de 20%
        }
        this.id = id;
    }

    public static void main(String[] args) {
        // Créez des instances de salle
        Salle salle1 = new Salle("Salle de réunion", 5, 720.0);
        Salle salle2 = new Salle("Amphithéâtre", 10, 1200.0);
        Salle salle3 = new Salle("Salle avec équipements", 7, 960.0);

        // Affichez les informations sur les salles avec l'augmentation de prix déjà appliquée
        System.out.println(salle1);
        System.out.println(salle2);
        System.out.println(salle3);
    }

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	@Override
	public String toString() {
		return "Salle [nom=" + nom + ", id=" + id + ", prix=" + prix + "]";
	}

    // ... (le reste de votre code)
}
