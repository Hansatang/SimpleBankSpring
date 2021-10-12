package vsb.alternativeTier3;/*
 * 03.10.2021 Original version
 */


import org.springframework.web.bind.annotation.*;
import vsb.model.Account;

import java.sql.*;


@RestController
public class AltTier3Controller {

    private final Connection connection =
            DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "qwerty");
    private final Statement statement = connection.createStatement();

    public AltTier3Controller() throws SQLException {
    }


    @GetMapping("/BankF/{phoneNo}")
    public synchronized Account getAccount(@PathVariable(value = "phoneNo") int accountNumber) throws SQLException {
        Account account = null;
        ResultSet resultSet = statement.executeQuery
                ("SELECT * FROM bank.accounts WHERE id = " + accountNumber);
        while (resultSet.next()) {
            account = new Account(resultSet.getInt(1), resultSet.getDouble(2));
        }
        assert account != null;
        System.out.println(account);
        return account;
    }

    @PostMapping("/bankW/{phoneNo}")
    public synchronized Account withdrawAccount(@PathVariable(value = "phoneNo") int accountNumber, @RequestBody double amount) {
        try {
            Account account = getAccount(accountNumber);
            System.out.println(account.getBalance() + "HA");
            System.out.println(amount + "HI");
            account.setBalance(account.getBalance() - amount);
            System.out.println(account.getBalance() + "HE");
            statement.execute
                    ("UPDATE bank.accounts SET balance = " + account.getBalance()
                            + " WHERE id = " + accountNumber);
            return account;

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/bankI/{phoneNo}")
    public synchronized Account insertAccount(@PathVariable(value = "phoneNo") int accountNumber, @RequestBody double amount) {
        try {
            Account account = getAccount(accountNumber);
            account.setBalance(account.getBalance() + amount);
            statement.execute
                    ("UPDATE bank.accounts SET balance = " + account.getBalance()
                            + " WHERE id = " + accountNumber);
            return account;

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/BankC")
    public synchronized Account createAccount(@RequestBody String json) {
        Account f = Account.fromJson(json);
        try {
            statement.execute("INSERT INTO bank.accounts (id,balance) VALUES ("
                    + f.getNumber() + "," + f.getBalance() + ")");
            return getAccount(f.getNumber());
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return null;
    }
}
