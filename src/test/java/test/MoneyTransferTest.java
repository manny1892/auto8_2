package test;

import data.APIHelper;
import data.DataHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoneyTransferTest {

    @AfterAll
    static void clearDB() {
        DataHelper.clearDB();
    }

    @Test
    void shouldTransfer() {
        var amount = "100";
        var amountForAssert = Integer.parseInt(amount);

        APIHelper.login(DataHelper.getAuthInfoFirstUser());
        var token = APIHelper.verification(DataHelper.getVerificationCodeFor());
        var cardOneBeforeTransfer = Integer.parseInt(APIHelper.getCards(token).get(0).getBalance());
        var cardTwoBeforeTransfer = Integer.parseInt(APIHelper.getCards(token).get(1).getBalance());
        var transfers = new DataHelper.Transfers(DataHelper.getFirstCardNumber().getNumber(), DataHelper.getSecondCardNumber().getNumber(), amount);
        APIHelper.transfer(transfers, token);
        var cardOneAfterTransfer = Integer.parseInt(APIHelper.getCards(token).get(0).getBalance());
        var cardTwoAfterTransfer = Integer.parseInt(APIHelper.getCards(token).get(1).getBalance());
        Assertions.assertEquals(cardOneBeforeTransfer + amountForAssert, cardOneAfterTransfer);
        Assertions.assertEquals(cardTwoBeforeTransfer - amountForAssert, cardTwoAfterTransfer);

    }

    @Test
    void doNotShouldTransferNegativeSum() {
        var amount = "-800000";
        APIHelper.login(DataHelper.getAuthInfoFirstUser());
        var token = APIHelper.verification(DataHelper.getVerificationCodeFor());
        var cardOneBeforeTransfer = Integer.parseInt(APIHelper.getCards(token).get(0).getBalance());
        var cardTwoBeforeTransfer = Integer.parseInt(APIHelper.getCards(token).get(1).getBalance());
        var transfers = new DataHelper.Transfers(DataHelper.getFirstCardNumber().getNumber(), DataHelper.getSecondCardNumber().getNumber(), amount);
        APIHelper.transfer(transfers, token);
        var cardOneAfterTransfer = Integer.parseInt(APIHelper.getCards(token).get(0).getBalance());
        var cardTwoAfterTransfer = Integer.parseInt(APIHelper.getCards(token).get(1).getBalance());
        Assertions.assertEquals(cardOneBeforeTransfer, cardOneAfterTransfer);
        Assertions.assertEquals(cardTwoBeforeTransfer, cardTwoAfterTransfer);

    }
}

