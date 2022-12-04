
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        codeSuivi.setQuantite(intToString(quantite,4));
    }

    public String getTypeEnergie() {
        return typeEnergie;
    }

    public void setTypeEnergie(String typeEnergie) {
        this.typeEnergie = typeEnergie;
        this.codeSuivi.setCodePone(typeEnergie);
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
    public Energie(int quantite, String typeEnergie, int prix, String pays) {
        this.quantite = quantite;
        this.typeEnergie = typeEnergie;
        this.prix = prix;
        this.pays = pays;
        this.codeSuivi= new CodeDeSuivi("000100000000000100101000100001");
        this.codeSuivi = new CodeDeSuivi(generationCodeSuivi(this));
    }

    private String intToString(int nombre,int taille){
        String nombreString = Integer.toString(nombre);
        while(nombreString.length() < taille){
            nombreString = "0" + nombreString;
        }
        return nombreString;
    }



    private String generationCodeSuivi(Energie energie) {
        //todo : generer automatiquement le code de suivi
        String codeTMP ="";
        codeTMP += intToString(energie.getQuantite(),4);
        //
        DateFormat format = new SimpleDateFormat("ddMMyyHHmm");
        Date date = new Date();
        System.out.println();
        codeTMP += format.format(date)+"01";
        
        codeTMP += intToString(verifCodeDeSuivi.getIntFromPays(energie.getPays()), 4);

        codeTMP += intToString(verifCodeDeSuivi.getIntFromType(energie.getTypeEnergie()), 3);


        codeTMP+= this.getCodeSuivi().gettare();

        //pone
        codeTMP += this.codeSuivi.getrevendeur();

        return codeTMP;
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
        return "Energie : " + this.codeSuivi.toString() + " " + this.quantite + " " + this.typeEnergie + " " + this.prix + " " + this.pays;
    }

    public String toStringLimite(){
        return  this.typeEnergie + " ("+this.pays+") : "+ this.quantite + " unites; prix : " +   this.prix + " euros "  ;
    }

    public void setCodePone(String nomPONE) {
        String codePone = nomPONE.substring(nomPONE.length() - 6);
        codeSuivi.setCodePone(codePone);
    }

}
