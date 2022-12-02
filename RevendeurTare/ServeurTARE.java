import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import java.net.InetSocketAddress;

public class ServeurTARE {
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
    }

}