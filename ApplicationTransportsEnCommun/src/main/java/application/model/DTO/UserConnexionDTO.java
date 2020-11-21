package application.model.DTO;

public class UserConnexionDTO {
    public String getMail() {
        return mail;
    }

    public String getMdp() {
        return mdp;
    }

    private String mail;
    private String mdp;

    public UserConnexionDTO(String mail, String mdp) {
        this.mail = mail;
        this.mdp = mdp;
    }
}
