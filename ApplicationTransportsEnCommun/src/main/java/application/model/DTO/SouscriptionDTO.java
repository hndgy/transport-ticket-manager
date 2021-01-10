package application.model.DTO;

public class SouscriptionDTO {

    private long id;
    private String type;

    public SouscriptionDTO(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
