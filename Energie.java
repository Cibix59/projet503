
import java.io.Serializable;

import org.json.JSONObject;

public class Energie implements Serializable {
    private CodeDeSuivi codeSuivi;
    private int quantite;
    private String typeEnergie;
    private int prix;
    private String pays;

    public CodeDeSuivi getCodeSuivi() {
        return codeSuivi;
    }

    public void setCodeSuivi(CodeDeSuivi codeSuivi) {
        this.codeSuivi = codeSuivi;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getTypeEnergie() {
        return typeEnergie;
    }

    public void setTypeEnergie(String typeEnergie) {
        this.typeEnergie = typeEnergie;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Energie(String codeSuivi, int quantite, String typeEnergie, int prix, String pays) {

        this.codeSuivi = new CodeDeSuivi(codeSuivi);
        this.quantite = quantite;
        this.typeEnergie = typeEnergie;
        this.prix = prix;
        this.pays = pays;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("codeSuivi", this.codeSuivi.toJson());
        json.put("quantite", this.quantite);
        json.put("typeEnergie", this.typeEnergie);
        json.put("prix", this.prix);
        json.put("pays", this.pays);
        return json;
    }

    public static Energie FromJson(JSONObject json) {
        CodeDeSuivi codeSuivi = CodeDeSuivi.FromJson(json.getJSONObject("codeSuivi").toString());
        int quantite = json.getInt("quantite");
        String typeEnergie = json.getString("typeEnergie");
        int prix = json.getInt("prix");
        String pays = json.getString("pays");
        return new Energie(codeSuivi.toString(), quantite, typeEnergie,prix,pays);
    }

    public String toString() {
        return "Energie : " + this.codeSuivi.toString() + " " + this.quantite + " " + this.typeEnergie + this.prix + " " + this.pays;
    }

}
