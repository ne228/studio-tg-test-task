package tg.studio.task.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tg.studio.task.entity.Field;
import tg.studio.task.entity.Game;
import tg.studio.task.repository.FieldRepository;
import tg.studio.task.repository.GameRepository;
import tg.studio.task.request.GameTurnRequest;
import tg.studio.task.request.NewGameRequest;
import tg.studio.task.response.GameInfoResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final FieldRepository fieldRepository;


    public GameServiceImpl(GameRepository gameRepository, FieldRepository fieldRepository) {
        this.gameRepository = gameRepository;
        this.fieldRepository = fieldRepository;
    }

    @Override
    @Transactional
    public GameInfoResponse create(NewGameRequest request) throws Exception {
        var game = new Game(request.getWidth(), request.getHeight(), request.getMineCount());
        var fields = new ArrayList<Field>();
        int mineIndex = 0;
        var mineIndexes = genMineIndexes(request.getMineCount(), request.getHeight() * request.getWidth());
        for (int row = 0; row < request.getHeight(); row++)
            for (int col = 0; col < request.getWidth(); col++) {
                var field = new Field(row, col, mineIndexes.contains(mineIndex), game);
                fields.add(field);
                mineIndex++;
            }
        var fieldProcessor = new FieldProcessor(fields, request.getHeight(), request.getWidth());
        for (var field : fields) {
            var behindFields = fieldProcessor.getBehindFields(field);
            var countMinesBehind = behindFields
                    .stream().filter(f -> f.isMine())
                    .count();
            field.setBehindMinesCount((int) countMinesBehind);
        }

        var savedGame = gameRepository.save(game);
        if (savedGame == null)
            throw new Exception("Ошибка сохранения game");

        var savedFields = fieldRepository.saveAll(fields);
        if (savedFields.size() != fields.size())
            throw new Exception("Ошибка сохранения fields");

        return new GameInfoResponse(savedGame, savedFields);
    }

    @Override
    public GameInfoResponse turn(GameTurnRequest request) throws Exception {
        var game = gameRepository.findByGameId(request.getGameId());
        if (game == null)
            throw new Exception("Не найдена game c game_id=" + request.getGameId());

        if (game.isCompleted())
            throw new Exception("Игра закончена game_id=" + request.getGameId());

        var fields = fieldRepository.findAllByGame_GameIdOrderByRowAscColAsc(request.getGameId());
        var fieldProcessor = new FieldProcessor(fields, game.getHeight(), game.getWidth());

        var targetField = fieldProcessor.getFieldByPos(request.getRow(), request.getCol());

        if (targetField.isOpen())
            throw new Exception("Ячейка уже открыта");
        // Mine
        if (targetField.isMine()) {
            fields.forEach(fieldProcessor::openFieldLose);
            game.setCompleted(true);
        } else {
            fieldProcessor.turnField(targetField);
        }

        // Проверка на победу
        int f = (int) fields.stream().filter(Field::isOpen).count();
        int s = fields.size() - game.getMinesCount();
        if (f == s) {
            fields.forEach(fieldProcessor::openFieldWin);
            game.setCompleted(true);
        }


        gameRepository.save(game);
        fieldRepository.saveAll(fields);


        return new GameInfoResponse(game, fields);
    }

    /**
     * Генерирует индексы для мин
     *
     * @param mine_count количество мин
     * @param size       размер height x width игрового поля
     */
    private Set<Integer> genMineIndexes(int mine_count, int size) {
        Set<Integer> numbers = new HashSet<>();
        Random random = new Random();

        while (numbers.size() < mine_count) {
            int num = random.nextInt(size + 1);
            numbers.add(num);
        }
        return numbers;
    }
}
