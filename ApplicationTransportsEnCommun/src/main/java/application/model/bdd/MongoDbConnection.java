package application.model.bdd;

import application.model.bdd.pojos.Carte;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDbConnection {


    private static final String DB_NAME = "transportDB";
    private static final String COL_CARTES = "cartes";
    private static final String USER_DB = "admin";
    private static final String USER = "admin";
    private static final String PASSWORD = "root";
    private static final String HOST = "localhost";


    private MongoDatabase db;
    private MongoCollection<Carte> cartes;
    private static MongoDbConnection instance;


    public MongoDbConnection() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + USER + ":" + PASSWORD + "@" + HOST + ":27017/");
        //ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings =
                MongoClientSettings
                        .builder()
                        .applyConnectionString(connectionString)
                        .codecRegistry(codecRegistry)
                        .build();
        MongoClient mongoClient = MongoClients.create(clientSettings);
        this.db = mongoClient.getDatabase(DB_NAME);
        this.cartes = this.db.getCollection(COL_CARTES, Carte.class);
    }

    public static MongoDbConnection getMongoInstance() {
        return instance == null ? new MongoDbConnection() : instance;
    }


    public static void main(String[] args) {
        MongoDbConnection c = new MongoDbConnection();

/*
        var res = c.insertCarte(new Carte()
                .setIdTitulaire(2)
                .setNbVoyages(0)
                .setDateFinAbonnement(LocalDate.now().plusDays(10)));

        System.out.println(res.getValue());
*/

        /*
        c.getAllCarte().stream().forEach(cr -> System.out.println(cr.getIdTitulaire()));
        System.out.println(c.updateNbVoyage(1, 4));
        System.out.println(c.getCarteById(1).toString());
        System.out.println(c.updateAboMensuel(2));
        System.out.println(c.updateAboAnnuel(2));
        System.out.println(c.addCarteByTitu(3));
        System.out.println(c.updateAboMensuel(3));
        System.out.println(c.addCarteByTitu(4));
        System.out.println(c.updateAboAnnuel(4));
        System.out.println(c.updateAboMensuel(4));
        System.out.println(c.addCarteByTitu(6));
        System.out.println(c.updateAboAnnuel(6));
        System.out.println(c.isValide(6));
        System.out.println(c.addCarteByTitu(7));
        System.out.println(c.updateNbVoyage(7,1));
        System.out.println(c.isValide(7));
        */
        //System.out.println(c.addCarteByTitu(11));
        //        //System.out.println(c.updateNbVoyage(11,1));
        //        //System.out.println(c.updateAbonnement(11,12));
        //        //System.out.println(c.isValide(11));

    }

    /**
     * Méthode qui permet de récuperer le nombre de voyage restant sur la carte de transport d'un utilisateur en passant son id en paramètre
     * @param idTitulaire long qui correspond à l'id du titulaire de la carte de transport
     * @return un int qui correspond au nombre de voyage restant
     */
    public int getNbVoyage(long idTitulaire) {
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).iterator().next().getNbVoyages();
    }

    /**
     * Méthode qui permet de récuperer une carte de transport en passant en paramètre l'id du titulaire de la carte
     * @param idTitulaire long qui correspond à l'id du titulaire de la carte de transport
     * @return une Carte (voir pojos) qui correspond a la carte de transport demandée
     */
    public Carte getCarteById(long idTitulaire) {
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).first();
    }

    /**
     * Méthode qui permet de récuperer une carte de transport en passant directement en paramètre l'id de la carte de transport
     * @param idCarte String qui correspond à l'ObjectId de la carte de transport
     * @return une Carte (voir pojos) qui correspond à la carte de transport demandée
     */
    public Carte getCarteByIdCarte(String idCarte) {
        return this.cartes.find(new Document("_id", new ObjectId(idCarte))).first();
    }


    /**
     * Méthode qui permet de set la date de fin d'abonnement d'une carte de transport à la date de l'appel si elle n'en a pas ou si l'abonnement à déja expiré au moment de l'appel et de retourner cette date ou de retourner la date de fin de l'abonnement d'une carte
     * @param idCarte String qui correspond à l'ObjectId de la carte de transport pour laquelle on veut récupérer l'abonnement
     * @return une LocalDate qui correspond au jour de fin de l'abonnement ou à la date d'aujourd'hui si pas d'abonnement ou si abonnement expiré
     */
    public LocalDate getFinAbo(String idCarte) {
        if (this.cartes.find(new Document("_id", new ObjectId(idCarte))).first().getDateFinAbonnement() == null) {
            return (Objects.requireNonNull(this.cartes.find(new Document("_id", new ObjectId(idCarte))).first())).setDateFinAbonnement(LocalDate.now()).getDateFinAbonnement();
        }
        if (Objects.requireNonNull(this.cartes.find(new Document("_id", new ObjectId(idCarte))).first()).getDateFinAbonnement().isBefore(LocalDate.now())) {
            return (Objects.requireNonNull(this.cartes.find(new Document("_id", new ObjectId(idCarte))).first())).setDateFinAbonnement(LocalDate.now()).getDateFinAbonnement();
        }
        return Objects.requireNonNull(this.cartes.find(new Document("_id", new ObjectId(idCarte))).first()).getDateFinAbonnement();
    }


    /**
     * Méthode qui permet d'incrémenter ou de décrémenter avec le nombre que l'on veut le nombre de voyage restant sur la carte de transport d'un utilisateur passé en paramètre
     * @param idUser long qui correspond à l'id de l'utilisateur
     * @param nbVoyagePlusMoins int qui correspond au nombre de voyages qu'on va ajouter ou supprimer sur la carte
     */
    public void updateNbVoyage(long idUser, int nbVoyagePlusMoins) {
        this.cartes.updateOne(
                new Document("id_titulaire", idUser),
                new Document("$inc",
                        new Document("nb_voyages", nbVoyagePlusMoins)));
    }

    /**
     * Méthode qui met à jour la date de fin d'abonnement d'une carte de transport pour un utilisateur passé en paramètre
     * en rajoutant le nombre de mois (abonnement mensuel pour chaque mois ou annuel si 12 mois demandé) demandé par l'utilisateur que l'on a passé en paramètre
     * en appelant la méthode getFinAbo() pour set ou récuperer la date de fin d'abonnement
     * @param idUser long qui correspond à l'id de l'utilisateur qui achète un abonnement
     * @param nbMois int qui correspond au nombre de mois demandé par l'utilisateur (1 mois pour abo mensuel ou 12 pour abo annuel)
     */
    public void updateAbonnement(long idUser, int nbMois) {
        var idCarte = getCarteById(idUser).getId().toHexString();
        var updateRes = this.cartes.updateOne(
                new Document("id_titulaire", idUser),
                new Document("$set",
                        new Document("date_fin_abonnement", getFinAbo(idCarte).plusMonths(nbMois)))
        );
    }

    public BsonObjectId addCarteByTitu(long idTitulaire) {
        return this.insertCarte(new Carte().setIdTitulaire(idTitulaire).setNbVoyages(0));
    }

    public BsonObjectId insertCarte(Carte carte) {
        return this.cartes.insertOne(carte).getInsertedId().asObjectId();
    }

    public DeleteResult removeCarteById(ObjectId id) {

        return this.cartes.deleteOne(new Document("id", id));
    }

    public DeleteResult removeCarteByTitu(long idTitu) {
        return this.cartes.deleteOne(new Document("id_titulaire", idTitu));
    }


    public boolean isValide(String idCarte) {
        var carte = this.getCarteByIdCarte(idCarte);
        var userId = carte.getIdTitulaire();
        if (LocalDate.now().isBefore(getFinAbo(carte.getId().toHexString()))) {
            return true;
        } else if (carte.getDateDerniereValidation() != null) {
            if (LocalDateTime.now().isBefore(carte.getDateDerniereValidation().plusMinutes(1))) {
                return true;
            } else if (carte.getNbVoyages() > 0) {
                carte.setNbVoyages(carte.getNbVoyages() - 1);
                this.updateDateValidation(carte.getIdTitulaire());
                this.updateNbVoyage(userId, -1);
                return true;
            }
        } else if (carte.getNbVoyages() > 0) {
            carte.setNbVoyages(carte.getNbVoyages() - 1);
            this.updateDateValidation(carte.getIdTitulaire());
            this.updateNbVoyage(userId, -1);
            return true;
        }
        return false;
    }

    public LocalDateTime getDateValidation(long idTitulaire) {
        if (this.cartes.find(new Document("id_titulaire", idTitulaire)).first().getDateDerniereValidation() == null) {
            return (Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).setDateDerniereValidation(LocalDateTime.now()).getDateDerniereValidation());
        } else {
            return (this.cartes.find(new Document("id_titulaire", idTitulaire)).first().setDateDerniereValidation(LocalDateTime.now()).getDateDerniereValidation());
        }
    }

    public long updateDateValidation(long idTitulaire) {
        var updateRes = this.cartes.updateOne(
                new Document("id_titulaire", idTitulaire),
                new Document("$set",
                        new Document("date_derniere_validation", getDateValidation(idTitulaire))));
        return updateRes.getModifiedCount();
    }

    public List<Carte> getAllCarte() {
        List<Carte> res = new ArrayList<>();
        this.cartes.find().forEach(res::add);
        return res;
    }


}
