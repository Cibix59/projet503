import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

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


        //LANCE UNE DEMANDE AU MARCHE EN UDP POUR CONNAITRE LES OFFRES
        System.out.println(demandeListeOffres());
    }

    public static String demandeListeOffres(){
        //cree un objet JSON pour faire une requete au MARCHE avec le champ "requete"
        JSONObject requete = new JSONObject();
        requete.put("requete", "consultation");

        // Création de la socket
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }

        

        // Transformation en tableau d'octets
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(requete);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sérialisation : " + e);
            System.exit(0);
        }

        // Création et envoi du segment UDP
        try {
            byte[] donnees = baos.toByteArray();
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
        } finally {
            socket.close();
        }


        //todo : verifier si la reception fonctionne bien
        //peut etre ne pas recreer de socket
        String reponse="";
        //reception de la reponse
        try {
            socket = new DatagramSocket();
            byte[] tampon = new byte[1024];
            DatagramPacket msg = new DatagramPacket(tampon, tampon.length);
            socket.receive(msg);
            reponse=new String(msg.getData());
            System.out.println("Réponse reçue : " + reponse);
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        } finally {
            socket.close();
        }

        return reponse;
        
    }

}