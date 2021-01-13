package application.model.bdd;

import application.model.DTO.CommandeTitreDTO;
import application.model.facade.IFacade;

public class Fixture {


    public static void main(String[] args) {

        /**
         * FIXTURE : Pour créer un jeu de données avec les utilisateurs dans la base MySQL
         * Pour chaque utilisateur dans la base mysql :
         *          - on lui met un abonnement ou des voyages aléatoirement
         *          - on lui crée une carte
         */

        MySQLBddConnection mySQLBddConnection = new MySQLBddConnection();
        MongoDbConnection mongoDbConnection = new MongoDbConnection();

        IFacade facade = IFacade.creerFacade();

        int min = 1;
        int max = 3;
        int range = max - min + 1;

       mySQLBddConnection.getAllUser().forEach(
               user -> {
                   int rand = (int)(Math.random() * range + min) ;
                   System.out.println(rand);
                    mongoDbConnection.addCarteByTitu(user.getId());

                   switch (rand){
                       case 1:
                           facade.souscrireAbonnement1An(user.getId());
                           break;
                       case 2:
                           facade.commmander1Voyage(user.getId());
                           break;
                       case 3:
                           facade.commmander10Voyages(user.getId());
                           break;
                   }

               });




    }
}

