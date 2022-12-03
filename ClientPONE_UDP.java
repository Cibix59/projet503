
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
        String nomTARE = "TARE" + generateString(rng, characters, 16);
        boolean finSession = false;
        while (!finSession) {
            System.out.println("Interface TARE : " + nomTARE);
            System.out.println("1. Ajouter une énergie");
            System.out.println("2. Quitter");
            // get an int value from user in console
            int t = new Scanner(System.in).nextInt();
            switch (t) {
                case 1:
                    // Création de l'energie
                    Energie energie = new Energie("000100000000000100101000100001", 10, "charbon", 150, "France");
                    ajoutEnergie(energie);
                    break;
                case 2:
                    finSession = true;
                    break;
            }
        }

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