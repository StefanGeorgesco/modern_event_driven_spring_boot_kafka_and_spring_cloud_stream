package fr.stefangeorgesco.eventdrivenplayground.sec08.dto;

import java.util.UUID;

public record Payment(int orderId,
                      int amount,
                      UUID paymentId) {
}
