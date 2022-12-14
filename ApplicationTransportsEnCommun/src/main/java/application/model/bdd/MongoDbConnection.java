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

    private MongoDatabase db;
    private MongoCollection<Carte> cartes;
    private static MongoDbConnection instance;

    public MongoDbConnection() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + ConfigBdd.getConfig("mongo.user") + ":" + ConfigBdd.getConfig("mongo.password") + "@" + ConfigBdd.getConfig("mongo.host"));
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
        this.db = mongoClient.getDatabase(ConfigBdd.getConfig("mongo.db"));
        this.cartes = this.db.getCollection(ConfigBdd.getConfig("mongo.collection.cartes"), Carte.class);
    }

    public static MongoDbConnection getMongoInstance() {
        return instance == null ? new MongoDbConnection() : instance;
    }

    /**
     * M??thode qui permet de r??cuperer le nombre de voyage restant sur la carte de transport d'un utilisateur en passant son id en param??tre
     *
     * @param idTitulaire long qui correspond ?? l'id du titulaire de la carte de transport
     * @return un int qui correspond au nombre de voyage restant
     */
    public int getNbVoyage(long idTitulaire) {
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).iterator().next().getNbVoyages();
    }

    /**
     * M??thode qui permet de r??cuperer le nombre de voyage restant sur la carte de transport d'un utilisateur en passant l'id de la carte en param??tre
     *
     * @param idCarte String qui correspond ?? l'ObjectId de la carte de transport
     * @return un int qui correspond au nombre de voyage restant sur la carte
     */
    public int getNbVoyageByIdCarte(String idCarte) {
        return this.cartes.find(new Document("_id", new ObjectId(idCarte))).iterator().next().getNbVoyages();
    }

    /**
     * M??thode qui permet de r??cuperer une carte de transport en passant en param??tre l'id du titulaire de la carte
     *
     * @param idTitulaire long qui correspond ?? l'id du titulaire de la carte de transport
     * @return une Carte (voir pojos) qui correspond ?? la carte de transport demand??e
     */
    public Carte getCarteById(long idTitulaire) {
        return this.cartes.find(new Document("id_titulaire", idTitulaire)).first();
    }

    /**
     * M??thode qui permet de r??cuperer l'id d'une carte de transport en passant en param??tre l'id du titulaire de la carte
     *
     * @param idTitulaire long qui correspond ?? l'id du titulaire de la carte de transport
     * @return un ObjectId qui correspond ?? l'id de la carte de transport demand??e
     */
    public ObjectId getIdCarteByIdTitu(long idTitulaire) {
        return Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).getId();
    }

    /**
     * M??thode qui permet de r??cuperer une carte de transport en passant directement en param??tre l'id de la carte de transport
     *
     * @param idCarte String qui correspond ?? l'ObjectId de la carte de transport
     * @return une Carte (voir pojos) qui correspond ?? la carte de transport demand??e
     */
    public Carte getCarteByIdCarte(String idCarte) {
        return this.cartes.find(new Document("_id", new ObjectId(idCarte))).first();
    }

    /**
     * M??thode qui permet de set la date de fin d'abonnement d'une carte de transport ?? la date de l'appel
     * si elle n'en a pas ou si l'abonnement ?? d??ja expir?? au moment de l'appel et de retourner cette date
     * ou de retourner la date de fin de l'abonnement d'une carte
     *
     * @param idCarte String qui correspond ?? l'ObjectId de la carte de transport pour laquelle on veut r??cup??rer l'abonnement
     * @return une LocalDate qui correspond au jour de fin de l'abonnement ou ?? la date d'aujourd'hui si pas d'abonnement ou si abonnement expir??
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
     * M??thode qui permet de r??cup??rer la date de fin d'abonnement d'une carte avec l'id de la carte
     *
     * @param idCarte String qui correspond ?? l'ObjectId de la carte
     * @return un LocalDate qui correspond ?? la date de la fin de l'abonnement
     */
    public LocalDate getFinAboByIdCarte(String idCarte) {
        return this.cartes.find(new Document("_id", new ObjectId(idCarte))).first().getDateFinAbonnement();
    }

    /**
     * M??thode qui permet d'incr??menter ou de d??cr??menter avec le nombre que l'on veut le nombre de voyage restant sur la carte de transport d'un utilisateur pass?? en param??tre
     *
     * @param idUser            long qui correspond ?? l'id de l'utilisateur
     * @param nbVoyagePlusMoins int qui correspond au nombre de voyages qu'on va ajouter ou supprimer sur la carte
     */
    public void updateNbVoyage(long idUser, int nbVoyagePlusMoins) {
        this.cartes.updateOne(
                new Document("id_titulaire", idUser),
                new Document("$inc",
                        new Document("nb_voyages", nbVoyagePlusMoins)));
    }

    /**
     * M??thode qui appel updateNbVoyage() et qui permet d'ajouter directement 10 voyages ?? une carte de transport en prenant l'id de son titulaire en param??tre
     *
     * @param idUser long qui correspond ?? l'id de l'utilisateur
     */
    public void add10Voyages(long idUser){
        this.updateNbVoyage(idUser,10);
    }

    /**
     * M??thode qui appel updateNbVoyage() et qui permet d'ajouter directement 1 voyage ?? une carte de transport en prenant l'id de son titulaire en param??tre
     *
     * @param idUser long qui correspond ?? l'id de l'utilisateur
     */
    public void add1Voyage(long idUser){
        this.updateNbVoyage(idUser, 1);
    }

    /**
     * M??thode qui met ?? jour la date de fin d'abonnement d'une carte de transport pour un utilisateur pass?? en param??tre
     * en rajoutant le nombre de mois (abonnement mensuel pour chaque mois ou annuel si 12 mois demand??) demand?? par l'utilisateur que l'on a pass?? en param??tre
     * en appelant la m??thode getFinAbo() pour set ou r??cup??rer la date de fin d'abonnement
     *
     * @param idUser long qui correspond ?? l'id de l'utilisateur qui ach??te un abonnement
     * @param nbMois int qui correspond au nombre de mois demand?? par l'utilisateur (1 mois pour abo mensuel ou 12 pour abo annuel)
     */
    public void updateAbonnement(long idUser, int nbMois) {
        var idCarte = getCarteById(idUser).getId().toHexString();
        var updateRes = this.cartes.updateOne(
                new Document("id_titulaire", idUser),
                new Document("$set",
                        new Document("date_fin_abonnement", getFinAbo(idCarte).plusMonths(nbMois)))
        );
    }

    /**
     * M??thode qui appel updateAbonnement() et qui permet d'ajouter directement 1 mois d'abonnement ?? la carte de transport d'un utilisateur pass?? en param??tre
     *
     * @param idUser long qui correspond ?? l'id de l'utilisateur qui ach??te l'abonnement
     */
    public void add1MoisAbonnement(long idUser) {
        updateAbonnement(idUser, 1);
    }

    /**
     * M??thode qui appel updateAbonnement() et qui permet d'ajouter directement 1 an d'abonnement ?? la carte de transport
     *
     * @param idUser long qui correspond ?? l'id de l'utilisateur qui ach??te l'abonnement
     */
    public void add1AnAbonnement(long idUser) {
        updateAbonnement(idUser, 12);
    }

    /**
     * M??thode qui permet d'ins??rer dans la bdd mongo une carte de transport pass??e en param??tre
     *
     * @param carte Carte qui correspond ?? la carte de transport que l'on veut ins??rer
     * @return l'ObjectId de la carte que l'on a ins??r??
     */
    public BsonObjectId insertCarte(Carte carte) {
        return this.cartes.insertOne(carte).getInsertedId().asObjectId();
    }

    /**
     * M??thode qui permet d'ins??rer une carte de transport dans la bdd mongo pour un utilisateur pass?? en param??tre et un nbVoyages=0 et en appellant la m??thode insertCarte()
     *
     * @param idTitulaire long qui correspond ?? l'id du titulaire de la carte que l'on va cr??er
     * @return l'ObjectId de la carte que l'on a ins??r??
     */
    public BsonObjectId addCarteByTitu(long idTitulaire) {
        return this.insertCarte(new Carte().setIdTitulaire(idTitulaire).setNbVoyages(0));
    }

    /**
     * M??thode qui permet de supprimer de la base une carte en prenant son id en param??tre
     *
     * @param idCarte ObjectId qui correspond ?? l'id de la carte que l'on veut supprimer
     * @return le r??sultat de l'op??ration
     */
    public DeleteResult removeCarteById(ObjectId idCarte) {
        return this.cartes.deleteOne(new Document("_id", idCarte));
    }

    /**
     * M??thode qui permet de supprimer de la base une carte en prenant l'id de son titulaire en param??tre
     *
     * @param idTitu long qui correspond ?? l'id du titulaire de la carte que l'on veut supprimer
     * @return le r??sultat de l'op??ration
     */
    public DeleteResult removeCarteByTitu(long idTitu) {
        return this.cartes.deleteOne(new Document("id_titulaire", idTitu));
    }

    /**
     * M??thode qui permet de set et de get la date de la derni??re validation d'un ticket pour une carte ?? l'heure et la date de l'appel de cette m??thode
     *
     * @param idTitulaire long qui correspond ?? l'id tu titulaire de la carte pour laquelle on veut enregistrer la derni??re date de validation de ticket
     * @return un LocalDateTime qui correspond ?? la date et heure de l'appel de cette fonction
     */
    public LocalDateTime getDateValidation(long idTitulaire) {
        if (this.cartes.find(new Document("id_titulaire", idTitulaire)).first().getDateDerniereValidation() == null) {
            return (Objects.requireNonNull(this.cartes.find(new Document("id_titulaire", idTitulaire)).first()).setDateDerniereValidation(LocalDateTime.now()).getDateDerniereValidation());
        } else {
            return (this.cartes.find(new Document("id_titulaire", idTitulaire)).first().setDateDerniereValidation(LocalDateTime.now()).getDateDerniereValidation());
        }
    }

    /**
     * M??thode qui en appelant getDateValidation() permet de mettre ?? jour la derni??re date de validation quand un utilisateur valide un ticket
     *
     * @param idTitulaire long qui correspond ?? l'id du titulaire qui valide un ticket
     */
    public void updateDateValidation(long idTitulaire) {
        var updateRes = this.cartes.updateOne(
                new Document("id_titulaire", idTitulaire),
                new Document("$set",
                        new Document("date_derniere_validation", getDateValidation(idTitulaire))));
    }

    /**
     * M??thode qui permet de v??rifier si la carte de transport ?? un abonnement valide
     * ou un titre de transport encore valide (derni??re validation datant de moins d'une heure)
     * ou pas de titre valide mais disponible qui va ??tre consommer puis on met ?? jour la derni??re date de validation en appellant updateDateValidation()
     *
     * @param idCarte String qui correspond ?? l'ObjectId de la carte que l'on veut valider
     * @return true si la carte ?? un abonnement valide, ou un titre encore valide, ou un titre ?? valider (nombre de voyage sup??rieur ?? 0)
     * et sinon false
     */

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

    /**
     * M??thode qui permet de retourner toutes les cartes de transport pr??sente dans la base de donn??e mongo
     *
     * @return une Liste de Carte contenant toutes les cartes de transport
     */
    public List<Carte> getAllCarte() {
        List<Carte> res = new ArrayList<>();
        this.cartes.find().forEach(res::add);
        return res;
    }

    /**
     * Main avec des tests effectu?? pendant la phase de dev pour v??rifier le fonctionnement de toutes les m??thodes en Mongo
     */
    //public static void main(String[] args) {
      //  MongoDbConnection c = new MongoDbConnection();
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
        System.out.println(c.addCarteByTitu(11));
        System.out.println(c.updateNbVoyage(11,1));
        System.out.println(c.updateAbonnement(11,12));
        System.out.println(c.isValide(11));
        */
    //}

}
