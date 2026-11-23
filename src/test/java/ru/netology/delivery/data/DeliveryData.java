package ru.netology.delivery.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryData {
    private String city;
    private String name;
    private String phone;
}
