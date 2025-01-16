package tg.studio.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.studio.task.entity.Game;


@Repository
public interface GameRepository extends JpaRepository<Game, String> {

    Game findByGameId(String gameId);
//    Game findByGameIdOOrderByField(String gameId);

}
