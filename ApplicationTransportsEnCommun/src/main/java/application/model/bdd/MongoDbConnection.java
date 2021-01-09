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
import java.time.Period;
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

    public static MongoDbConnection getMongoInstance(){
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
        //System.out.println(c.updateNbVoyage(11,1));
        //System.out.println(c.updateAbonnement(11,12));
        //System.out.println(c.isValide(11));

    }

    public int getNbVoyage(long idTitulaire) {
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).iterator().next().getNbVoyages();
    }

    public Carte getCarteById(long idTitulaire) {
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).first();
    }

    public LocalDate getFinAbo(long idTitulaire) {
        if (this.cartes.find(new Document("id_titulaire", idTitulaire)).first().getDateFinAbonnement() == null) {
            return (Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first())).setDateFinAbonnement(LocalDate.now()).getDateFinAbonnement();
        }
        if (Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).getDateFinAbonnement().isBefore(LocalDate.now())){
            return (Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first())).setDateFinAbonnement(LocalDate.now()).getDateFinAbonnement();
        }
        return Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).getDateFinAbonnement();
    }

    public long updateNbVoyage(long idTitulaire, int nbVoyagePlusMoins) {
        var updateRes = this.cartes.updateOne(
                new Document("id_titulaire", idTitulaire),
                new Document("$inc",
                        new Document("nb_voyages", nbVoyagePlusMoins)));
        return updateRes.getModifiedCount();
    }

    public long updateAbonnement(long idTitulaire, int nbMois) {
        var updateRes = this.cartes.updateOne(
                new Document("id_titulaire", idTitulaire),
                new Document("$set",
                        new Document("date_fin_abonnement", getFinAbo(idTitulaire).plusMonths(nbMois))));
        return updateRes.getModifiedCount();
    }

    public BsonObjectId addCarteByTitu(long idTitulaire) {
        return this.insertCarte(new Carte().setIdTitulaire(idTitulaire).setNbVoyages(0));
    }

    public BsonObjectId insertCarte(Carte carte) {
        return this.cartes.insertOne(carte).getInsertedId().asObjectId();
    }

    public DeleteResult removeCarteById(ObjectId id){
        return this.cartes.deleteOne(new Document("id",id));
    }

    public DeleteResult removeCarteByTitu(long idTitu){
        return this.cartes.deleteOne(new Document("id_titulaire", idTitu));
    }


    public boolean isValide(int idTitulaire) {
        var carte = this.getCarteById(idTitulaire);
        if (LocalDate.now().isBefore(getFinAbo(carte.getIdTitulaire()))) {
            return true;
        }
        else if (carte.getDateDerniereValidation() != null) {
            if (LocalDateTime.now().isBefore(carte.getDateDerniereValidation().plusMinutes(1))) {
                return true;
            }
            else if (carte.getNbVoyages() > 0) {
                carte.setNbVoyages(carte.getNbVoyages() - 1);
                this.updateDateValidation(carte.getIdTitulaire());
                this.updateNbVoyage(carte.getIdTitulaire(), -1);
                return true;
            }
        }
        else if (carte.getNbVoyages() > 0) {
            carte.setNbVoyages(carte.getNbVoyages() - 1);
            this.updateDateValidation(carte.getIdTitulaire());
            this.updateNbVoyage(carte.getIdTitulaire(), -1);
            return true;
        }
        return false;
    }

    public LocalDateTime getDateValidation(long idTitulaire) {
        if (this.cartes.find(new Document("id_titulaire", idTitulaire)).first().getDateDerniereValidation() == null) {
            return (Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).setDateDerniereValidation(LocalDateTime.now()).getDateDerniereValidation());
        }
        else {
            return(this.cartes.find(new Document("id_titulaire", idTitulaire)).first().setDateDerniereValidation(LocalDateTime.now()).getDateDerniereValidation());
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
