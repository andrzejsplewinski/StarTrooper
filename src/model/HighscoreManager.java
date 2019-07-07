//package model;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class HighscoreManager {
//
//    private List<HighScore> scores;
//    private static final String HIGHSCORE_FILE = "ranking.list";
//
//    ObjectOutputStream outputStream = null;
//    ObjectInputStream inputStream = null;
//
//    public HighscoreManager() {
//        scores = new ArrayList<HighScore>();
//    }
//
//    public ArrayList<HighScore> getScores() {
//  //      loadScoreFile();
//        sort();
//        return (ArrayList<HighScore>) scores;
//    }
//
//    private void sort() {
//        ScoreComparator comparator = new ScoreComparator();
//        Collections.sort(scores, comparator);
//    }
//
//    public void addScores(String name, int score) {
//        loadScoreFile();
//        scores.add(new HighScore(name, score));
//        updateScoreFile();
//    }
//
//    public void loadScoreFile() {
//        try {
//            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
//            scores = (ArrayList<HighScore>) inputStream.readObject();
//        } catch (FileNotFoundException e) {
//            System.out.println("[Laad] FNF Error: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("[Laad] IO Error: " + e.getMessage());
//        } catch (ClassNotFoundException e) {
//            System.out.println("[Laad] CNF Error: " + e.getMessage());
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.flush();
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                System.out.println("[Laad] IO Error: " + e.getMessage());
//            }
//        }
//    }
//
//    public void updateScoreFile() {
//
//        try {
//            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
//            outputStream.writeObject(scores);
//        } catch (FileNotFoundException e) {
//            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
//        } catch (IOException e) {
//            System.out.println("[Update] IO Error: " + e.getMessage());
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.flush();
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                System.out.println("[Update] Error: " + e.getMessage());
//            }
//        }
//
//    }
//
//    public String getHighscoreString() {
//        String highscoreString = "";
//        int max = 5;
//        ArrayList<HighScore> scores;
//        scores = getScores();
//
//        int i = 0;
//        int x = scores.size();
//        if (x > max) {
//            x = max;
//        }
//        while (i < max) {
//            highscoreString += (i + 1) + ".\t"  + scores.get(i).getScore() + "\n";
//        }
//        return highscoreString;
//
//
//    }
//}