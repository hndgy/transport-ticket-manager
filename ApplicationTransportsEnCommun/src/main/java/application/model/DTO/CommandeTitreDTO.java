package application.model.DTO;

public class CommandeTitreDTO {

    private String idCarte;
    private int nbTitre;

    public CommandeTitreDTO(String idCarte,int nbTitre) {
        this.idCarte = idCarte;
        this.nbTitre = nbTitre;
    }

    public String getIdCarte() {
        return idCarte;
    }

    public int getNbTitre() {
        return nbTitre;
    }
}
