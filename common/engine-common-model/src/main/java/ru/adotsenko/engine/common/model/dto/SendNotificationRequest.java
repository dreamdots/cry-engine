package ru.adotsenko.engine.common.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SendNotificationRequest {
    private String message;
    private NotificationDestinationType destinationType;
    private String destination;
}
