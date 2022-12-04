
import org.json.JSONObject;
import java.util.*;

public class verifCodeDeSuivi {
    public static int getIntFromPays(String pays) {
        HashMap<String, Integer> listePays = new HashMap<String, Integer>();
        listePays.put("Chine", 1);
        listePays.put("Etats-unis", 2);
        listePays.put("Brésil", 3);
        listePays.put("Canada", 4);
        listePays.put("Russie", 5);
        listePays.put("Inde", 6);
        listePays.put("Norvège", 7);
        listePays.put("Allemagne", 8);
        listePays.put("Japon", 9);
        listePays.put("Suède", 10);
        listePays.put("France", 11);

        if (listePays.containsKey(pays)) {
            return listePays.get(pays);
        } else {
            return -1;
        }

    }

    public static String vérification(Energie energie, CodeDeSuivi codeSuivi) {
        JSONObject ener = energie.toJson();
        JSONObject code = codeSuivi.toJson();
        String validité = "Valide";
        String erreur = "";
        HashMap<String, Integer> listePays = new HashMap<String, Integer>();
        listePays.put("Chine", 1);
        listePays.put("Etats-unis", 2);
        listePays.put("Brésil", 3);
        listePays.put("Canada", 4);
        listePays.put("Russie", 5);
        listePays.put("Inde", 6);
        listePays.put("Norvège", 7);
        listePays.put("Allemagne", 8);
        listePays.put("Japon", 9);
        listePays.put("Suède", 10);
        listePays.put("France", 11);
        ArrayList<String> listeTypeEnergie = new ArrayList<String>();
        listeTypeEnergie.add("Nucléaire");
        listeTypeEnergie.add("Hydraulique");
        listeTypeEnergie.add("Solaire");
        listeTypeEnergie.add("Eolienne");
        listeTypeEnergie.add("Biomasse");
        listeTypeEnergie.add("Gaz");
        listeTypeEnergie.add("Charbon");
        listeTypeEnergie.add("Pétrole");

        if (!listeTypeEnergie.contains(ener.getString("TypeEnergie"))) {
            validité = "Invalide";
            erreur += "Type d'énergie invalide\n";
        }
        if (!listePays.containsKey(ener.getString("Pays"))) {
            validité = "Invalide";
            erreur += "Nom de Pays invalide\n";
        } else {
            Integer pays = listePays.get(ener.getString("Pays"));
            if (!(pays == code.getInt("Pays"))) {
                validité = "Invalide";
                erreur += "Le pays ne correspond pas\n";
            }
        }

        if (!(ener.getInt("Quantite") == code.getInt("Quantite"))) {
            validité = "Invalide";
            erreur += "Quantité invalide\n";
        }
        if (!(ener.getString("CodeSuivi") == code.getString("CodeSuivi"))) {
            validité = "Invalide";
            erreur += "Le code de suivi ne correspond pas\n";
        }

        return validité + erreur;
    }

    public static int getIntFromType(String typeEnergie) {
        HashMap<String,Integer> listeTypeEnergie = new HashMap<String,Integer>();
        listeTypeEnergie.put("Nucléaire",1);
        listeTypeEnergie.put("Hydraulique",2);
        listeTypeEnergie.put("Solaire",3);
        listeTypeEnergie.put("Eolienne",4);
        listeTypeEnergie.put("Biomasse",5);
        listeTypeEnergie.put("Gaz",6);
        listeTypeEnergie.put("Charbon",7);
        listeTypeEnergie.put("Pétrole",8);

        if (listeTypeEnergie.containsKey(typeEnergie)) {
            return listeTypeEnergie.get(typeEnergie);
        } else {
            return -1;
        }
    }

}
