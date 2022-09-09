package de.judgeman.EmailService.Repositories;

import de.judgeman.EmailService.Model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface EmailRepository extends CrudRepository<Email, Long> {

    ArrayList<Email> findAllByOrderByIdDesc();

    Page<Email> findAllByOrderByIdDesc(Pageable pageable);
}
