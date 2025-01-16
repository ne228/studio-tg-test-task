package tg.studio.task;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tg.studio.task.entity.Field;
import tg.studio.task.entity.Game;
import tg.studio.task.repository.FieldRepository;
import tg.studio.task.repository.GameRepository;
import tg.studio.task.request.GameTurnRequest;
import tg.studio.task.request.NewGameRequest;
import tg.studio.task.response.GameInfoResponse;
import tg.studio.task.service.FieldProcessor;
import tg.studio.task.service.GameService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestTaskApplicationTests {
    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @BeforeEach
    public void setUp() {
        gameRepository.deleteAll();
        fieldRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateNewGame() throws Exception {
        NewGameRequest request = new NewGameRequest(5, 4, 2);

        // Вызов метода create
        GameInfoResponse response = gameService.create(request);

        // Проверяем, что игра была сохранена
        assertNotNull(response);
        assertNotNull(response.getGameId());

        // Проверяем сохранение игры в базе
        Game savedGame = gameRepository.findById(response.getGameId()).orElseThrow();
        assertEquals(request.getWidth(), savedGame.getWidth());
        assertEquals(request.getHeight(), savedGame.getHeight());
        assertEquals(request.getMineCount(), savedGame.getMinesCount());


        var fields = fieldRepository.findAllByGame_GameIdOrderByRowAscColAsc(savedGame.getGameId());
        long mineCount = fields.stream().filter(Field::isMine).count();
        assertEquals(mineCount, 2);
        assertEquals(fields.size(), 20);
    }

    @Test
    @Transactional
    public void testTurn_oneBehindMinesField() throws Exception {
        var game = new Game(3, 4, 1);
        var fields = new ArrayList<Field>();
        for (int row = 0; row < game.getHeight(); row++)
            for (int col = 0; col < game.getWidth(); col++) {
                var isMine = false;
                if (row == 1 && col == 1)
                    isMine = true;
                var field = new Field(row, col, isMine, game);
                fields.add(field);
            }
        var fieldProcessor = new FieldProcessor(fields, game.getHeight(), game.getWidth());
        for (var field : fields) {
            var behindFields = fieldProcessor.getBehindFields(field);
            var countMinesBehind = behindFields
                    .stream().filter(Field::isMine)
                    .count();
            field.setBehindMinesCount((int) countMinesBehind);
        }
        var savedGame = gameRepository.save(game);
        var savedFields = fieldRepository.saveAll(fields);
        GameTurnRequest request = new GameTurnRequest(savedGame.getGameId(), 0, 0);


        // Вызов метода create
        GameInfoResponse response = gameService.turn(request);

        // Проверяем, что игра была сохранена
        assertNotNull(response);
        assertNotNull(response.getGameId());


        assertEquals(response.getField().get(0).get(0), "1");
        assertEquals(response.getField().get(0).get(1), " ");
        assertEquals(response.getField().get(0).get(2), " ");


        assertEquals(response.getField().get(1).get(0), " ");
        assertEquals(response.getField().get(1).get(1), " ");
        assertEquals(response.getField().get(1).get(2), " ");

        assertEquals(response.getField().get(2).get(0), " ");
        assertEquals(response.getField().get(2).get(1), " ");
        assertEquals(response.getField().get(2).get(2), " ");

        assertEquals(response.getField().get(3).get(0), " ");
        assertEquals(response.getField().get(3).get(1), " ");
        assertEquals(response.getField().get(3).get(2), " ");
        assertFalse(response.isCompleted());
    }

    @Test
    @Transactional
    public void testTurn_isMineField() throws Exception {
        var game = new Game(3, 4, 1);
        var fields = new ArrayList<Field>();
        for (int row = 0; row < game.getHeight(); row++)
            for (int col = 0; col < game.getWidth(); col++) {
                var isMine = false;
                if (row == 1 && col == 1)
                    isMine = true;
                var field = new Field(row, col, isMine, game);
                fields.add(field);
            }
        var fieldProcessor = new FieldProcessor(fields, game.getHeight(), game.getWidth());
        for (var field : fields) {
            var behindFields = fieldProcessor.getBehindFields(field);
            var countMinesBehind = behindFields
                    .stream().filter(Field::isMine)
                    .count();
            field.setBehindMinesCount((int) countMinesBehind);
        }
        var savedGame = gameRepository.save(game);
        var savedFields = fieldRepository.saveAll(fields);
        GameTurnRequest request = new GameTurnRequest(savedGame.getGameId(), 1, 1);


        // Вызов метода create
        GameInfoResponse response = gameService.turn(request);

        // Проверяем, что игра была сохранена
        assertNotNull(response);
        assertNotNull(response.getGameId());


        assertEquals(response.getField().get(0).get(0), "1");
        assertEquals(response.getField().get(0).get(1), "1");
        assertEquals(response.getField().get(0).get(2), "1");

        assertEquals(response.getField().get(1).get(0), "1");
        assertEquals(response.getField().get(1).get(1), "X");
        assertEquals(response.getField().get(1).get(2), "1");

        assertEquals(response.getField().get(2).get(0), "1");
        assertEquals(response.getField().get(2).get(1), "1");
        assertEquals(response.getField().get(2).get(2), "1");

        assertEquals(response.getField().get(3).get(0), "0");
        assertEquals(response.getField().get(3).get(1), "0");
        assertEquals(response.getField().get(3).get(2), "0");
        assertTrue(response.isCompleted());
    }


    @Test
    @Transactional
    public void testTurn_zeroBehindMinesField() throws Exception {
        var game = new Game(3, 4, 1);
        var fields = new ArrayList<Field>();
        for (int row = 0; row < game.getHeight(); row++)
            for (int col = 0; col < game.getWidth(); col++) {
                var isMine = false;
                if (row == 1 && col == 1)
                    isMine = true;
                var field = new Field(row, col, isMine, game);
                fields.add(field);
            }
        var fieldProcessor = new FieldProcessor(fields, game.getHeight(), game.getWidth());
        for (var field : fields) {
            var behindFields = fieldProcessor.getBehindFields(field);
            var countMinesBehind = behindFields
                    .stream().filter(Field::isMine)
                    .count();
            field.setBehindMinesCount((int) countMinesBehind);
        }
        var savedGame = gameRepository.save(game);
        var savedFields = fieldRepository.saveAll(fields);
        GameTurnRequest request = new GameTurnRequest(savedGame.getGameId(), 3, 0);


        // Вызов метода create
        GameInfoResponse response = gameService.turn(request);

        // Проверяем, что игра была сохранена
        assertNotNull(response);
        assertNotNull(response.getGameId());


        assertEquals(response.getField().get(0).get(0), " ");
        assertEquals(response.getField().get(0).get(1), " ");
        assertEquals(response.getField().get(0).get(2), " ");

        assertEquals(response.getField().get(1).get(0), " ");
        assertEquals(response.getField().get(1).get(1), " ");
        assertEquals(response.getField().get(1).get(2), " ");

        assertEquals(response.getField().get(2).get(0), "1");
        assertEquals(response.getField().get(2).get(1), "1");
        assertEquals(response.getField().get(2).get(0), "1");

        assertEquals(response.getField().get(3).get(0), "0");
        assertEquals(response.getField().get(3).get(1), "0");
        assertEquals(response.getField().get(3).get(0), "0");
        assertFalse(response.isCompleted());
    }

    @Test
    @Transactional
    public void testTurn_gameWin() throws Exception {
        var game = new Game(3, 4, 1);
        var fields = new ArrayList<Field>();
        for (int row = 0; row < game.getHeight(); row++)
            for (int col = 0; col < game.getWidth(); col++) {
                var isMine = false;
                if (row == 1 && col == 1)
                    isMine = true;
                var field = new Field(row, col, isMine, game);
                fields.add(field);
            }
        var fieldProcessor = new FieldProcessor(fields, game.getHeight(), game.getWidth());
        for (var field : fields) {
            var behindFields = fieldProcessor.getBehindFields(field);
            var countMinesBehind = behindFields
                    .stream().filter(Field::isMine)
                    .count();
            field.setBehindMinesCount((int) countMinesBehind);
        }
        var savedGame = gameRepository.save(game);
        var savedFields = fieldRepository.saveAll(fields);
        GameTurnRequest request1 = new GameTurnRequest(savedGame.getGameId(), 3, 0);
        // Top line
        GameTurnRequest request2 = new GameTurnRequest(savedGame.getGameId(), 0, 0);
        GameTurnRequest request3 = new GameTurnRequest(savedGame.getGameId(), 0, 1);
        GameTurnRequest request4 = new GameTurnRequest(savedGame.getGameId(), 0, 2);

        GameTurnRequest request5 = new GameTurnRequest(savedGame.getGameId(), 1, 0);
        //GameTurnRequest request6 = new GameTurnRequest(savedGame.getGameId(), 1, 1); - ячейка с миной
        GameTurnRequest request6 = new GameTurnRequest(savedGame.getGameId(), 1, 2);

        gameService.turn(request1);
        gameService.turn(request2);
        gameService.turn(request3);
        gameService.turn(request4);
        gameService.turn(request5);


        // Вызов метода create
        GameInfoResponse response = gameService.turn(request6);

        // Проверяем, что игра была сохранена
        assertNotNull(response);
        assertNotNull(response.getGameId());


        assertEquals(response.getField().get(0).get(0), "1");
        assertEquals(response.getField().get(0).get(1), "1");
        assertEquals(response.getField().get(0).get(2), "1");

        assertEquals(response.getField().get(1).get(0), "1");
        assertEquals(response.getField().get(1).get(1), "M");
        assertEquals(response.getField().get(1).get(2), "1");

        assertEquals(response.getField().get(2).get(0), "1");
        assertEquals(response.getField().get(2).get(1), "1");
        assertEquals(response.getField().get(2).get(2), "1");

        assertEquals(response.getField().get(3).get(0), "0");
        assertEquals(response.getField().get(3).get(1), "0");
        assertEquals(response.getField().get(3).get(2), "0");
        assertTrue(response.isCompleted());
    }


}
