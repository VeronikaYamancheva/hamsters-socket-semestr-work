package ru.itis.vhsroni.semestrovka.game.logic;

import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.game.entity.Player;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.settings.GameConstants;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameLogic {

    private int[][] levelMap;

    private int maxCookiesCount;

    private int hamsterX;

    private int hamsterY;

    private CopyOnWriteArrayList<int[]> cookies;

    private HashMap<Integer, Player> players;

    private int jumpPhase;

    private long jumpStartTime;

    private int jumpHeight;

    private boolean isJumping;

    private Player currentPlayer;

    private int score;


    public GameLogic(int[][] levelMap, int maxCookiesCount, Player currentPlayer) {
        this.levelMap = levelMap;
        this.hamsterX = 0;
        this.hamsterY = 19;
        this.cookies = new CopyOnWriteArrayList<>();
        this.players = new HashMap<>();
        this.jumpPhase = 0;
        this.jumpHeight = 0;
        this.isJumping = false;
        this.currentPlayer = currentPlayer;
        this.maxCookiesCount = maxCookiesCount;
        this.score = 0;

        fillcookiesList();
    }

    private void fillcookiesList() {
        for (int row = 0; row < levelMap.length; row++) {
            for (int col = 0; col < levelMap[row].length; col++) {
                if (levelMap[row][col] == 2) {
                    cookies.add(new int[]{row, col});
                }
            }
        }
    }

    public int[][] getLevelMap() {
        return levelMap;
    }

    public int getMaxCookiesCount() {
        return maxCookiesCount;
    }

    public int getHamsterX() {
        return hamsterX;
    }

    public int getHamsterY() {
        return hamsterY;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        sendPlayerPositionUpdate(hamsterX, hamsterY);
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void jump() {
        if (!isJumping && isOnSolidGround(hamsterY, hamsterX)) {
            isJumping = true;
            jumpPhase = 1;
            jumpStartTime = System.currentTimeMillis();
            jumpHeight = 0;
            sendPlayerJump(GameConstants.MAX_JUMP_HEIGHT);
        }
    }

    public void goLeft() {
        int newX = hamsterX - 1;
        if (newX >= 0 && isCollidingWithObstacle(hamsterY, newX)) {
            hamsterX = newX;
        }
    }

    public void goRight() {
        int newX = hamsterX + 1;
        if (newX < levelMap[0].length && isCollidingWithObstacle(hamsterY, newX)) {
            hamsterX = newX;
        }
    }

    private boolean isObstacleAbove(int row, int col) {
        if (row < 0) {
            return false;
        }

        return levelMap[row][col] == 1;
    }

    private boolean isCollidingWithObstacle(int row, int col) {
        if (row < 0 || row >= levelMap.length || col < 0 || col >= levelMap[0].length) {
            return false;
        }

        return levelMap[row][col] != 1;
    }

    private int calculateLandingPosition(int row, int col) {
        for (int r = row + 1; r < levelMap.length; r++) {
            if (levelMap[r][col] == 1) {
                return (r - 1);
            }
        }

        return levelMap.length - 1;
    }

    private boolean isOnSolidGround(int row, int col) {
        if (row + 1 >= levelMap.length) {
            return true;
        }

        return levelMap[row + 1][col] == 1;
    }

    public void update() {
        if (isJumping) {
            long elapsed = System.currentTimeMillis() - jumpStartTime;

            switch (jumpPhase) {
                case 1 -> {
                    if (jumpHeight < GameConstants.MAX_JUMP_HEIGHT) {
                        int newY = hamsterY - 1;
                        if (newY >= 0 && !isObstacleAbove(newY, hamsterX)) {
                            hamsterY = newY;
                            jumpHeight++;
                        } else {
                            jumpPhase = 3;
                        }
                    } else {
                        jumpPhase = 2;
                        jumpStartTime = System.currentTimeMillis();
                    }
                }
                case 2 -> {
                    if (elapsed >= GameConstants.JUMP_DELAY) {
                        jumpPhase = 3;
                    }
                }
                case 3 -> {
                    if (jumpHeight > 0) {
                        int newY = hamsterY + 1;
                        if (newY < levelMap.length && isCollidingWithObstacle(newY, hamsterX)) {
                            hamsterY = newY;
                            jumpHeight--;
                        } else {
                            isJumping = false;
                            jumpPhase = 0;
                            hamsterY = calculateLandingPosition(hamsterY, hamsterX);
                        }
                    } else {
                        isJumping = false;
                        jumpPhase = 0;
                        hamsterY = calculateLandingPosition(hamsterY, hamsterX);
                    }
                }
            }
        } else if (!isOnSolidGround(hamsterY, hamsterX)) {
            hamsterY += 1;

            if (isOnSolidGround(hamsterY, hamsterX)) {
                hamsterY = calculateLandingPosition(hamsterY, hamsterX);
            }
        }

        if (currentPlayer != null) {
            if (hamsterX != currentPlayer.getX() || hamsterY != currentPlayer.getY()) {
                currentPlayer.setPosition(hamsterX, hamsterY);
                sendPlayerPositionUpdate(hamsterX, hamsterY);
            }
        } else {
        }

        CopyOnWriteArrayList<int[]> cookiesToRemove = new CopyOnWriteArrayList<>();

        Iterator<int[]> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            int[] coin = iterator.next();
            int coinX = coin[1];
            int coinY = coin[0];

            if (hamsterX == coinX && hamsterY == coinY) {
                sendCoinCollected(coinX, coinY);
                levelMap[coinY][coinX] = 0;
                ByteBuffer buffer = ByteBuffer.allocate(GameConstants.INT_BYTES);
                score++;
                buffer.putInt(score);
                HamsterApplication.getClient().sendMessage(MessageCreator.createMessage(MessageType.SCORE_UPDATED_REQUEST, buffer.array()));
                cookiesToRemove.add(coin);
            }
        }
        cookies.removeAll(cookiesToRemove);
    }

    private void sendPlayerPositionUpdate(int x, int y) {
        ByteBuffer buffer = ByteBuffer.allocate(GameConstants.INT_BYTES * 3);
        buffer.putInt(x);
        buffer.putInt(y);
        Message message = MessageCreator.createMessage(MessageType.PLAYER_POSITION_UPDATE_REQUEST, buffer.array());
        HamsterApplication.getClient().sendMessage(message);
    }

    private void sendCoinCollected(int coinX, int coinY) {
        ByteBuffer buffer = ByteBuffer.allocate(GameConstants.INT_BYTES * 3);
        buffer.putInt(coinX);
        buffer.putInt(coinY);
        Message message = MessageCreator.createMessage(MessageType.COOKIE_COLLECTED_REQUEST, buffer.array());
        HamsterApplication.getClient().sendMessage(message);
    }

    private void sendPlayerJump(int jumpHeight) {
        ByteBuffer buffer = ByteBuffer.allocate(GameConstants.INT_BYTES * 2);
        buffer.putInt(jumpHeight);
        Message message = MessageCreator.createMessage(MessageType.PLAYER_JUMP_REQUEST, buffer.array());
        HamsterApplication.getClient().sendMessage(message);
    }

    public void updatePlayerPosition(int playerId, int x, int y) {
        Player player = players.get(playerId);
        if (player == null) {
            player = new Player(playerId, x, y);
            players.put(playerId, player);
        } else {
            if (player.isCompletedLevel()) {
                player.setPosition(x, y);
            }
        }
    }

    public void collectCoin(int playerId, int coinX, int coinY) {
        Player player = players.get(playerId);
        if (player != null && player.isCompletedLevel()) {
            levelMap[coinY][coinX] = 0;
            Iterator<int[]> iterator = cookies.iterator();
            CopyOnWriteArrayList<int[]> cookiesToRemove = new CopyOnWriteArrayList<>();
            while (iterator.hasNext()) {
                int[] coin = iterator.next();
                int x = coin[1];
                int y = coin[0];
                if (x == coinX && y == coinY) {
                    sendCoinCollected(coinX, coinY);
                    cookiesToRemove.add(coin);
                }
            }
            cookies.removeAll(cookiesToRemove);
        }
        if (score == maxCookiesCount) {
            HamsterApplication.getClient().sendMessage(MessageCreator.createMessage(MessageType.ALL_COOKIES_COLLECTED_REQUEST, new byte[0]));
        }
    }

    public void handlePlayerJump(int playerId, int jumpHeight) {
        Player player = players.get(playerId);
        if (player != null) {
            player.jump(jumpHeight);
        }
    }

    public boolean checkLose() {
        return score != maxCookiesCount;
    }

    public boolean checkWin() {
        return score == maxCookiesCount;
    }
}
