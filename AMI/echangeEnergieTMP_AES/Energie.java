package AMI.echangeEnergieTMP_AES;
import org.json.JSONObject;

public class Energie {
    private CodeDeSuivi codeSuivi;
    private int quantite;
    private String typeEnergie;
    
    public Energie(String codeSuivi, int quantite, String typeEnergie) {

        this.codeSuivi = new CodeDeSuivi(codeSuivi);
        this.quantite = quantite;
        this.typeEnergie = typeEnergie;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("codeSuivi", this.codeSuivi.toJson());
        json.put("quantite", this.quantite);
        json.put("typeEnergie", this.typeEnergie);
        return json;
    }
    
    public static Energie FromJson(JSONObject json) {
        CodeDeSuivi codeSuivi = CodeDeSuivi.FromJson(json.getJSONObject("codeSuivi").toString());
        int quantite = json.getInt("quantite");
        String typeEnergie = json.getString("typeEnergie");
        return new Energie(codeSuivi.toString(), quantite, typeEnergie);
    }


    
    
}
