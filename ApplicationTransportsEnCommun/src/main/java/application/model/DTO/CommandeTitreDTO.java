package application.model.DTO;

public class CommandeTitreDTO {

    private long idUser;
    private int nbTitre;

    public CommandeTitreDTO(long idCarte, int nbTitre) {
        this.idUser = idCarte;
        this.nbTitre = nbTitre;
    }

    public long getIdUser() {
        return idUser;
    }

    public int getNbTitre() {
        return nbTitre;
    }
}
