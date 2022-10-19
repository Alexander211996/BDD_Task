package transferTests;

import data.DataHelper;
import data.SUTcorrector;
import org.junit.jupiter.api.BeforeAll;
import pages.DashboardPage;
import pages.LoginPage;
import pages.TransferPage;

import lombok.val;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class transferTests {

    @BeforeAll
    public static void openUrl() {
        open("http://localhost:9999/");
        val loginPage = new LoginPage();
        val verificationPage = loginPage.validLogin(DataHelper.getAuthInfo());
        verificationPage.validVerify(DataHelper.getVerificationCode(DataHelper.getAuthInfo()));
    }

    @BeforeEach
    public void correctSUT() {
        val corrector = new SUTcorrector();
        corrector.setUp();
    }


    @Test
    void shouldTransferFromSecondToFirst() {
        int transferSum = 5000;
        val dashBoardPage = new DashboardPage();
        val firstCardInfo = DataHelper.getFirstCard();
        val firstCardInitialBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardInfo = DataHelper.getSecondCard();
        val secondCardInitialBalance = dashBoardPage.getCardBalance(secondCardInfo);
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferMoney(transferSum, DataHelper.getSecondCard().getCardNumber());
        val firstCardFinalBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardFinalBalance = dashBoardPage.getCardBalance(secondCardInfo);
        int firstCardExpectedBalance = firstCardInitialBalance + transferSum;
        int secondCardExpectedBalance = secondCardInitialBalance - transferSum;
        assertEquals(firstCardExpectedBalance, firstCardFinalBalance);
        assertEquals(secondCardExpectedBalance, secondCardFinalBalance);
    }

    @Test
    void shouldTransferFromFirstToSecond() {
        int transferSum = 5000;
        val dashBoardPage = new DashboardPage();
        val firstCardInfo = DataHelper.getFirstCard();
        val firstCardInitialBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardInfo = DataHelper.getSecondCard();
        val secondCardInitialBalance = dashBoardPage.getCardBalance(secondCardInfo);
        dashBoardPage.transferToSecondCard();
        val transferPage = new TransferPage();
        transferPage.transferMoney(transferSum, DataHelper.getFirstCard().getCardNumber());
        val firstCardFinalBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardFinalBalance = dashBoardPage.getCardBalance(secondCardInfo);
        int firstCardExpectedBalance = firstCardInitialBalance - transferSum;
        int secondCardExpectedBalance = secondCardInitialBalance + transferSum;
        assertEquals(firstCardExpectedBalance, firstCardFinalBalance);
        assertEquals(secondCardExpectedBalance, secondCardFinalBalance);
    }

    @Test
    void shouldNotTransferMoneyIfNotEnoughMoney() {
        int transferSum = 40000;
        val dashBoardPage = new DashboardPage();
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferMoney(transferSum, DataHelper.getSecondCard().getCardNumber());
        transferPage.shouldExecuteError();
    }

    @Test
    void shouldNotTransferNull() {
        int transferSum = 0;
        val dashBoardPage = new DashboardPage();
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferMoney(transferSum, DataHelper.getSecondCard().getCardNumber());
        transferPage.shouldExecuteError();
    }

    @Test
    void shouldNotTransferWithNoSum() {
        val dashBoardPage = new DashboardPage();
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferMoney(null, DataHelper.getSecondCard().getCardNumber());
        transferPage.shouldExecuteError();
    }
}
