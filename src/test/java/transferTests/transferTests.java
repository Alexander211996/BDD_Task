package transferTests;

import Data.DataHelper;
import Pages.DashboardPage;
import Pages.LoginPage;
import Pages.TransferPage;
import com.codeborne.selenide.Selenide;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class transferTests {
    @BeforeEach
    public void openUrl() {
        open("http://localhost:9999/");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashBoardPage = new DashboardPage();
        val firstCardInfo = DataHelper.getFirstCard();
        val firstCardInitialBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardInfo = DataHelper.getSecondCard();
        val secondCardInitialBalance = dashBoardPage.getCardBalance(secondCardInfo);
        //Дальше реализована проверка и приведение SUT в начальное состояние, когда обе карты имеют одинаковый положительный баланс
        int cardBalanceDifferenceFirstSecond = firstCardInitialBalance - secondCardInitialBalance;
        int cardBalanceDifferenceSecondFirst = secondCardInitialBalance - firstCardInitialBalance;
        if (cardBalanceDifferenceFirstSecond > 0) {
            dashBoardPage.transferToSecondCard();
            val transferPage = new TransferPage();
            transferPage.transferFromFirstToSecond(cardBalanceDifferenceFirstSecond / 2);
        } else {
            if (cardBalanceDifferenceSecondFirst > 0) {
                dashBoardPage.transferToFirstCard();
                val transferPage = new TransferPage();
                transferPage.transferFromSecondToFirst(cardBalanceDifferenceSecondFirst / 2);
            }
        }

    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }


    @Test
    void shouldTransferFromSecondToFirst () {
        val dashBoardPage = new DashboardPage();
        val firstCardInfo = DataHelper.getFirstCard();
        val firstCardInitialBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardInfo = DataHelper.getSecondCard();
        val secondCardInitialBalance = dashBoardPage.getCardBalance(secondCardInfo);
        int transferSum = 5000;
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferFromSecondToFirst(transferSum);
        val firstCardFinalBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardFinalBalance = dashBoardPage.getCardBalance(secondCardInfo);
        int firstCardExpectedBalance = firstCardInitialBalance + transferSum;
        int secondCardExpectedBalance = secondCardInitialBalance - transferSum;
        assertEquals (firstCardExpectedBalance, firstCardFinalBalance);
        assertEquals(secondCardExpectedBalance, secondCardFinalBalance);
    }

    @Test
    void shouldTransferFromFirstToSecond () {
        val dashBoardPage = new DashboardPage();
        val firstCardInfo = DataHelper.getFirstCard();
        val firstCardInitialBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardInfo = DataHelper.getSecondCard();
        val secondCardInitialBalance = dashBoardPage.getCardBalance(secondCardInfo);
        int transferSum = 5000;
        dashBoardPage.transferToSecondCard();
        val transferPage = new TransferPage();
        transferPage.transferFromFirstToSecond(transferSum);
        val firstCardFinalBalance = dashBoardPage.getCardBalance(firstCardInfo);
        val secondCardFinalBalance = dashBoardPage.getCardBalance(secondCardInfo);
        int firstCardExpectedBalance = firstCardInitialBalance - transferSum;
        int secondCardExpectedBalance = secondCardInitialBalance + transferSum;
        assertEquals(firstCardExpectedBalance, firstCardFinalBalance);
        assertEquals(secondCardExpectedBalance, secondCardFinalBalance);
    }

    @Test
    void shouldNotTransferMoneyIfNotEnoughMoney () {
        val dashBoardPage = new DashboardPage();
        int transferSum = 40000;
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferFromSecondToFirst(transferSum);
        transferPage.shouldExecuteError();
    }

    @Test
    void shouldNotTransferNull () {
        val dashBoardPage = new DashboardPage();
        int transferSum = 0;
        dashBoardPage.transferToFirstCard();
        val transferPage = new TransferPage();
        transferPage.transferFromSecondToFirst(transferSum);
        transferPage.shouldExecuteError();
    }
}
