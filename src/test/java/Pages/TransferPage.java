package Pages;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import Data.DataHelper;
import lombok.val;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amount = $("[data-test-id='amount'] input");
    private SelenideElement from = $("[data-test-id='from'] input");
    private SelenideElement to = $("[data-test-id='to'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement error = $("[data-test-id=error-notification]");

    public DashboardPage transferFromSecondToFirst (int sum) {
        amount.setValue(String.valueOf(sum));
        from.setValue(DataHelper.getSecondCard().getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public DashboardPage transferFromFirstToSecond (int sum) {
        amount.setValue(String.valueOf(sum));
        from.setValue(DataHelper.getFirstCard().getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }

    public void shouldExecuteError () {
        error.shouldBe(Condition.visible);
    }

}
