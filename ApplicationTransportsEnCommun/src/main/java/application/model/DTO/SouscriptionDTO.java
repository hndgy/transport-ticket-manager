package application.model.DTO;

public class SouscriptionDTO {

    private long idUser;
    private String type;

    public SouscriptionDTO(long idUser, String type) {
        this.idUser = idUser;
        this.type = type;
    }

    public long getIdUser() {
        return idUser;
    }

    public String getType() {
        return type;
    }
}
