package application.model.bdd;

import application.model.bdd.pojos.Carte;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDbConnection {



    private static final String DB_NAME = "transportapp";
    private static final String COL_CARTES = "cartes";
    private static final String USER_DB = "admin";
    private static final String USER = "admin";
    private static final String PASSWORD = "root";
    private static final String HOST = "localhost";


    private  MongoDatabase db;
    private MongoCollection<Carte> cartes;



    private MongoDbConnection(){
        ConnectionString connectionString = new ConnectionString("mongodb://"+USER+":"+PASSWORD+"@"+HOST+":27017/");
        //ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),pojoCodecRegistry);
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

    public int getNbVoyage(int idTitulaire){
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).iterator().next().getNbVoyages();
    }


    public Carte getCarteById(int idTitulaire){
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).first();
    }

    public LocalDate getFinAbo(int idTitulaire){
        if (this.cartes.find(new Document("id_titulaire",idTitulaire)).first().getDateFinAbonnement() == null) {
            return (this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).setDateFinAbonnement(LocalDate.now()).getDateFinAbonnement();
        }
        return Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).getDateFinAbonnement();
    }

    public long updateNbVoyage(int idTitulaire, int nbVoyagePlusMoins){
        var updateRes=  this.cartes.updateOne(
                new Document("id_titulaire",idTitulaire),
                new Document("$inc",
                        new Document("nb_voyages",nbVoyagePlusMoins)));
        return updateRes.getModifiedCount();
    }

    public long updateAboMensuel(int idTitulaire){
        var updateRes=  this.cartes.updateOne(
                new Document("id_titulaire",idTitulaire),
                new Document("$set",
                        new Document("date_fin_abonnement",getFinAbo(idTitulaire).plusMonths(1))));
        return updateRes.getModifiedCount();
    }

    public long updateAboAnnuel(int idTitulaire){
        var updateRes=  this.cartes.updateOne(
                new Document("id_titulaire",idTitulaire),
                new Document("$set",
                        new Document("date_fin_abonnement", getFinAbo(idTitulaire).plusYears(1))));
        return updateRes.getModifiedCount();
    }

    public BsonObjectId addCarteByTitu(int idTitulaire){
        return this.insertCarte(new Carte().setIdTitulaire(idTitulaire));
    }

    public BsonObjectId insertCarte(Carte carte){
        return this.cartes.insertOne(carte).getInsertedId().asObjectId();
    }

    public boolean isValide(int idTitulaire){
        var carte = this.getCarteById(idTitulaire);
        if (LocalDate.now().isBefore(getFinAbo(carte.getIdTitulaire()))) {
            return true;
        }
        else if (carte.getNbVoyages() > 0) {
            carte.setNbVoyages(carte.getNbVoyages()-1);
            this.updateNbVoyage(carte.getIdTitulaire(),-1);
            return true;
        }
        return false;
    }


    public List<Carte> getAllCarte(){
        List<Carte> res = new ArrayList<>();
        this.cartes.find().forEach(res::add);
        return res;
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

        //c.getAllCarte().stream().forEach(cr -> System.out.println(cr.getIdTitulaire()));
        //System.out.println(c.updateNbVoyage(1, 4));

        //System.out.println(c.getCarteById(1).toString());
        //System.out.println(c.updateAboMensuel(2));
        //System.out.println(c.updateAboAnnuel(2));
        //System.out.println(c.addCarteByTitu(3));
        //System.out.println(c.updateAboMensuel(3));
        //System.out.println(c.addCarteByTitu(4));
        //System.out.println(c.updateAboAnnuel(4));
        //System.out.println(c.updateAboMensuel(4));
        //System.out.println(c.addCarteByTitu(6));
        //System.out.println(c.updateAboAnnuel(6));
        //System.out.println(c.isValide(6));
        //System.out.println(c.addCarteByTitu(7));

        //System.out.println(c.updateNbVoyage(7,1));
        //System.out.println(c.isValide(7));
    }





}
