package ru.netology.delivery;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.DeliveryData;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");


@BeforeAll
static void setUp() {
    Configuration.baseUrl = "http://localhost:9999";
    Configuration.browser = "chrome";
    Configuration.headless = true;
    Configuration.timeout = 10000;
}

    @Test
    void shouldRescheduleDelivery() {
        DeliveryData client = DataGenerator.generateClient();

        LocalDate firstDate = LocalDate.now().plusDays(4);
        LocalDate secondDate = LocalDate.now().plusDays(7);

        String firstDateText = firstDate.format(DATE_FORMAT);
        String secondDateText = secondDate.format(DATE_FORMAT);

        open("/");

        // Заполняем город, имя и телефон.
        $("[data-test-id='city'] input").setValue(client.getCity());
        $("[data-test-id='name'] input").setValue(client.getName());
        $("[data-test-id='phone'] input").setValue(client.getPhone());

        // Выбираем первую дату через календарь: у поля даты есть маска,
        // поэтому обычный setValue мог оставить прежнее значение.
        selectDate(firstDate);

        $("[data-test-id='agreement']").click();

        // Создаём первую заявку.
        $$("button").findBy(exactText("Запланировать")).click();

        $("[data-test-id='success-notification']")
                .shouldBe(visible)
                .shouldHave(text(firstDateText));

        // Выбираем другую дату и повторно отправляем ту же заявку.
        selectDate(secondDate);

        $$("button").findBy(exactText("Запланировать")).click();

        // Приложение должно предложить перепланирование.
        SelenideElement replanNotification =
                $("[data-test-id='replan-notification']");

        replanNotification.shouldBe(visible);
        replanNotification.$$("button")
                .findBy(exactText("Перепланировать"))
                .click();

        // Проверяем, что встреча перенесена на новую дату.
        $("[data-test-id='success-notification']")
                .shouldBe(visible)
                .shouldHave(text(secondDateText));
    }

    private void selectDate(LocalDate date) {
        // Открываем календарь кнопкой с иконкой внутри поля даты.
        $("[data-test-id='date'] button").click();
        $(".calendar").shouldBe(visible);

        // Переключаем календарь на нужный месяц, если дата перешла
        // через границу месяца или года.
        long monthDifference = ChronoUnit.MONTHS.between(
                YearMonth.now(),
                YearMonth.from(date)
        );

        for (long i = 0; i < monthDifference; i++) {
            $(".calendar__arrow_direction_right").click();
        }

        for (long i = 0; i > monthDifference; i--) {
            $(".calendar__arrow_direction_left").click();
        }

        // Выбираем день в открытом месяце.
        $$(".calendar__day")
                .findBy(exactText(String.valueOf(date.getDayOfMonth())))
                .click();

        // Убеждаемся, что поле действительно обновилось.
        $("[data-test-id='date'] input")
                .shouldHave(value(date.format(DATE_FORMAT)));
    }
}
