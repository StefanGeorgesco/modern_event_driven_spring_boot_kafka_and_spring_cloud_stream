package fr.stefangeorgesco.eventdrivenplayground.sec07.dto;

import java.util.UUID;

public record Payment(int orderId,
                      int amount,
                      UUID paymentId) {
}
