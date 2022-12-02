import java.io.IOException;


import com.sun.net.httpserver.HttpServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class ServeurTARE {
    public static int portEcoute = 3032;

    public static void main(String[] args) {

        HttpServer serveur = null;
        try {
            // construire un objet HttpServer en spécifiant le port d’écoute.
            // Ici, nous choisissons le port 8080
            serveur = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du serveur " + e);
            System.exit(0);
        }

        // nous créons un contexte index auquel nous associons le handler nommé
        // AccueilSimpleHandler. La méthode start démarre le serveur
        serveur.createContext("/Accueil", new AccueilSimpleHandler());
        serveur.createContext("/commande", new CommandeHandler());
        serveur.setExecutor(null);
        serveur.start();

        System.out.println("Serveur démarré. Pressez CRTL+C pour arrêter.");

        // LANCE UNE DEMANDE AU MARCHE EN UDP POUR CONNAITRE LES OFFRES

        System.out.println("lancement " + demandeListeOffres());
    }

    public static String demandeListeOffres() {
        // cree un objet JSON pour faire une requete au MARCHE avec le champ "requete"
        JSONObject requete = new JSONObject();
        requete.put("requete", "consultation");

        System.out.println("requete créee : " + requete.toString());
        // Création de la socket
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }

        // Création et envoi du segment UDP
        try {
            /* byte[] donnees = baos.toByteArray(); */
            byte[] donnees = requete.toString().getBytes();
            InetAddress adresse = InetAddress.getByName("localhost");
            DatagramPacket msg = new DatagramPacket(donnees, donnees.length,
                    adresse, portEcoute);
            socket.send(msg);
        } catch (UnknownHostException e) {
            System.err.println("Erreur lors de la création de l'adresse : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message : " + e);
            System.exit(0);
        }
        socket.close();


        // creation de la socket retour
        DatagramSocket socketReponse = null;
        try {
            socketReponse = new DatagramSocket(portEcoute);
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }

        byte[] tamponR = new byte[1024];
        DatagramPacket msgR = new DatagramPacket(tamponR, tamponR.length);
        String reponse = "";
        try {
            System.out.println("attente de reponse");
            socketReponse.receive(msgR);
            reponse = new String(msgR.getData(), 0, msgR.getLength());
            System.out.println("recu " + reponse);
            

        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }

        socketReponse.close();

        /*
         * System.out.println("vas attendre la reponse");
         * //todo : verifier si la reception fonctionne bien
         * //peut etre ne pas recreer de socket
         * String reponse="";
         * //reception de la reponse
         * try {
         * byte[] tampon = new byte[1024];
         * DatagramPacket msg = new DatagramPacket(tampon, tampon.length);
         * System.out.println("avant");
         * socket.receive(msg);
         * System.out.println("apres");
         * reponse=new String(msg.getData());
         * System.out.println("Réponse reçue : " + reponse);
         * } catch (SocketException e) {
         * System.err.println("Erreur lors de la création du socket : " + e);
         * System.exit(0);
         * } catch (IOException e) {
         * System.err.println("Erreur lors de la réception du message : " + e);
         * System.exit(0);
         * } finally {
         * socket.close();
         * }
         */

        return reponse;

    }

}