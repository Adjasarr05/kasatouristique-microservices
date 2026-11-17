package com.ziguinchor.patrimoine.repository;

import com.ziguinchor.patrimoine.model.Reservation;
import com.ziguinchor.patrimoine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTouristeOrderByDateReservationDesc(User touriste);
    Optional<Reservation> findByQrCodeHash(String qrCodeHash);
}
