
import org.json.JSONObject;
import java.util.*;

public class verifCodeDeSuivi {
    
    public static String vérification(Energie energie , CodeDeSuivi codeSuivi){
        JSONObject ener = energie.toJson();
        JSONObject code = codeSuivi.toJson();
        String validité ="Valide";
        String erreur="";
        HashMap<String, Integer> listePays = new HashMap<String, Integer>();
        listePays.put("Chine",1);
        listePays.put("Etats-unis",2);
        listePays.put("Brésil",3);
        listePays.put("Canada",4);
        listePays.put("Russie",5);
        listePays.put("Inde",6);
        listePays.put("Norvège",7);
        listePays.put("Allemagne",8);
        listePays.put("Japon",9);
        listePays.put("Suède",10);
        listePays.put("France",11);
        ArrayList<String> listeTypeEnergie = new ArrayList<String>();
        listeTypeEnergie.add("Nucléaire");
        listeTypeEnergie.add("Hydraulique");
        listeTypeEnergie.add("Solaire");
        listeTypeEnergie.add("Eolienne");
        listeTypeEnergie.add("Biomasse");
        listeTypeEnergie.add("Gaz");
        listeTypeEnergie.add("Charbon");
        listeTypeEnergie.add("Pétrole");
        
        if(!listeTypeEnergie.contains(ener.getString("TypeEnergie"))){
            validité="Invalide";
            erreur+="Type d'énergie invalide\n";
        }
        if(!listePays.containsKey(ener.getString("Pays"))){
            validité="Invalide";
            erreur+="Nom de Pays invalide\n";
        }
        else{
        Integer pays = listePays.get(ener.getString("Pays"));
        if(!(pays==code.getInt("Pays"))){
            validité="Invalide";
            erreur+="Le pays ne correspond pas\n";
        }
    }
        
        if(!(ener.getInt("Quantite") == code.getInt("Quantite"))){
            validité="Invalide";
            erreur+="Quantité invalide\n";
        }
        if(!(ener.getString("CodeSuivi") == code.getString("CodeSuivi"))){
            validité="Invalide";
            erreur+="Le code de suivi ne correspond pas\n";
        }
        
        return validité+erreur ;
    }

}
