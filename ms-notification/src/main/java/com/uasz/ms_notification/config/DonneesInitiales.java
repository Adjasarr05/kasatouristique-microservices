package com.uasz.ms_notification.config;

import com.uasz.ms_notification.entities.Notification;
import com.uasz.ms_notification.repositories.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DonneesInitiales implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    public DonneesInitiales(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) {
        if (notificationRepository.count() > 0) {
            return;
        }

        Notification n1 = new Notification();
        n1.setUtilisateurId(5L);
        n1.setDestinataire("visiteur@ziguinchor.sn");
        n1.setCanal("EMAIL");
        n1.setTypeNotification("CONFIRMATION");
        n1.setSujet("Confirmation de votre visite : Cathédrale Saint Antoine");
        n1.setMessage("Bonjour Visiteur Test, votre réservation pour la visite guidée de la Cathédrale Saint Antoine est confirmée.");
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setUtilisateurId(5L);
        n2.setDestinataire("+221 77 100 00 05");
        n2.setCanal("SMS");
        n2.setTypeNotification("RAPPEL");
        n2.setSujet("Rappel de visite à Ziguinchor");
        n2.setMessage("Rappel : votre visite guidée aura lieu prochainement au centre-ville de Ziguinchor.");
        notificationRepository.save(n2);
    }
}
