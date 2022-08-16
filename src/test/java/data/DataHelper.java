package data;

import lombok.Data;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;


public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfoFirstUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getAuthInfoSecondUser() {
        return new AuthInfo("petya", "123qwerty");
    }

    public static AuthInfo getAuthInfoWrongPassword() {
        return new AuthInfo("petya", "321123");
    }

    public static AuthInfo getAuthInfoWrongLogin() {
        return new AuthInfo("kolya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        private String login;
        private String code;
    }

    public static VerificationCode getVerificationCodeFor() {
        val verificationLoginSQL = "SELECT login FROM auth_codes JOIN users ON user_id = users.id ORDER BY created DESC LIMIT 1;";
        val verificationCodeSQL = "SELECT code FROM auth_codes JOIN users ON user_id = users.id ORDER BY created DESC LIMIT 1;";
        val runner = new QueryRunner();
        String login;
        String verificationCode;

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            val logon = runner.query(conn, verificationLoginSQL, new ScalarHandler<>());
            val code = runner.query(conn, verificationCodeSQL, new ScalarHandler<>());
            login = (String) logon;
            verificationCode = (String) code;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new VerificationCode(login, verificationCode);
    }

    public static void clearDB() {

        val deleteAuthCodes = "DELETE FROM auth_codes;";
        val deleteCardTransactions = "DELETE FROM card_transactions;";
        val deleteCards = "DELETE FROM cards;";
        val deleteUsers = "DELETE FROM users;";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, deleteAuthCodes);
            runner.update(conn, deleteCardTransactions);
            runner.update(conn, deleteCards);
            runner.update(conn, deleteUsers);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class Cards {
        private final String id;
        private final String number;
        private final String balance;
    }

    @Value
    public static class Transfers {
        private final String from;
        private final String to;
        private final String amount;
    }

    @Value
    public static class CardData {
        private String number;
    }

    public static CardData getFirstCardNumber() {
        return new CardData("5559 0000 0000 0001");
    }

    public static CardData getSecondCardNumber() {
        return new CardData("5559 0000 0000 0002");
    }
}