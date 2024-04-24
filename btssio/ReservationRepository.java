package btssio;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReservationRepository {
    public static List<Reservation> getToutesLesReservations(String filename)
            throws ParserConfigurationException, SAXException, IOException, ParseException {
        List<Reservation> lesReservations = new ArrayList<Reservation>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filename));
        
        NodeList nodeList = document.getElementsByTagName("reservation");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                int id = Integer.parseInt(elem.getAttribute("id"));
                String nom = getElementValue(elem, "nom");
                String prenom = getElementValue(elem, "prenom");
                int utilisateurId = Integer.parseInt(getElementValue(elem, "utilisateur_id"));
                int structureId = Integer.parseInt(getElementValue(elem, "structure_id"));
                String structureNom = getElementValue(elem, "structure_nom");
                String structureAdresse = getElementValue(elem, "structure_adresse");
                String mail = getElementValue(elem, "mail");
                int salleId = Integer.parseInt(getElementValue(elem, "salle_id"));
                Date date = dateFormat.parse(getElementValue(elem, "date"));
                int periode = Integer.parseInt(getElementValue(elem, "periode"));

                // Création de la réservation
                Client client = new Client(utilisateurId, nom + " " + prenom, mail, structureAdresse, structureNom);
                Salle salle = new Salle("Salle " + nom, salleId, 0.0); // Remplacez le prix par la valeur réelle
                Reservation Reservation = new Reservation(id, client, salle, date, periode);

                lesReservations.add(Reservation);
            }
        }
        return lesReservations;
    }
    
    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }


    public static void main(String[] args) {
    	
    }
}
