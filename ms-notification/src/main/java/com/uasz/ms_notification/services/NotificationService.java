package com.uasz.ms_notification.services;

import com.uasz.ms_notification.entities.Notification;
import com.uasz.ms_notification.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification envoyerNotification(Notification notification) {
        Notification saved = notificationRepository.save(notification);
        log.info("Notification [{}] envoyée à {} ({}) : {}",
                saved.getCanal(), saved.getDestinataire(), saved.getTypeNotification(), saved.getSujet());
        return saved;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getByUtilisateurId(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdOrderByDateEnvoiDesc(utilisateurId);
    }

    @Override
    public Optional<Notification> getByIdNotification(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
