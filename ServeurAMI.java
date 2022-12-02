
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;

public class ServeurAMI {

    public static final int portEcoute = 5001;

    public static void main(String[] args) {
        // Création de la socket serveur
        ServerSocket socketServeur = null;
        try {
            socketServeur = new ServerSocket(portEcoute);
        } catch (IOException e) {
            System.err.println("Création de la socket impossible : " + e);
            System.exit(0);
        }

        // Attente d'une connexion d'un client
        Socket socketClient = null;
        try {
            socketClient = socketServeur.accept();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'attente d'une connexion : " + e);
            System.exit(0);
        }

        // Association d'un flux d'entrée et de sortie
        BufferedReader input = null;
        PrintWriter output = null;
        try {
            input = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);
        } catch (IOException e) {
            System.err.println("Association des flux impossible : " + e);
            System.exit(0);
        }

        // Lecture du prix
        String message = "";
        try {
            message = input.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e);
            System.exit(0);
        }
        System.out.println("Lu: " + message);

        // recupere quel est le type d'information qui est recu
        String typeInfo = message.substring(0, 1);

        switch (typeInfo) {
            case "1":
                // Envoi de validité


                // transforme le string en json en energie
                JSONObject json = new JSONObject(message.substring(1));
                Energie energie = Energie.FromJson(json);

                String val = validite(energie);
                System.out.println("Envoi: " + val);
                output.println(val);
                break;
            default:
                output.println("pas de traitement possible");
                break;
        }

        // Lecture de fin transaction
        try {
            message = input.readLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture : " + e);
            System.exit(0);
        }
        System.out.println("Lu: " + message);

        // Envoi de transaction terminée
        message = "transaction terminée";
        System.out.println("Envoi: " + message);
        output.println(message);

        // Fermeture des flux et des sockets
        try {
            input.close();
            output.close();
            socketClient.close();
            socketServeur.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture des flux et des sockets : " + e);
            System.exit(0);
        }
    }

    public static String validite(Energie energie) {
        // todo : verifier que toutes les informations sont ok
        //todo : verifier que ca correspond au code suivi (faire une methode utilitaire pour ça, qui verifie avec une table de correspondance ce qui est possible)
        String valid = "Invalide";
        if (energie.getPrix() < 1000) {
            valid = "Valide";
        }
        return valid;
    }
}