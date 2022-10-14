package Pages;

import Data.DataHelper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    private SelenideElement heading = $("[data-test-id=dashboard]");

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public int getCardBalance (DataHelper.CardInfo cardInfo) {
        String id = cardInfo.getId();
        SelenideElement card = $ (withText(id));
        val text =card.text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }


        private ElementsCollection transferButtons = $$("[data-test-id='action-deposit']");
        private SelenideElement transferToFirstCardButton = transferButtons.first();
        private SelenideElement transferToSecondCardButton = transferButtons.last();

        public void transferToFirstCard() {
            transferToFirstCardButton.click();
        }

        public void transferToSecondCard() {
            transferToSecondCardButton.click();
        }

}