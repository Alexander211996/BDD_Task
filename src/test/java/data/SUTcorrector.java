package data;

import lombok.val;
import pages.DashboardPage;
import pages.TransferPage;

public class SUTcorrector {
    public void setUp() {
        val dashBoardPage = new DashboardPage();
        val firstCardInitialBalance = dashBoardPage.getCardBalance(DataHelper.getFirstCard());
        val secondCardInitialBalance = dashBoardPage.getCardBalance(DataHelper.getSecondCard());
        //Дальше реализована проверка и приведение SUT в состояние, когда обе карты имеют одинаковый положительный баланс
        int cardBalanceDifferenceFirstSecond = firstCardInitialBalance - secondCardInitialBalance;
        int cardBalanceDifferenceSecondFirst = secondCardInitialBalance - firstCardInitialBalance;
        if (cardBalanceDifferenceFirstSecond > 0) {
            dashBoardPage.transferToSecondCard();
            val transferPage = new TransferPage();
            transferPage.transferMoney(cardBalanceDifferenceFirstSecond / 2,
                    DataHelper.getSecondCard().getCardNumber());
        } else {
            if (cardBalanceDifferenceSecondFirst > 0) {
                dashBoardPage.transferToFirstCard();
                val transferPage = new TransferPage();
                transferPage.transferMoney(cardBalanceDifferenceSecondFirst / 2,
                        DataHelper.getFirstCard().getCardNumber());
            }
        }

    }
}
