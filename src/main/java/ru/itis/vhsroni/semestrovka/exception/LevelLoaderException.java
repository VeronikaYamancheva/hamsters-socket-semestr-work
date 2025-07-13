package ru.itis.vhsroni.semestrovka.exception;

public class LevelLoaderException extends RuntimeException{

    public LevelLoaderException(Throwable cause) {
        super("Exception during levelMapLoader: ", cause);
    }
}
