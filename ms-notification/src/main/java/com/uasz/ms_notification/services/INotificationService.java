package com.uasz.ms_notification.services;

import com.uasz.ms_notification.entities.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    Notification envoyerNotification(Notification notification);

    List<Notification> getAllNotifications();

    List<Notification> getByUtilisateurId(Long utilisateurId);

    Optional<Notification> getByIdNotification(Long id);

    void deleteNotification(Long id);
}
