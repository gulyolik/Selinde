package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @Test
    void shouldCheckCityNameValidation() {
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 10000;
        open("http:/localhost:9999/");
        $("[data-test-id = 'city'] input").setValue("Вол");
        $x("//*[contains(text(),'Вологда')]").click();
        $(By.className("icon-button__content")).click();
        $x("//*[contains(text(), '21')]").click();
        $("[data-test-id = 'name'] input").setValue("Гуляева Ольга");
        $("[data-test-id = 'phone'] input").setValue("+79967737496");
        $(By.className("checkbox__box")).click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id = 'notification']").should(appear, Duration.ofSeconds(10)).shouldBe(visible);
    }

    @Test
    void shouldChooseDateWithNewWay() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Configuration.holdBrowserOpen = true;
        Configuration.timeout = 10000;
        open("http:/localhost:9999/");
        $("[data-test-id = 'city'] input").setValue("Вол");
        $x("//*[contains(text(),'Вологда')]").click();
        $(By.className("icon-button__content")).click();
        $(By.className("calendar__day_state_today")).click();
        $("[data-test-id = 'name'] input").setValue("Гуляева Ольга");
        $("[data-test-id = 'phone'] input").setValue("+79967737496");
        $(By.className("checkbox__box")).click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id = 'notification']").should(appear, Duration.ofSeconds(10)).shouldBe(visible);
    }
}
