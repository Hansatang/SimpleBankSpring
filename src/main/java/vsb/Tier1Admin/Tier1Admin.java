/*
 * 12.09.2018 Original version
 */


package vsb.Tier1Admin;


import vsb.common.ITier1;
import vsb.common.ITier2;
import vsb.common.ITier25;
import vsb.model.Account;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import static vsb.common.ITier25.T25_SERVICE_NAME;


public class Tier1Admin implements ITier1 {

    private ITier1 iTier1;
    private ITier2 iTier2;


    public Tier1Admin(String s) throws RemoteException{
        this.iTier1 = (ITier1) UnicastRemoteObject.exportObject(this, 0);
        Scanner on = new Scanner(System.in);
        System.out.println("Where are you?");
        String location = on.nextLine();
        try {
            ITier25 tier25 = (ITier25) Naming.lookup(T25_SERVICE_NAME);
            this.iTier2 = tier25.getServer(location);
            iTier2.saveClient(s, iTier1);
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void create(int accountNumber) throws RemoteException {
        iTier2.createAccount(accountNumber);
    }

    @Override
    public void replyGet(String result) throws RemoteException {
        if (result.contains("number=0"))
        {
            System.out.println("Account number already taken");
        }
        else {
            System.out.println(result);
        }
    }
}
