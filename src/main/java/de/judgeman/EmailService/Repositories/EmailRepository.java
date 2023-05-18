package de.judgeman.EmailService.Repositories;

import de.judgeman.EmailService.Model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface EmailRepository extends CrudRepository<Email, Long> {

    ArrayList<Email> findAllByOrderByIdDesc();

    Page<Email> findAllByOrderByIdDesc(Pageable pageable);

    ArrayList<Email> findBySendingDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
