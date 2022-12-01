package PoneUdp.upd;
import java.io.Serializable;

/**
 * Classe correspondant a une personne, utilisée pour tester l'envoi d'objets
 * dans un segment UDP.
 * @author Cyril Rabat
 */
public class Personne implements Serializable {

    private String nom;
    private String prenom;

    /**
     * Constructeur par défaut
     */
    public Personne() {
        nom = "Doe";
        prenom = "John";
    }

    /**
     * Constructeur par initialisation
     * @param prenom le prénom de la personne
     * @param nom le nom de la personne
     */
    public Personne(String prenom, String nom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Retourne le nom de la personne
     * @return le nom de la personne
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prénom de la personne
     * @return le prénom de la personne
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Modifie le nom de la personne
     * @param nom le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifie le prénom de la personne
     * @param prenom le nouveau prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Transforme la personne sous forme de chaine de caractères
     * @return une chaine de caractères contenant la personne
     */
    public String toString() {
        return prenom + " " + nom;
    }

}