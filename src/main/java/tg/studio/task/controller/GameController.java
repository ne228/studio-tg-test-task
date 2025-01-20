package tg.studio.task.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.studio.task.request.GameTurnRequest;
import tg.studio.task.request.NewGameRequest;
import tg.studio.task.response.ErrorResponse;
import tg.studio.task.service.GameService;

@RestController
@RequestMapping("api")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping("/message")
    public ResponseEntity<?> message() {
        try {
            return ResponseEntity.ok().body("message");
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody NewGameRequest request) {
        try {
            return ResponseEntity.ok().body(gameService.create(request));
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(new ErrorResponse(exception.getMessage()));
        }
    }

    @PostMapping("/turn")
    public ResponseEntity<?> turn(@RequestBody GameTurnRequest request) {
        try {
            return ResponseEntity.ok().body(gameService.turn(request));
        } catch (Exception exception) {
            return ResponseEntity.status(400).body(new ErrorResponse(exception.getMessage()));
        }
    }
}
