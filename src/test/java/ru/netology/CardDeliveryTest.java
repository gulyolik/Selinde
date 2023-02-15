package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }


    public void planningDateGeneration(int plusDays) {
        LocalDate selectedDate = LocalDate.now().plusDays(3);
        LocalDate requiredDate = LocalDate.now().plusDays(plusDays);

        int deltaMonths = (requiredDate.getYear() - selectedDate.getYear()) * 12 +
                (requiredDate.getMonthValue() - selectedDate.getMonthValue());

        for (int i = 0; i < deltaMonths; i++)
            $(".popup [data-step='1']").click();

        $$(".popup_visible .calendar__layout td")
                .findBy(text(generateDate(plusDays, "d"))).click();
    }


     @Test
    void shouldCheckCityNameValidation() {
        Configuration.holdBrowserOpen = true;
        open("http:/localhost:9999/");
        $("[data-test-id = 'city'] input").sendKeys((Keys.CONTROL + "A"), Keys.DELETE);
        $("[data-test-id = 'city'] input").setValue("Вологда");
        $("[data-test-id = 'date'] input").sendKeys((Keys.CONTROL + "A"), Keys.DELETE);
        $("[data-test-id = 'date'] input").setValue(generateDate(5, "dd.MM.yyyy"));
        $("[data-test-id = 'name'] input").setValue("Гуляева Ольга");
        $("[data-test-id = 'phone'] input").setValue("+79967737496");
        $(By.className("checkbox__box")).click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id = 'notification'] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(12)).shouldHave(exactText("Успешно!"));
        $("[data-test-id = 'notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(12))
                .shouldHave(exactText("Встреча успешно забронирована на " + generateDate(5, "dd.MM.yyyy")));
    }

    @Test
    void shouldChooseDateWithNewWay() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 10000;
        open("http:/localhost:9999/");
        $("[data-test-id = 'city'] input").setValue("Вол");
        $x("//*[contains(text(),'Вологда')]").click();
        $(By.className("icon-button__content")).click();
        this.planningDateGeneration(7);
        $("[data-test-id = 'name'] input").setValue("Гуляева Ольга");
        $("[data-test-id = 'phone'] input").setValue("+79967737496");
        $(By.className("checkbox__box")).click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id = 'notification'] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(12)).shouldHave(exactText("Успешно!"));
        $("[data-test-id = 'notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(12))
                .shouldHave(exactText("Встреча успешно забронирована на "  + generateDate(7,"dd.MM.yyyy")));
    }

}
