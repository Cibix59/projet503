package TMPCodeSuiviJAVA;
import java.util.*;
import org.json.JSONObject;

public class CodeDeSuivi  implements Comparable<CodeDeSuivi> {
    
    
    private String lieu;
    private String energie;
    private String tare;
    private String revendeur;
    private String quantite;
    private String numCommande;
    
    public CodeDeSuivi(String lieu , String energie , String tare , String revendeur , String quantite , String numCommande) {
        this.lieu = lieu ;
        this.energie =energie ;
        this.tare=tare;
        this.revendeur=revendeur;
        this.quantite=quantite;
        this.numCommande=numCommande;
    }
    public CodeDeSuivi(String code) {
        this.lieu = code.substring(16,19) ;
        this.energie =code.substring(19,21) ;
        this.tare=code.substring(21,25);
        this.revendeur=code.substring(25,30);
        this.quantite=code.substring(0,4);
        this.numCommande=code.substring(4,16);
    }
    
    public String getlieu() {
        return lieu ;
    }
    
    public String getenergie() {
        return energie ;
    }
    
    public String gettare() {
        return tare ;
    }
    
    public String getrevendeur() {
        return revendeur ;
    }
    
    public String getquantite() {
        return quantite ;
    }
    
    public String getnumCommande() {
        return numCommande ;
    }
    public JSONObject toJson(){

        JSONObject obj = new JSONObject();

         ;

        obj.put("quantite",quantite );
        obj.put("numCommande", numCommande );
        obj.put("lieu", lieu );
        obj.put("energie", energie );
        obj.put("tare", tare);
        obj.put("revendeur", revendeur );

        return obj ;
    }
    
    public static CodeDeSuivi FromJson(String string) {
        JSONObject obj = new JSONObject(string);

        return new CodeDeSuivi(obj.getString("lieu") , obj.getString("energie") , obj.getString("tare") , obj.getString("revendeur") , obj.getString("quantite") , obj.getString("numCommande" ));
    }
    
    @Override
    public String toString() {
        return  quantite + numCommande +lieu + energie + tare + revendeur ;
    }

    @Override
    public int compareTo(CodeDeSuivi o) {
        if (quantite== o.getquantite()){
            return 0;}
        else if (Integer.parseInt(quantite) > Integer.parseInt(o.getquantite())){
            return 1;
        }
        else {
            return -1;
        }
        
    }
    
}