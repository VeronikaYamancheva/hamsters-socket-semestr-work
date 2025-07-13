package ru.itis.vhsroni.semestrovka.controller.impl;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.client.HamsterClient;
import ru.itis.vhsroni.semestrovka.controller.MessageController;
import ru.itis.vhsroni.semestrovka.exception.message.MessageTypeException;
import ru.itis.vhsroni.semestrovka.game.entity.Player;
import ru.itis.vhsroni.semestrovka.game.logic.GameLogic;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.settings.GameConstants;
import ru.itis.vhsroni.semestrovka.settings.UIConstants;
import ru.itis.vhsroni.semestrovka.ui.CustomAlertManager;
import ru.itis.vhsroni.semestrovka.ui.animation.StarAnimation;
import ru.itis.vhsroni.semestrovka.ui.drawer.CookieDrawer;
import ru.itis.vhsroni.semestrovka.ui.drawer.ObstacleDrawer;
import ru.itis.vhsroni.semestrovka.ui.drawer.PixelBackgroundDrawer;
import ru.itis.vhsroni.semestrovka.ui.drawer.PixelDoorDrawer;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;
import ru.itis.vhsroni.semestrovka.utils.LevelLoaderUtil;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameController implements MessageController {

    @FXML
    private Button startGameButton;

    private Stage primaryStage;

    private Alert informationAlert;

    private HamsterClient client;

    private GameLogic gameLogic;

    private int currentLevel;

    private double tileWidth;

    private double tileHeight;

    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private Player currentPlayer;

    private Image currentPlayerImage;

    private Image defaultPlayerImage;

    private FXMLVisualizer visualizer;

    private AnimationTimer animationTimer;

    private String roomName;

    private Label levelLabel;

    private Label scoreLabel;

    private List<StarAnimation> activeAnimations;


    @Override
    public void receiveMessage(Message message) {
        if (message != null) {
            switch (message.getType()) {
                case MessageType.ROOM_NAME_INFO_RESPONSE -> {
                    roomName = new String(message.getData(), StandardCharsets.UTF_8);
                }
                case MessageType.GAME_WAS_STARTED_RESPONSE -> {
                    Platform.runLater(() -> startGame(0));
                }
                case MessageType.COOKIE_COLLECTED_RESPONSE -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    int playerId = buffer.getInt();
                    int coinX = buffer.getInt();
                    int coinY = buffer.getInt();

                    if (currentPlayer.getId() == playerId) {
                        StarAnimation animation = new StarAnimation(coinX, coinY);
                        animation.start();
                        activeAnimations.add(animation);
                    }
                    gameLogic.collectCoin(playerId, coinX, coinY);
                }
                case MessageType.PLAYER_POSITION_UPDATE_RESPONSE -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    int playerId = buffer.getInt();
                    int x = buffer.getInt();
                    int y = buffer.getInt();
                    if (gameLogic != null) {
                        gameLogic.updatePlayerPosition(playerId, x, y);
                    }

                    if (x == 19 && y == 0 && playerId == currentPlayer.getId()) {
                        Platform.runLater(this::showLevelWaitingRoomAlert);
                    }
                }
                case MessageType.PLAYER_JUMP_RESPONSE -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    int playerId = buffer.getInt();
                    int jumpHeight = buffer.getInt();
                    gameLogic.handlePlayerJump(playerId, jumpHeight);
                }
                case MessageType.ALL_COOKIES_COLLECTED_RESPONSE -> Platform.runLater(() -> {
                    informationAlert.setTitle("Information");
                    informationAlert.setHeaderText("Information");
                    informationAlert.setContentText("All coins were collected!!!");
                    informationAlert.show();
                });
                case MessageType.SCORE_UPDATED_RESPONSE -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    gameLogic.setScore(buffer.getInt());
                }
                case MessageType.PLAYER_COMPLETED_LEVEL -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    int playerId = buffer.getInt();
                    Player player = gameLogic.getPlayers().get(playerId);
                    if (player != null) {
                        player.setCompletedLevel(true);
                        player.setCanMove(false);
                    }
                }
                case MessageType.ALL_PLAYERS_COMPLETED_LEVEL -> {
                    completeLevel();
                }
                case MessageType.NEW_LEVEL -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    int currentLevel = buffer.getInt();
                    startGame(currentLevel);
                }
                case MessageType.GAME_OVER -> Platform.runLater(this::endGame);
                case MessageType.PLAYER_DISCONNECTED -> playerDisconnected();

                default -> throw new MessageTypeException(message.getType());
            }
        }
    }

    public void playerDisconnected() {
        Platform.runLater(() -> {
            informationAlert.setHeaderText("One player is disconnected!");
            informationAlert.setContentText("One of the players disconnected from the game!");
            informationAlert.show();
            endGame();

        });
    }

    private void completeLevel() {
        if (gameLogic.checkLose()) {
            Platform.runLater(() -> {
                informationAlert.setHeaderText("Loooooose(((");
                informationAlert.setContentText("Game is over. You lose!");
                informationAlert.show();
                endGame();
            });
        } else {
            if (currentLevel == GameConstants.LEVELS_COUNT) {
                Platform.runLater(() -> {
                    informationAlert.setHeaderText("Wiiiiiiiin!!!");
                    informationAlert.setContentText("Game is over. You win!");
                    informationAlert.show();
                    endGame();
                });
            } else {
                showNextLevel();
            }
        }
    }

    private void showNextLevel() {
        ByteBuffer buffer = ByteBuffer.allocate(GameConstants.INT_BYTES);
        buffer.putInt(currentLevel);
        client.sendMessage(MessageCreator.createMessage(MessageType.NEW_LEVEL, buffer.array()));
    }

    private void endGame() {
        if (HamsterApplication.getClient() != null) {
            sendGameOverMessage();

            if (client.getThread() != null) client.getThread().stop();

            if (animationTimer != null) {
                animationTimer.stop();
                animationTimer = null;
            }
            HamsterApplication.setClient(null);
            gameLogic = null;
            currentLevel = 0;
            client = null;

            visualizer.show(FXMLConstants.START_SCREEN_NAME);
        }
    }


    private void showLevelWaitingRoomAlert() {
        Alert completeLevelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        completeLevelAlert.initOwner(primaryStage);
        completeLevelAlert.setTitle("Confirmation");
        completeLevelAlert.setHeaderText("You have reached the end of the level!");
        completeLevelAlert.setContentText("Do you want to proceed to the next level?");
        CustomAlertManager.setConfirmAlertTheme(completeLevelAlert);


        Optional<ButtonType> result = completeLevelAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            client.sendMessage(MessageCreator.createMessage(MessageType.PLAYER_COMPLETED_LEVEL, new byte[0]));
            gameLogic.getCurrentPlayer().setCompletedLevel(true);
            gameLogic.getPlayers().forEach((integer, player) -> {
                if (player.isCompletedLevel()) {
                    Platform.runLater(() -> {

                        Alert waitAlert = new Alert(Alert.AlertType.INFORMATION);
                        waitAlert.initOwner(primaryStage);
                        waitAlert.setTitle("Ожидание");
                        waitAlert.setHeaderText("Подождите, пока не закончат игру остальные");
                        waitAlert.setContentText("Вы завершили уровень. Ожидайте завершения уровня другими игроками.");
                        CustomAlertManager.setInfoAlertTheme(waitAlert);
                        waitAlert.show();

                    });
                }
            });
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentLevel = 0;
        client = HamsterApplication.getClient();
        client.setController(this);
        client.sendMessage(MessageCreator.createMessage(MessageType.ROOM_NAME_INFO_REQUEST, new byte[0]));

        primaryStage = HamsterApplication.getStage();

        currentPlayerImage = new Image(Objects.requireNonNull(HamsterApplication.class.getResourceAsStream(GameConstants.CURRENT_PLAYER_IMAGE)));
        defaultPlayerImage = new Image(Objects.requireNonNull(HamsterApplication.class.getResourceAsStream(GameConstants.DEFAULT_PLAYER_IMAGE)));

        activeAnimations = new ArrayList<>();

        informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.initOwner(primaryStage);
        CustomAlertManager.setInfoAlertTheme(informationAlert);

        visualizer = new FXMLVisualizer();

        client.sendMessage(MessageCreator.createMessage(
                MessageType.GAME_WAS_STARTED_REQUEST, new byte[0]));
    }

    private void startGame(int previousLevel) {
        currentLevel = previousLevel + 1;
        initGameLogic();

        currentPlayer = new Player(client.getPlayerId(), gameLogic.getHamsterX(), gameLogic.getHamsterY());
        gameLogic.getPlayers().put(currentPlayer.getId(), currentPlayer);
        gameLogic.setCurrentPlayer(currentPlayer);

        for (Player player : gameLogic.getPlayers().values()) {
            player.setCompletedLevel(false);
            player.setCanMove(true);
        }

        if (canvas == null) {
            initCanvas();
        }
        setupKeyControls(primaryStage.getScene());

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameLogic != null) {
                    gameLogic.update();
                    draw();
                }
            }
        };
        animationTimer.start();
    }


    private void initGameLogic() {
        LevelLoaderUtil levelLoader = new LevelLoaderUtil();
        String levelFileName = GameConstants.LEVEL_FILE_NAME.formatted(currentLevel);
        levelLoader.loadLevel(levelFileName);
        gameLogic = new GameLogic(levelLoader.getLevel(), levelLoader.getCoinsCount(), currentPlayer);
    }

    private void initCanvas() {
        HBox topPanel = new HBox();
        topPanel.getStyleClass().add("top-panel");

        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(HamsterApplication.class.getResourceAsStream(GameConstants.CURRENT_PLAYER_IMAGE))));
        userIcon.setFitWidth(40);
        userIcon.setFitHeight(40);

        Label userNameLabel = new Label(HamsterApplication.getUserName());
        userNameLabel.getStyleClass().add("label-user");

        levelLabel = new Label("Level: " + currentLevel);
        levelLabel.getStyleClass().add("label-level");

        scoreLabel = new Label("Score: %s/%s".formatted(gameLogic.getScore(), gameLogic.getMaxCookiesCount()));
        scoreLabel.getStyleClass().add("label-score");

        Label roomNameLabel = new Label("Room: " + roomName);
        roomNameLabel.getStyleClass().add("label-room");

        topPanel.getChildren().addAll(userIcon, userNameLabel, levelLabel, scoreLabel, roomNameLabel);

        canvas = new Canvas();
        graphicsContext = canvas.getGraphicsContext2D();

        BorderPane canvasPane = new BorderPane();
        canvasPane.setCenter(canvas);
        canvasPane.getStyleClass().add("canvas-pane");

        canvas.widthProperty().bind(canvasPane.widthProperty().subtract(2 * GameConstants.CANVAS_PADDING));
        canvas.heightProperty().bind(canvasPane
                .heightProperty().subtract(2 * GameConstants.CANVAS_PADDING));

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            tileWidth = canvas.getWidth() / 20;
            tileHeight = canvas.getHeight() / 20;
            redraw();
        });
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            tileWidth = canvas.getWidth() / 20;
            tileHeight = canvas.getHeight() / 20;
            redraw();
        });

        VBox root = new VBox();
        root.getChildren().addAll(topPanel, canvasPane);
        VBox.setVgrow(canvasPane, Priority.ALWAYS);

        Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(UIConstants.GAME_SCREEN_SCC)).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (gameLogic != null && gameLogic.getCurrentPlayer().canMove()) {
                switch (event.getCode()) {
                    case UP -> gameLogic.jump();
                    case LEFT -> gameLogic.goLeft();
                    case RIGHT -> gameLogic.goRight();
                }
            }
        });
    }

    private void draw() {
        redraw();

        Iterator<StarAnimation> iterator = activeAnimations.iterator();
        while (iterator.hasNext()) {
            StarAnimation animation = iterator.next();
            animation.update();
            animation.draw(graphicsContext, tileWidth, tileHeight);
            if (!animation.isActive()) {
                iterator.remove();
            }
        }

    }

    private void redraw() {
        levelLabel.setText("Level: %s".formatted(currentLevel));
        scoreLabel.setText("Score: %s/%s".formatted(gameLogic.getScore(), gameLogic.getMaxCookiesCount()));
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        tileWidth = canvas.getWidth() / 20;
        tileHeight = canvas.getHeight() / 20;

        int[][] levelMap = gameLogic.getLevelMap();

        for (int row = 0; row < levelMap.length; row++) {
            for (int col = 0; col < levelMap[row].length; col++) {
                PixelBackgroundDrawer.drawPixelBackground(graphicsContext, col * tileWidth, row * tileHeight, tileWidth, tileHeight);
            }
        }
        for (int row = 0; row < levelMap.length; row++) {
            for (int col = 0; col < levelMap[row].length; col++) {
                if (levelMap[row][col] == 1) {
                    ObstacleDrawer.drawObstacle(graphicsContext, row, col, tileWidth, tileHeight);
                } else if (levelMap[row][col] == 2) {
                    CookieDrawer.drawCookie(graphicsContext, col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                } else if (levelMap[row][col] == 3) {
                    PixelDoorDrawer.drawPixelBackground(graphicsContext, col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                }

            }
        }
        if (currentPlayer.isCompletedLevel()) {
            graphicsContext.drawImage(currentPlayerImage, gameLogic.getHamsterX() * tileWidth,
                    gameLogic.getHamsterY() * tileHeight, tileWidth, tileHeight);
        }
        Map<Integer, Player> players = gameLogic.getPlayers();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            if (player.isCompletedLevel() && entry.getKey() != currentPlayer.getId()) {
                graphicsContext.drawImage(defaultPlayerImage, player.getX() * tileWidth,
                        player.getY() * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    private void sendGameOverMessage() {
        client.sendMessage(MessageCreator.createMessage(MessageType.GAME_OVER, new byte[0]));
    }
}
