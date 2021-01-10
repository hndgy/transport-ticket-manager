package application.model.DTO;

public class SouscriptionDTO {

    private long id;
    private int tarif;

    public SouscriptionDTO(long id, int tarif) {
        this.id = id;
        this.tarif = tarif;
    }

    public long getId() {
        return id;
    }

    public int getTarif() {
        return tarif;
    }
}
