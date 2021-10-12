package vsb.Tier1Customer;

import java.rmi.RemoteException;
import java.util.Scanner;

public class StartTier1Customer {

    public static void main(String[] args) throws RemoteException {
        Scanner in = new Scanner(System.in);
        System.out.println("Account number? or type exit to close");
        String userTypedText = in.nextLine();
        Tier1Customer tier1Customer = new Tier1Customer(userTypedText);


        while (true) {
            int accountNumber;
            if (userTypedText.equals("exit")) {
                break;
            } else {
                accountNumber = Integer.parseInt(userTypedText);
            }
            System.out.println("Withdrawn amount? ");
            double amount = in.nextDouble();
            tier1Customer.withdraw(accountNumber, amount);

        }
    }

}
