package application.model.models.utilisateur;

public interface IUtilisateur {

    static IUtilisateur creerUtilisateur(long id, String prenom, String nom, String mail, String motDePasse) {
        return new UtilisateurImpl(id, prenom, nom, mail, motDePasse);
    }

    long getId();

    String getMail();
    String getNom();
    String getPrenom();
    String getMotDePasse();

    void setMail(String mail);
    void setNom(String nom);
    void setPrenom(String prenom);
    void setMotDePasse(String motDePasse);

}
