package application.programme;

import application.model.DTO.*;
import application.model.facade.IFacade;
import application.model.models.carteDeTransport.produits.ticket.ITicket;
import application.model.models.exceptions.MailDejaUtiliseException;
import application.model.models.exceptions.NbTitreNonValide;

public class ProgrammeTest {
    public static void main(String[] args) {

          /*
        IFacade facade = IFacade.creerFacade();

        try {
           facade.inscrire(new UserInscriptionDTO("deguyenne","nicolas", "nicolas@tnndev.fr","12345"));
        } catch (MailDejaUtiliseException e) {
            e.printStackTrace();
        }
        var uid = facade.connecter(new UserConnexionDTO("nicolas@tnndev.fr","12345"));
        try {
            facade.commanderTitre(new CommandeTitreDTO(uid, 10));
            System.out.println("ok");
            facade.getTickets(uid).stream().forEach(e -> System.out.println(e.getPrix()));
        } catch (NbTitreNonValide nbTitreNonValide) {
            nbTitreNonValide.printStackTrace();
        }



        long id;




        for (int i =0 ; i <= 10; i++){
            id = facade.connecter(new UserConnexionDTO("test"+i+"@tnndev.fr","12345678"+i));
            if(id != -1)
                System.out.println("user id = "+id+" [connected]");
                System.out.println("user id="+id+" [disconneted]");
        }


        try {
            id = facade.inscrire(new UserInscriptionDTO("test", "test","test99@tnndev.fr","123456781"));
            if(id != -1){
                System.out.println(id + " [created]");
            }
            if( facade.desinscrire(new UserDesinscriptionDTO("test99@tnndev.fr","123456781"))){
                System.out.println(id + " [deleted]");
            }

        } catch (MailDejaUtiliseException e) {
            System.out.println(e.getMessage());
        }

*/














    }
}
