package ru.yandex.practicum.stellarburgers.api.model;

import java.util.Arrays;
import java.util.Date;

public class Order {
    public String _id;
    public String[] ingredients;
    public String status;
    public String name;
    public Date createdAt;
    public Date updatedAt;
    public int number;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Order{" +
                "_id='" + _id + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", number=" + number +
                '}';
    }
}
