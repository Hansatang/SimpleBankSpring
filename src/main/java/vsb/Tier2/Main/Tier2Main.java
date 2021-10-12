package vsb.Tier2.Main;


import java.rmi.registry.LocateRegistry;

public class Tier2Main {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Tier2MainController controller = new Tier2MainController();

            System.out.println("Tier25 ready");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
