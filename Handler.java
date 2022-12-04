import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;

import org.json.JSONObject;

class Handler implements HttpHandler {
    public static int portEcoute = 3032;
    public void handle(HttpExchange t) throws IOException {
        String response = "";
        StringBuilder sb = new StringBuilder();
        InputStream ios = t.getRequestBody();
        int i;

        while ((i = ios.read()) != -1) {
            sb.append((char) i);
        }
        System.out.println("oui");
        JSONObject commande=null;
        try {
            String messageRecu = sb.toString();
            commande = new JSONObject(messageRecu);
            System.out.println(commande.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        
        // recupere les numEnergie à acheter
        int[] listeNumEnergies = demandeListeNumOffres(commande.getInt("qte"), commande.getString("type"));
        // boucle sur tout les numeros d'energies à acheter
        for (int j : listeNumEnergies) {
            System.out.println("num energie à acheter : "+j);
        }
        response = "reponseTest";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static int[] demandeListeNumOffres(int qte,String type) {
        // cree un objet JSON pour faire une requete au MARCHE avec le champ "requete"
        JSONObject requete = new JSONObject();
        requete.put("requete", "listeNumEnergies");
        requete.put("qte", qte);
        requete.put("type", type);

        System.out.println("requete créee : " + requete.toString());
        // Création de la socket
        DatagramSocket socket = null;
        try {
            System.out.println("11111");
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }
System.out.println(("2222"));
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
            System.out.println("3333");
            socketReponse = new DatagramSocket(portEcoute+1);
        } catch (SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            System.exit(0);
        }
System.out.println("444");
        byte[] tamponR = new byte[1024];
        DatagramPacket msgR = new DatagramPacket(tamponR, tamponR.length);
        try {
            System.out.println("attente de reponse");
            socketReponse.receive(msgR);
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            System.exit(0);
        }
        int[] listeNumEnergies=null;
        // Récupération de l'objet
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msgR.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            listeNumEnergies = (int[]) ois.readObject();

            System.out.println("Recu : " + listeNumEnergies.toString());

        } catch (ClassNotFoundException e) {
            System.err.println("Objet reçu non reconnu : " + e);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Erreur lors de la récupération de l'objet : " + e);
            System.exit(0);
        }
        socketReponse.close();

        return listeNumEnergies;

    }
}