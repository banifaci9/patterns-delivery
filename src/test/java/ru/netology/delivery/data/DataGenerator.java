package ru.netology.delivery.data;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static final Faker FAKER = new Faker(new Locale("ru"));
    private static final Random RANDOM = new Random();

    private static final String[] CITIES = {
            "Москва",
            "Санкт-Петербург",
            "Казань",
            "Нижний Новгород",
            "Екатеринбург"
    };

    private DataGenerator() {
        // Utility class
    }

    public static String generateCity() {
        return CITIES[RANDOM.nextInt(CITIES.length)];
    }

    public static String generateName() {
        String lastName = cleanRussianName(FAKER.name().lastName());
        String firstName = cleanRussianName(FAKER.name().firstName());

        if (lastName.isEmpty() || firstName.isEmpty()) {
            return "Иван Петров";
        }

        return lastName + " " + firstName;
    }

    private static String cleanRussianName(String value) {
        return value
                .replace('Ё', 'Е')
                .replace('ё', 'е')
                .replaceAll("[^А-Яа-я -]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String generatePhone() {
        return String.format(
                "+79%09d",
                RANDOM.nextInt(1_000_000_000)
        );
    }

    public static DeliveryData generateClient() {
        return new DeliveryData(
                generateCity(),
                generateName(),
                generatePhone()
        );
    }
}
