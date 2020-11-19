package application.model.models.utilisateur;

public abstract class AbstractUtilisateur implements IUtilisateur{

    private long id;
    private String prenom;
    private String nom;
    private String mail;
    private String motDePasse;


    public AbstractUtilisateur(long id, String prenom, String nom, String mail, String motDePasse) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.mail = mail;
        this.motDePasse = motDePasse;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getMail() {
        return this.mail;
    }

    @Override
    public String getNom() {
        return this.nom;
    }

    @Override
    public String getPrenom() {
        return this.prenom;
    }

    @Override
    public String getMotDePasse() {
        return this.motDePasse;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }



}
