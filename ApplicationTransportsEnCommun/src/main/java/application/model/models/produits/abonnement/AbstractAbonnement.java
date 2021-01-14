package application.model.models.produits.abonnement;

import java.time.LocalDate;

public abstract class AbstractAbonnement implements IAbonnement{

    private long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private float prix;


    public AbstractAbonnement(long id, LocalDate dateDebut, LocalDate dateFin, float prix) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    @Override
    public LocalDate getDateFin() {
        return this.dateFin;
    }

    @Override
    public float getPrix() {
        return this.prix;
    }


}
