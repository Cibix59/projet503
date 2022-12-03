import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

class Handler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        String response = "This is the response";
//extract the data from t
        StringBuilder sb = new StringBuilder();
        InputStream ios = t.getRequestBody();
        int i;
        System.out.println("oui");
        while ((i = ios.read()) != -1) {
            sb.append((char) i);
        }
        System.out.println("hm: " + sb.toString());

        System.out.println("fin");
        response = "reponseTest";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

        /*
         * // Utilisation d'un flux pour lire les données du message Http
         * BufferedReader br = null;
         * try {
         * br = new BufferedReader(new InputStreamReader(t.getRequestBody(), "utf-8"));
         * } catch (UnsupportedEncodingException e) {
         * System.err.println("Erreur lors de la récupération du flux " + e);
         * System.exit(0);
         * }
         * // Récupération des données en POST
         * URI requestedUri = t.getRequestURI();
         * String query = requestedUri.getRawQuery();
         * 
         * try {
         * System.out.println("recu");
         * query = br.readLine();
         * System.out.println(query);
         * } catch (IOException e) {
         * System.err.println("Erreur lors de la lecture d'une ligne " + e);
         * System.exit(0);
         * }
         */

        // todo : renvoyer du json au lieu du html
        // Envoi de l'en-tête Http
        /*
         * try {
         * Headers h = t.getResponseHeaders();
         * h.set("Content-Type", "text/html; charset=utf-8");
         * t.sendResponseHeaders(200, reponse.getBytes().length);
         * } catch(IOException e) {
         * System.err.println("Erreur lors de l'envoi de l'en-tête : " + e);
         * System.exit(0);
         * }
         * 
         * // Envoi du corps (données HTML)
         * try {
         * OutputStream os = t.getResponseBody();
         * os.write(reponse.getBytes());
         * os.close();
         * } catch(IOException e) {
         * System.err.println("Erreur lors de l'envoi du corps : " + e);
         * }
         */

        // à retirer
        /*
         * t.sendResponseHeaders(200, response.length());
         * OutputStream os = t.getResponseBody();
         * os.write(response.getBytes());
         * os.close();
         */
    }
}