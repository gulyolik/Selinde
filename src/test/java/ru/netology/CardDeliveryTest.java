package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
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

    public void planningDateGeneration(int plusDays) {
        LocalDateTime dateTime = LocalDate.now().plusDays(plusDays).atTime(LocalTime.MIN);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
        String millis = Long.toString(zonedDateTime.toInstant().toEpochMilli());

        final ElementsCollection days = $$x("//*[@data-day]");
        final SelenideElement dateInput = $x("//*[@data-test-id = 'date']//input");
        final SelenideElement nextMonthButton = $x("//*[data-step = '1']");


        List<String> millisPerDay = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        for (SelenideElement element : days) {
            millisPerDay.add(element.getAttribute("data-day"));
        }

        dateInput.click();
        while (!millisPerDay.contains(millis)) {
            nextMonthButton.click();
            for (SelenideElement element : days) {
                tmp.add(element.getAttribute("data-day"));
                millisPerDay = tmp;
            }
        }
        days.findBy(Condition.attribute("data-day", millis)).click();
    }



        @Test
        void shouldCheckCityNameValidation() {
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = date.plusDays(5);
            String planningDate = formatters.format(date);
            Configuration.holdBrowserOpen = true;
            open("http:/localhost:9999/");
            $("[data-test-id = 'city'] input").setValue("Во");
            $x("//*[contains(text(),'Вологда')]").click();
            $("[data-test-id = 'date'] input").sendKeys((Keys.CONTROL + "A"), Keys.DELETE);
            $("[data-test-id = 'date'] input").setValue(planningDate);
            $(By.className("icon-button__content")).click();
            $x("//*[contains(text(), '25')]").click();
            $("[data-test-id = 'name'] input").setValue("Гуляева Ольга");
            $("[data-test-id = 'phone'] input").setValue("+79967737496");
            $(By.className("checkbox__box")).click();
            $x("//*[contains(text(), 'Забронировать')]").click();
            $("[data-test-id = 'notification']").should(appear, Duration.ofSeconds(12)).shouldBe(visible);
        }

        @Test
        void shouldChooseDateWithNewWay() {
            Configuration.holdBrowserOpen = true;
            Configuration.timeout = 10000;
            open("http:/localhost:9999/");
            $("[data-test-id = 'city'] input").setValue("Вол");
            $x("//*[contains(text(),'Вологда')]").click();
            $(By.className("icon-button__content")).click();
            this.planningDateGeneration(5);
            $("[data-test-id = 'name'] input").setValue("Гуляева Ольга");
            $("[data-test-id = 'phone'] input").setValue("+79967737496");
            $(By.className("checkbox__box")).click();
            $x("//*[contains(text(), 'Забронировать')]").click();
            $("[data-test-id = 'notification']").should(appear, Duration.ofSeconds(12)).shouldBe(visible);
        }

    }
