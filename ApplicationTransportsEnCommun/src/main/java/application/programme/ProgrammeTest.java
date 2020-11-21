package application.programme;

import application.model.DTO.UserConnexionDTO;
import application.model.DTO.UserInscriptionDTO;
import application.model.facade.IFacade;

public class ProgrammeTest {
    public static void main(String[] args) {

        IFacade facade = IFacade.creerFacade();
        long id;

        for (int i =0 ; i <= 10; i++){
            id = facade.inscrire(new UserInscriptionDTO("test", "nico", "12345678"+i, "test"+i+"@tnndev.fr"));
            System.out.println("user id = "+id + "[created]");
        }

        id = facade.connecter(new UserConnexionDTO("test1@tnndev.fr","123456781"));
        System.out.println("user id = "+id+" [connected]");

    }
}
