package application.programme;

import application.model.DTO.SouscriptionDTO;
import application.model.DTO.UserConnexionDTO;
import application.model.DTO.UserDesinscriptionDTO;
import application.model.DTO.UserInscriptionDTO;
import application.model.facade.IFacade;
import application.model.models.exceptions.MailDejaUtiliseException;

public class ProgrammeTest {
    public static void main(String[] args) {

        IFacade facade = IFacade.creerFacade();
        /*
        try {
           facade.inscrire(new UserInscriptionDTO("deguyenne","nicolas", "nico@tnndev.fr","12345"));
        } catch (MailDejaUtiliseException e) {
            e.printStackTrace();
        }
*/
        var uid = facade.connecter(new UserConnexionDTO("nico@tnndev.fr","12345"));

        System.out.println("Connected : "+facade.isConnected(uid));


        System.out.println("Abonnement mensuel : "+facade.souscrireUnAbonnement(new SouscriptionDTO(101, "mensuel")));



        /*
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
