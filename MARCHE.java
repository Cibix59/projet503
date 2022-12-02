import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONObject;

public class MARCHE {

    private static Energie[] offres = new Energie[100];
    public static int nombreDoffres = 0;

    public static int portEcoute = 2025;

    public static int portEcouter = 2026;

    public static int portEcoutePONE = 3031;
    public static int portEcouteTARE = 3032;
    public static int portEcouteAMI = 5001;


    public static void main(String[] args) {

        // Création de la socket
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(portEcoutePONE);
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }

        // Lecture du message du client
        DatagramPacket msgRecu = null;
        try {
            byte[] tampon = new byte[1024];
            msgRecu = new DatagramPacket(tampon, tampon.length);
            socket.receive(msgRecu);
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }

        // Récupération de l'objet
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msgRecu.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Energie energie = (Energie) ois.readObject();

            System.out.println("Recu : " + energie.toString());

            String reponseAMI = demandeConfirmationAddEnergieAMI(energie);

            // si l'AMI confirme, l'energie est ajoutée au marché
            if (reponseAMI.equals("Valide")) {
                offres[nombreDoffres] = energie;
                nombreDoffres++;
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Objet reçu non reconnu : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération de l'objet : " + e);
            System.exit(0);
        }

        socket.close();

        ouvreSocketTARE();

    }

    private static void ouvreSocketTARE() {

        // PARTIE GESTION REQUETE DU TARE(style demande des energies sur le marché)
        // Création de la socket
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(portEcouteTARE);
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création de la socket : " + e);
            System.exit(0);
        }

        // Création du message

        byte[] tampon = new byte[1024];

        DatagramPacket msg = new DatagramPacket(tampon, tampon.length);

        // Lecture du message du TARE
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
                    break;
                case "achat":
                    break;
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

    public static String demandeConfirmationAddEnergieAMI(Energie energie) {
        // se connecte en TCP au serveur AMI pour lui faire verifier un objet Energie

        // Création de la socket
        Socket socket = null;
        try {
            socket = new Socket("localhost", portEcouteAMI);
        } catch (UnknownHostException e) {
            System.err.println("Erreur sur l'hôte : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Création de la socket impossible : " + e);
            System.exit(0);
        }

        // Association d'un flux d'entrée et de sortie
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            System.err.println("Association des flux impossible : " + e);
            System.exit(0);
        }

        // Envoi de la nouvelle energie à faire verifier sous forme de JSON
        System.out.println("Envoi: " + energie.toJson().toString());
        output.println("1" + energie.toJson().toString());

        String message = "";
        // Lecture de 'confirmation'
        try {
            message = input.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e);
            System.exit(0);
        }
        System.out.println("Lu: " + message);
        // Fermeture des flux et de la socket
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture des flux et de la socket : " + e);
            System.exit(0);
        }

        // renvois la reponse de l'AMI
        return message;
    }

    public static Energie[] getOffres() {
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
