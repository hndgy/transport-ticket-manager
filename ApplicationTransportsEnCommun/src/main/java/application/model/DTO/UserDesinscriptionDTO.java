package application.model.DTO;

public class UserDesinscriptionDTO {

    public UserDesinscriptionDTO(String mail, String mdp) {
        this.mail = mail;
        this.mdp = mdp;
    }

    public String getMail() {
        return mail;
    }

    private String mail;

    public String getMdp() {
        return mdp;
    }

    private String mdp;

}
