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
                new Document("date_fin_abonnement", LocalDate.now().getMonthValue()+1));
        return updateRes.getModifiedCount();
    }

    public long updateAboAnnuel(int idTitulaire){
        var updateRes=  this.cartes.updateOne(
                new Document("id_titulaire",idTitulaire),
                new Document("date_fin_abonnement", LocalDate.now().getYear()+1));
        return updateRes.getModifiedCount();
    }

    public BsonObjectId addCarteByTitu(int idTitulaire){
        return this.insertCarte(new Carte().setIdTitulaire(idTitulaire));
    }


    public BsonObjectId insertCarte(Carte carte){
        return this.cartes.insertOne(carte).getInsertedId().asObjectId();
    }


    public List<Carte> getAllCarte(){
        List<Carte> res = new ArrayList<>();
        this.cartes.find().forEach(res::add);
        return res;
    }


    public static void main(String[] args) {
        //MongoDbConnection c = new MongoDbConnection();
/*
        var res = c.insertCarte(new Carte()
                .setIdTitulaire(1)
                .setNbVoyages(0)
                .setDate_fin_abonnement(LocalDate.now().plusDays(10)));

        System.out.println(res.getValue());*/

        //c.getAllCarte().stream().forEach(cr -> System.out.println(cr.getIdTitulaire()));
        //System.out.println(c.updateNbVoyage(1, 4));


    }





}
