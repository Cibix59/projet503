import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Classe correspondant au handler sur le contexte 'commande.html'.
 */
class CommandeHandler implements HttpHandler {

    public void handle(HttpExchange t) {
        String reponse = """
            <h1>Bienvenue sur la page de commande</h1>
            <form method="post" action="add_commande.php">
    Type d'energie :
    <input type="text" name="type" id="type" />
    Quantite :
    <input type="text" name="qte" id="qte" />
    <input type="submit" value="Passer la commande" />
</form>
            """;


	

        // Envoi de l'en-tête Http
        try {
            Headers h = t.getResponseHeaders();
            h.set("Content-Type", "text/html; charset=utf-8");
            t.sendResponseHeaders(200, reponse.getBytes().length);
        } catch(IOException e) {
            System.err.println("Erreur lors de l'envoi de l'en-tête : " + e);
            System.exit(0);
        }

        // Envoi du corps (données HTML)
        try {
            OutputStream os = t.getResponseBody();
            os.write(reponse.getBytes());
            os.close();
        } catch(IOException e) {
            System.err.println("Erreur lors de l'envoi du corps : " + e);
        }
    }

}