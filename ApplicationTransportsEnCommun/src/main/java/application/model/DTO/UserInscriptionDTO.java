package application.model.DTO;

public class UserInscriptionDTO {
    public String getNom() {
        return nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    private String nom;
    private String prenom;

    public UserInscriptionDTO(String nom, String prenom, String mail, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.mail = mail;
    }

    private String motDePasse;
    private String mail;

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }
}
