import sun.font.TrueTypeFont;

import java.util.LinkedList;
import java.util.List;

public class Anime {
    private String name;
    private String url;
    private List<Anime> winList;
    private List<Anime> loseList;

    public Anime(String name) {
        this.name = name;
        this.url = "";
        winList = new LinkedList();
        loseList = new LinkedList();
    }

    public void addWinned(Anime anime){
        winList.add(anime);
    }

    public Anime(String name, String url) {
        this.name = name;
        this.url = url;
        winList = new LinkedList();
        loseList = new LinkedList();
    }

    public String getURL() {
        return url;
    }

    public void addLosed(Anime anime){
        loseList.add(anime);
    }

    public boolean isPlayed(Anime anime){
        if (winList.contains(anime) || loseList.contains(anime))
            return true;
        else
            return false;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return winList.size();
    }

    public List<Anime> getWinList() {
        return winList;
    }

    public List<Anime> getLoseList() {
        return loseList;
    }

    public int getSubScore(){
        int subScore = 0;
        for(Anime anime : winList){
            subScore += anime.getScore();
        }
        return subScore;
    }
}
