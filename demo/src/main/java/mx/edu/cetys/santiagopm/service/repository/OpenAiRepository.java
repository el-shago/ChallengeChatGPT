package mx.edu.cetys.santiagopm.service.repository;

import mx.edu.cetys.santiagopm.service.model.ChatInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenAiRepository extends JpaRepository<ChatInteraction, Long> {

}
