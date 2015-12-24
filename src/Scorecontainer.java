
public class Scorecontainer implements Comparable<Scorecontainer> {
    private int score;
    private String name;
    Scorecontainer(String name, Integer score) {
        this.score = (int) score;
        this.name = name;
    }
    public int getScore() {
        return this.score;
    }
    public String getName() {
        return this.name;
    }
    @Override
    public int compareTo(Scorecontainer o) {
        return this.score - o.score;
    }
}
