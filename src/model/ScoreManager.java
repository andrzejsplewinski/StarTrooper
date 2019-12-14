package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScoreManager {

    private static final String HIGHSCORE_FILE = "ranking.list";

    public ScoreManager() {

    }

    public void saveScore(Score score) {
        File scoreFile = new File(HIGHSCORE_FILE);
        BufferedWriter writer = null;

        try {
            FileWriter writerFile = new FileWriter(scoreFile, true);
            writer = new BufferedWriter(writerFile);
            writer.newLine();
            writer.write(score.toString());

        } catch (IOException e) {
            System.out.println("Write exception: " + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Write exception: " + e);
                }
            }
        }
    }

    public HashMap<String, Integer> getHighScores() {
        Map<String, Integer> highScore = new HashMap<>();

        List<Score> scores = new ArrayList<>();

        String line;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(HIGHSCORE_FILE));

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);

                if (parts.length >= 2) {
                    String key = parts[0].replace("{", "");
                    String value = parts[1].replace("}", "");
                    int intValue = Integer.parseInt(value);
                    highScore.put(key, intValue);
                    scores.add(Score.of(key, intValue));
                }
            }
        } catch (IOException e) {
            System.out.println("Read exception: " + e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                System.out.println("Read exception: " + e);
            }
        }

        //pass scores
        return getThreeBestScores(highScore);
    }

    private HashMap<String, Integer> getThreeBestScores(Map<String, Integer> highScore) {

        HashMap<String, Integer> sortedScoreMap = new HashMap<>();

        List<Map.Entry<String, Integer>> list = new LinkedList<>(highScore.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<String, Integer> sortList : list) {
            if (sortedScoreMap.size() < 3) {
                sortedScoreMap.put(sortList.getKey(), sortList.getValue());
                System.out.println("Name: " + sortList.getKey() + "  Score: " + sortList.getValue());
            }
        }
        return sortedScoreMap;
    }

}
