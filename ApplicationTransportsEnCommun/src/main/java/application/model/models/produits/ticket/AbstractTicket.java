package application.model.models.produits.ticket;

public abstract class AbstractTicket implements ITicket{

    private long id;
    private float prix;
    private int nbVoyages;

    public AbstractTicket(long id, float prix, int nbVoyages) {
        this.id = id;
        this.prix = prix;
        this.nbVoyages = nbVoyages;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public float getPrix() {
        return this.prix;
    }

    @Override
    public int getNbVoyagesRestant() {
        return this.nbVoyages;
    }

    @Override
    public boolean depenserVoyage() {
        if (this.nbVoyages > 0) {
            this.nbVoyages--;
            return true;
        }
        return false;
    }
}
