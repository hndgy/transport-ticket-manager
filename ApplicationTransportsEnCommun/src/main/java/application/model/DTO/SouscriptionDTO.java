package application.model.DTO;

public class SouscriptionDTO {

    private long id;
    private int nbMois;

    public SouscriptionDTO(long id, int nbMois) {
        this.id = id;
        this.nbMois = nbMois;
    }

    public long getId() {
        return id;
    }

    public int getNbMois() {
        return nbMois;
    }
}
