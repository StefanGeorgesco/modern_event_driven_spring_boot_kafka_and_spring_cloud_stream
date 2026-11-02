package fr.stefangeorgesco.eventdrivenplayground.sec08.dto;

public record Notification(int orderId,
                           NotificationChannel channel,
                           String recipient) {
}
