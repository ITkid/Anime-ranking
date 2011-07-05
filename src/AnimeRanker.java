import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class AnimeRanker {
    private List<Anime> animeList;
    private List<List<Anime>> groups;
    private int round;

    public static String FREESLOT = "Free slot";

    public AnimeRanker(List<Anime> animeList) {
        round = -1;
        groups = new LinkedList();
        if (animeList == null){
            this.animeList = new LinkedList();
        }else{
            this.animeList = animeList;
        }
    }

    public AnimeRanker() {
        round = -1;
        groups = new LinkedList();
        this.animeList = new LinkedList();
    }

    private void createGroups(){
        Collections.shuffle(animeList);
        for(Anime anime : animeList)
            groups.get(anime.getScore()).add(anime);
    }

    private List<Pair<Anime>> getPairs(){
        List<Pair<Anime>> pairs = new LinkedList();

        for(int i = round; i>= 0; i--){
            List<Anime> curList = groups.get(i);
            while (curList.size() > 0){
                int count = curList.size();
                Anime first = curList.remove(count - 1);
                count -= 1;
                if (count == 0){
                    if (i == 0){
                        pairs.add(new Pair<Anime>(first, new Anime(AnimeRanker.FREESLOT)));
                    }else{
                        groups.get(i-1).add(first);
                    }
                }
                for(int j = count-1; j >= 0; j--){
                    Anime second = curList.get(j);
                    if (first.isPlayed(second)){
                        if (j == 0){
                            if (i == 0){
                                pairs.add(new Pair<Anime>(first, second));
                                curList.remove(second);
                                break;
                            }else{
                                groups.get(i-1).add(first);
                            }
                        }
                    }else{
                        pairs.add(new Pair<Anime>(first, second));
                        curList.remove(second);
                        break;
                    }
                }
            }
        }

        return pairs;
    }

    public  List<Pair<Anime>> nextRound(){
        round += 1;
        groups.add(new LinkedList());
        createGroups();
        return getPairs();
    }

    public List<Anime> getAnimeList() {
        return animeList;
    }

    public void loadAnimeList(List<Anime> list){
        animeList.clear();
        round = -1;
        groups.clear();
        animeList.addAll(list);
    }
}