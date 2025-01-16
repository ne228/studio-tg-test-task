package tg.studio.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.studio.task.entity.Field;

import java.util.List;

public interface FieldRepository extends JpaRepository<Field, String> {
    List<Field> findAllByGame_GameIdOrderByRowAscColAsc(String gameId);
}
