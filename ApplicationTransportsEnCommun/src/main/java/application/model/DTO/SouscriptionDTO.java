package application.model.DTO;

public class SouscriptionDTO {

    private long id;
    private int type;

    public SouscriptionDTO(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}
