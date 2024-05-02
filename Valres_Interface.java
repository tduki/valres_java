package btssio;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Valres_Interface extends JFrame {
    private JPanel contentPane;
    private String xmlFilename;
    private static int numeroBordereau = 1;

    public Valres_Interface() {
        setTitle("Valres Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 150);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20)); // marge
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(1, 2, 10, 10)); // style des boutons

        JButton btnChargerXML = new JButton("Charger XML");
        btnChargerXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    xmlFilename = selectedFile.getAbsolutePath();
                    JOptionPane.showMessageDialog(null, "XML chargé avec succès.");
                }
            }
        });
        contentPane.add(btnChargerXML);

        JButton btnGenererPDF = new JButton("Générer PDF");
        btnGenererPDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (xmlFilename != null) {
                    try {
                        List<Reservation> lesReservations = ReservationRepository.getToutesLesReservations(xmlFilename);
                        List<Client> lesClients = ClientRepository.getTousLesClients("C:\\Users\\hamni\\Documents\\APP-Valres-main\\utilisateur.xml");

                        genererBordereauPDF(lesReservations, lesClients);
                        JOptionPane.showMessageDialog(null, "Bordereau PDF généré avec succès.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors de la génération du PDF.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez charger un fichier XML d'abord.");
                }
            }
        });
        contentPane.add(btnGenererPDF);

        
                    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Valres_Interface frame = new Valres_Interface();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void genererBordereauPDF(List<Reservation> reservations, List<Client> clients) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\hamni\\Documents\\APP-Valres-main\\Helvetica.ttf"));
            contentStream.setFont(font, 12);

            float currentY = 700;
            float lineHeight = 15;
            String textPeriode;
            String previousClientName = null;

            contentStream.beginText();
            contentStream.newLineAtOffset(50, currentY);

            LocalDate dateEmission = LocalDate.now();

            contentStream.setFont(font, 18);
            contentStream.showText("Maison des ligues");
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Bordereau n° " + numeroBordereau);
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Date d'émission: " + dateEmission.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Semaine " + obtenirNumeroSemaine(dateEmission));
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.setFont(font, 12);

            Map<String, Double> totalAPayerParClient = new HashMap<>();

            for (Reservation reservation : reservations) {
                Client client = reservation.getClient();
                Salle salle = reservation.getSalle();
                int periode = reservation.getPeriode();
                switch (periode) {
                    case 1:
                        textPeriode = "Matinée";
                        break;
                    case 2:
                        textPeriode = "Après-midi";
                        break;
                    case 3:
                        textPeriode = "Soirée";
                        break;
                    default:
                        textPeriode = "Période non-définie";
                }

                double prix;
                boolean clientExiste = false;
                for (Client c : clients) {
                    if (c.getId() == client.getId()) {
                        clientExiste = true;
                        break;
                    }
                }

                if (clientExiste) {
                    if (client instanceof Client_Niveau3) {
                        prix = salle.getPrix() * 0.80;
                    } else if (client instanceof Client_Niveau2) {
                        prix = salle.getPrix() * 0.9;
                    } else {
                        prix = salle.getPrix();
                    }
                } else {
                    prix = 0.0;
                }

                if (previousClientName == null || !previousClientName.equals(client.getNom())) {
                    if (previousClientName != null) {
                        double totalAPayer = totalAPayerParClient.get(previousClientName);
                        contentStream.showText("Total à payer pour " + previousClientName + ": " + totalAPayer + " euros.");
                        currentY -= lineHeight;
                        contentStream.newLineAtOffset(0, -lineHeight);
                    }

                    totalAPayerParClient.put(client.getNom(), 0.0);

                    contentStream.showText(client.getNom());
                    currentY -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText(client.getMail());
                    currentY -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText(client.getAdresse());
                    currentY -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText(client.getNom_structure());
                    currentY -= lineHeight;
                    previousClientName = client.getNom();
                }

                String montant = clientExiste ? String.valueOf(prix) : "-";

                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Nom salle: " + salle.getNom());
                currentY -= lineHeight;
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Période: " + textPeriode);
                currentY -= lineHeight;
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Date: " + reservation.getDateReservation());
                currentY -= lineHeight;
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("Montant TTC: " + montant + " euros");
                currentY -= lineHeight;
                contentStream.newLineAtOffset(0, -lineHeight);
                contentStream.showText("-------------------------------");

                double totalAPayer = totalAPayerParClient.get(client.getNom());
                totalAPayer += prix;
                totalAPayerParClient.put(client.getNom(), totalAPayer);
            }

            if (previousClientName != null) {
                double totalAPayer = totalAPayerParClient.get(previousClientName);
                contentStream.showText("Total à facturer pour " + previousClientName + " : " + totalAPayer + "euros.");
            }

            contentStream.endText();

            contentStream.close();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionnez un emplacement pour sauvegarder le bordereau PDF");
            fileChooser.setSelectedFile(new File("bordereau.pdf"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                document.save(new FileOutputStream(fileToSave));
                document.close();
            }

            numeroBordereau++;

            JOptionPane.showMessageDialog(null, "Bordereau PDF généré avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la génération du PDF.");
        }
    }

    private static int obtenirNumeroSemaine(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        return weekNumber;
    }

    // Méthode pour sauvegarder un utilisateur dans un fichier sérialisé
    private void sauvegarderUtilisateur(Client utilisateur) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("utilisateur.ser"));
            oos.writeObject(utilisateur);
            oos.close();
            JOptionPane.showMessageDialog(null, "Utilisateur créé avec succès.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la création de l'utilisateur.");
        }
    }
}
