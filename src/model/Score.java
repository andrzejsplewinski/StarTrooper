package model;

public class Score {
    private String name;
    private int value;

    private Score(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static Score of(String name, int value) {
        return new Score(name, value);
    }

    @Override
    public String toString() {
        return "{" +
                name +
                "=" +
                value +
                '}';
    }
}
