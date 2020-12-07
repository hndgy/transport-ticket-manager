package application.model.bdd.pojos;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public final class Carte {

    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("nbVoyages")
    private int nbVoyages;

    @BsonProperty("id_titulaire")
    private int idTitulaire;

    @BsonProperty("date_fin_abonnement")
    private LocalDate date_fin_abonnement;


    public ObjectId getId() {
        return id;
    }

    public Carte setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public int getNbVoyages() {
        return nbVoyages;
    }

    public Carte setNbVoyages(int nbVoyages) {
        this.nbVoyages = nbVoyages;
        return this;
    }

    public int getIdTitulaire() {
        return idTitulaire;
    }

    public Carte setIdTitulaire(int idTitulaire) {
        this.idTitulaire = idTitulaire;
        return this;
    }

    public LocalDate getDate_fin_abonnement() {
        return date_fin_abonnement;
    }

    public Carte setDate_fin_abonnement(LocalDate date_fin_abonnement) {
        this.date_fin_abonnement = date_fin_abonnement;
        return this;
    }
}
