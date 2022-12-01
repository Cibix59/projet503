package PoneUdp.upd;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Serveur UDP permettant de tester l'envoi d'objets dans un segment UDP.
 * Le client crée un message contenant une personne et l'envoie au serveur.
 * Le numéro de port est spécifié dans la classe ServeurUDPObjets.
 * @author Cyril Rabat
 */
public class ServeurUDPObjets {

    public static int portEcoute = 3031;

    public static void main(String[] args) {
        // Création de la socket
        DatagramSocket socket = null;
        try {        
            socket = new DatagramSocket(portEcoute);
        } catch(SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }

        // Lecture du message du client
        DatagramPacket msgRecu = null;
        try {
            byte[] tampon = new byte[1024];
            msgRecu = new DatagramPacket(tampon, tampon.length);
            socket.receive(msgRecu);
        } catch(IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }

        // Récupération de l'objet
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msgRecu.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Personne p = (Personne) ois.readObject();
            
            System.out.println("Recu : " + p);
        } catch(ClassNotFoundException e) {
            System.err.println("Objet reçu non reconnu : " + e);
            System.exit(0);
        } catch(IOException e) {
            System.err.println("Erreur lors de la récupération de l'objet : " + e);
            System.exit(0);
        }
            
        socket.close();
    }

}