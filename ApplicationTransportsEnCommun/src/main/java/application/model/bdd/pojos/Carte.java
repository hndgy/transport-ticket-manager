package application.model.bdd.pojos;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public final class Carte {

    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("nb_voyages")
    private int nbVoyages;

    @BsonProperty("id_titulaire")
    private int idTitulaire;

    @BsonProperty("date_fin_abonnement")
    private LocalDate dateFinAbonnement;


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

    public LocalDate getDateFinAbonnement() {
        return dateFinAbonnement;
    }

    public Carte setDateFinAbonnement(LocalDate date_fin_abonnement) {
        this.dateFinAbonnement = date_fin_abonnement;
        return this;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "id=" + id +
                ", nbVoyages=" + nbVoyages +
                ", idTitulaire=" + idTitulaire +
                ", date_fin_abonnement=" + dateFinAbonnement +
                '}';
    }
}
