package btssio;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Reservation {
    private int id;
    private Client client;
    private Salle salle;
    private String dateReservation;
    private int periode;

    public Reservation(int id, Client client, Salle salle, Date dateReservation, int periode) {
        this.id = id;
        this.client = client;
        this.salle = salle;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Format de date
        this.dateReservation = sdf.format(dateReservation); // Formater la date au format souhaité
        this.periode = periode;
    }
    
    public static void main(String[] args) {
	}
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }
    
    public int getPeriode() {
        return periode;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", client=" + client + ", salle=" + salle + ", dateReservation="
                + dateReservation + ", periode=" + periode + "]";
    }
}
