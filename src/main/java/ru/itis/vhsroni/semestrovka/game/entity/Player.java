package ru.itis.vhsroni.semestrovka.game.entity;

public class Player {
    private int id;
    private int x;
    private int y;
    private int score;
    private boolean isJumping;
    private int jumpHeight;

    private boolean isCompletedLevel;
    private boolean canMove;

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public Player(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.score = 0;
        this.isJumping = false;
        this.jumpHeight = 0;
        this.isCompletedLevel = false;
        this.canMove = true;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void jump(int jumpHeight) {
        this.isJumping = true;
        this.jumpHeight = jumpHeight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public int getId() {
        return id;
    }

    public boolean isCompletedLevel() {
        return !isCompletedLevel;
    }

    public void setCompletedLevel(boolean completedLevel) {
        isCompletedLevel = completedLevel;
    }
}