package ru.itis.vhsroni.semestrovka.utils;

import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.exception.LevelLoaderException;

import java.io.*;

public class LevelLoaderUtil {
    private int[][] levelMap;

    private int coinsCount;

    public void loadLevel(String levelFile) {
        levelMap = new int[20][20];
        coinsCount = 0;
        InputStream inputStream = HamsterApplication.class.getResourceAsStream(levelFile);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    levelMap[row][col] = line.charAt(col) == '1' ? 1 : line.charAt(col) == '2' ? 2 : line.charAt(col) == '3' ? 3 : 0;
                    if (levelMap[row][col] == 2) coinsCount++;
                }
                row++;
            }
        } catch (IOException e) {
            throw new LevelLoaderException(e);
        }
    }

    public int[][] getLevel() {
        return levelMap;
    }

    public int getCoinsCount() {
        return coinsCount;
    }
}
