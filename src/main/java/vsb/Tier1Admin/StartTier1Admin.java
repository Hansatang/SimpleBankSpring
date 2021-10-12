package vsb.Tier1Admin;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class StartTier1Admin {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        Tier1Admin tier1Admin = new Tier1Admin("0000");

        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.println("Account number? ");
            int accountNumber = in.nextInt();
            tier1Admin.create(accountNumber);
        }

    }

}
