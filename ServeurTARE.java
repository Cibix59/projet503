import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

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
        serveur.createContext("/commande", new Handler());
        serveur.setExecutor(null);
        serveur.start();

        System.out.println("Serveur démarré. Pressez CRTL+C pour arrêter.");

        boolean finSession = false;
        while (!finSession) {
            System.out.println("Interface TARE");
            System.out.println("1. Demander la liste des offres");
            System.out.println("2. Faire un achat");
            System.out.println("3. Quitter");
            int t = new Scanner(System.in).nextInt();
            switch (t) {
                case 1:
                    System.out.println("En attente ...");
                    System.out.println(demandeListeOffres());
                    System.out.println("socket fermé");
                    break;
                case 2:
                    System.out.println("Quel est le numero du bloc energie ?");
                    int numEnergie = new Scanner(System.in).nextInt();
                    faitAchat(numEnergie);
                    System.out.println("socket fermé");
                    break;
                case 3:
                    finSession = true;
                    break;
            }
        }

    }


    private static int[] getListeNumOffres(int qte,String type){
        int[] listeNumOffres = new int [100];




        return listeNumOffres;
    }

    private static void faitAchat(int numEnergie) {
        // cree un objet JSON pour faire une requete au MARCHE avec le champ "requete"
        JSONObject requete = new JSONObject();
        requete.put("requete", "achat");
        requete.put("numEnergie", ""+numEnergie);
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
        try {
            System.out.println("attente de reponse");
            socketReponse.receive(msgR);
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }
        Energie energie=null;
        // Récupération de l'objet
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msgR.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            energie = (Energie) ois.readObject();

            System.out.println("Recu : " + energie.toString());

        } catch (ClassNotFoundException e) {
            System.err.println("Objet reçu non reconnu : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération de l'objet : " + e);
            System.exit(0);
        }

        // todo : transmet au revendeur

        socketReponse.close();

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
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }

        socketReponse.close();

        return reponse;

    }
    
}