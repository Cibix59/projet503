package AMI;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.json.JSONObject;

public class MARCHE {

    //todo : faire methode pour generer et actualiser l'offre
    private static String[] offres;

    public static int portEcoute = 2025;

    public static int portEcouter = 2026;

    public static void main(String[] args) {

        // Création de la socket

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(portEcoute);
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création de la socket : " + e);
            System.exit(0);
        }

        // Création du message

        byte[] tampon = new byte[1024];

        DatagramPacket msg = new DatagramPacket(tampon, tampon.length);

        // Création du message retour

        // Lecture du message du client

        try {
            socket.receive(msg);
            String texte = new String(msg.getData(), 0, msg.getLength());
            JSONObject obj = new JSONObject(texte);
            switch (obj.getString("requete")) {
                case "inscription":
                case "miseEnVente":
                case "retrait":
                case "consultation":
                    try {
                        socket.send(getDataOffres());
                    } catch (IOException e) {
                        System.err.println("Erreur lors de l'envoi du message : " + e);
                        System.exit(0);
                    }

                default:
                    break;
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }

        // Fermeture de la socket

        socket.close();

    }

    public static String[] getOffres() {
        return offres;
    }

    public static DatagramPacket getDataOffres() {
        DatagramPacket msgr = null;
        try {
            InetAddress adresser = InetAddress.getByName(null);
            String messager = getOffres().toString();
            byte[] tamponr = messager.getBytes();
            msgr = new DatagramPacket(tamponr, tamponr.length, adresser, portEcouter);

        } catch (UnknownHostException e) {
            System.err.println("Erreur lors de la création du message : " + e);
            System.exit(0);
        }
        return msgr;
    }

}
