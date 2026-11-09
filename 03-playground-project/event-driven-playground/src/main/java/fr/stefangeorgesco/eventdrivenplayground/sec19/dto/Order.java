package fr.stefangeorgesco.eventdrivenplayground.sec19.dto;

public record Order(int id,
                    int customerId,
                    int amount,
                    ProductType productType) {
}