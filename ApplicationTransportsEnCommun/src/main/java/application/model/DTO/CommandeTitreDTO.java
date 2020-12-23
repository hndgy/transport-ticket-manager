package application.model.DTO;

public class CommandeTitreDTO {

    private long idCarte;
    private int nbTitre;

    public CommandeTitreDTO(long idCarte,int nbTitre) {
        this.idCarte = idCarte;
        this.nbTitre = nbTitre;
    }

    public long getIdCarte() {
        return idCarte;
    }

    public int getNbTitre() {
        return nbTitre;
    }
}
