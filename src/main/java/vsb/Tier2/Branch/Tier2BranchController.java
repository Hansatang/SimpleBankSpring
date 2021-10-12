/*
 * 12.09.2018 Original version
 */

package vsb.Tier2.Branch;

import org.springframework.web.client.RestTemplate;
import vsb.common.ITier1;
import vsb.common.ITier2;
import vsb.common.ITier3;
import vsb.model.Account;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Tier2BranchController extends UnicastRemoteObject implements ITier2 {
    private static final String ROOT = "http://localhost:8080/";
    private ITier3 tier3;
    private String location;
    private HashMap<String, List<ITier1>> users = new HashMap<String, List<ITier1>>();
    private RestTemplate rest = new RestTemplate();

    public Tier2BranchController(String location) throws RemoteException {
        try {
            this.location = location;
            Naming.rebind(T2_SERVICE_NAME + location, this);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }


    public Account withdraw(int accountNumber, double amount) throws RemoteException {
        Account account = Account.fromJson(rest.postForObject(ROOT + "bankW/" + accountNumber, amount, String.class));
        System.out.println(account.getNumber());
        send(account);
        return account;
    }


    public Account insert(int accountNumber, double amount) throws RemoteException {
        Account account = Account.fromJson(rest.postForObject(ROOT + "bankI/" + accountNumber, amount, String.class));
        send(account);
        return account;

    }

    @Override
    public void createAccount(int accountNumber) throws RemoteException {
        Account account1 = Account.fromJson(rest.getForObject(ROOT + "BankF/" + accountNumber, String.class));

        if (account1 == null) {
            Account account2 = new Account(accountNumber, 0.0);
            rest.put(ROOT + "BankC", account2.toJson());

            send(account2);
        }
        else {
            send(new Account(0, 0));
        }
    }

    @Override
    public void saveClient(String userTypedText, ITier1 tier1Customer) throws RemoteException {
        System.out.println("WER");
        ArrayList<ITier1> tier1s = new ArrayList<>();
        tier1s.add(tier1Customer);
        if (users.containsKey(userTypedText)) {
            if (users.get(userTypedText).contains(tier1Customer)) {

            } else {
                users.get(userTypedText).add(tier1Customer);
            }
        } else {
            users.put(userTypedText, tier1s);
        }
        System.out.println(users.get(userTypedText).size());
    }

    private void send(Account account) {
        ITier1 tier11 = null;
        System.out.println(account.getNumber()+"helo");
        if (users.get(String.valueOf(account.getNumber())) != null) {
            for (ITier1 tier1 : users.get(String.valueOf(account.getNumber()))
            ) {
                try {
                    System.out.println("Hej1");
                    tier1.replyGet(account.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        if (users.get("0000") != null) {
            for (ITier1 tier1 : users.get("0000")
            ) {
                System.out.println(tier1.toString());
                try {
                    System.out.println("Hej2");
                    tier1.replyGet(account.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    if (users.get("0000").contains(tier1))
                    {
                       tier11= tier1;
                        System.out.println("1234567809");
                    }
                }
            }
        }
        users.get("0000").remove(tier11);
    }

}
