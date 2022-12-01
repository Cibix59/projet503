
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

/**
 * Classe correspondant à un client TCP.
 * Le client envoie la chaine 'Bonjour' et lit une réponse de la part du
 * serveur.
 * Le client envoie ensuite la chaine 'Au revoir' et lit une réponse.
 * Le numéro de port du serveur est spécifié dans la classe ServeurTCP.
 * 
 * @author Cyril Rabat
 */
public class ClientTCP {

    public static final int portEcoute = 5001;

    public static void main(String[] args) {
        // Création de la socket
        Socket socket = null;
        try {
            socket = new Socket("localhost", portEcoute);
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

        Energie energie = new Energie("000100000000000100101000100001", 10, "charbon");
        
        String messageDecode = energie.toJson().toString();

        // Envoi de message crypté

        byte[] message = genererMessage(messageDecode,"motDePasseTresBI");

        String messsageCrypt = java.util.Base64.getEncoder().encodeToString(message);

        System.out.println("Envoi: " + new String(messsageCrypt));
        output.println(messsageCrypt);

        String retour="";
        // Lecture de 'confirmation'
        try {
            retour = input.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e);
            System.exit(0);
        }
        System.out.println("Lu: " + retour);

        // Envoi de 'Au revoir'
        retour = "Au revoir";
        System.out.println("Envoi: " + retour);
        output.println(retour);

        // Lecture de 'Au revoir'
        try {
            retour = input.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e);
            System.exit(0);
        }
        System.out.println("Lu: " + retour);

        // Fermeture des flux et de la socket
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture des flux et de la socket : " + e);
            System.exit(0);
        }
    }

    public static byte[] genererMessage(String message,String motDePasse) {
        // Chiffrement du message
        System.out.println("Message origine   : " + message);
        SecretKeySpec specification = new SecretKeySpec(motDePasse.getBytes(), "AES");
        byte[] bytes = null;

        try {
            Cipher chiffreur = Cipher.getInstance("AES");
            chiffreur.init(Cipher.ENCRYPT_MODE, specification);
            bytes = chiffreur.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            System.err.println("Erreur lors du chiffrement : " + e);
            System.exit(0);
        }
        return bytes;
    }

}