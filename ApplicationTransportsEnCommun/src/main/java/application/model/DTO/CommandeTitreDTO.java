package application.model.DTO;

public class CommandeTitreDTO {

    private long idUser;
    private int nbTitre;

    public CommandeTitreDTO(long idUser, int nbTitre) {
        this.idUser = idUser;
        this.nbTitre = nbTitre;
    }

    public long getIdUser() {
        return idUser;
    }

    public int getNbTitre() {
        return nbTitre;
    }
}
