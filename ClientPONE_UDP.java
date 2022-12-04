
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.Random;

/**
 * Client UDP permettant de tester l'envoi d'objets dans un segment UDP.
 * Le client crée un message contenant une personne et l'envoie au serveur.
 * Le numéro de port est spécifie dans la classe ServeurUDPObjets.
 * 
 * @author Cyril Rabat
 */
public class ClientPONE_UDP {

    public static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static int portEcoute = 3031;

    public static void main(String[] args) {

        Random rng = new Random();
        String characters = "01";
        String nomPONE = "PONE" + generateString(rng, characters, 6);
        boolean finSession = false;
        while (!finSession) {
            System.out.println("Interface PONE : " + nomPONE);
            System.out.println("1. Ajouter une énergie");
            System.out.println("2. Quitter");
            // get an int value from user in console
            int t = new Scanner(System.in).nextInt();
            switch (t) {
                case 1:
                    // Création de l'energie
                    Energie energie = genererEnergie(nomPONE);
                    ajoutEnergie(energie);
                    break;
                case 2:
                    finSession = true;
                    break;
            }
        }

    }

    private static Energie genererEnergie(String nomPONE) {
        boolean creationEnergie = false;
        // generation par defaut
        Energie energie = new Energie(0, "Biomasse", 0, "France");
        energie.setCodePone(nomPONE);
        while (!creationEnergie) {
            System.out.println("Creation energie : " + energie.toString());
            System.out.println("1. Quantite");
            System.out.println("2. Type");
            System.out.println("3. Prix");
            System.out.println("4. Pays");
            System.out.println("5. Valider");
            int t = new Scanner(System.in).nextInt();
            String u="";
            int v;
            switch (t) {
                case 1:
                    v = new Scanner(System.in).nextInt();
                    energie.setQuantite(v);
                    break;
                case 2:
                    
                     u = new Scanner(System.in).nextLine();
                    energie.setTypeEnergie(u);
                    break;
                case 3:
                    // Création de l'energie
                     v = new Scanner(System.in).nextInt();
                    energie.setPrix(v);
                    break;
                case 4:
                    // Création de l'energie
                     u = new Scanner(System.in).nextLine();
                    energie.setPays(u);
                    break;
                case 5:
                    creationEnergie = true;
                    break;
            }
        }

        return energie;
    }

    public static void ajoutEnergie(Energie energie) {
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
            oos.writeObject(energie);
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
            System.out.println("Energie envoyée sur le marché");
        } catch (UnknownHostException e) {
            System.err.println("Erreur lors de la création de l'adresse : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message : " + e);
            System.exit(0);
        }

        socket.close();
    }

}