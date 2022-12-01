package codeSuivi;

import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test_CodeSuivi {

    public Test_CodeSuivi() {
    }

    public static void main(String[] args) {


        CodeDeSuivi code = new CodeDeSuivi("000100000000000100101000100001");

        JSONObject json = new JSONObject();
        json = code.toJson();
        System.out.println(json);

        CodeDeSuivi codeFromJson = CodeDeSuivi.FromJson(json.toString());
        System.out.println(codeFromJson);
    }
}