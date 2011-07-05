import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimeHelper {

    public static void main(String[] args){
        getAnimeListFromURL("http://myanimelist.net/animelist/jck&show=0&order=4");
    }



    public static List<Anime> getAnimeListFromURL(String url){
        List<Anime> animeList = new LinkedList();
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            String responseBody = httpclient.execute(httpget, responseHandler);
            String[] lines = responseBody.split("\n");

            Pattern patternEndComplated = Pattern.compile("class=\"category_totals\"");
            Pattern patternComplated = Pattern.compile("class=\"header_completed\"");
            Pattern patternAnime = Pattern.compile("<a href=\"(.+?)\".*?><span>(.+?)</span></a>");

            int count = 0;
            Matcher matcherCompleted = patternComplated.matcher(lines[count]);

            while(!matcherCompleted.find()){
                count += 1;
                matcherCompleted.reset(lines[count]);
            }

            count += 1;
            Matcher matcherEndComplated = patternEndComplated.matcher(lines[count]);
            Matcher matcherAnime = patternAnime.matcher(lines[count]);

            while(!matcherEndComplated.find()){
                if(matcherAnime.find()){
                    animeList.add(new Anime(matcherAnime.group(2), matcherAnime.group(1)));
                }
                count += 1;
                matcherEndComplated.reset(lines[count]);
                matcherAnime.reset(lines[count]);
            }
            count += 0;
        }catch (Exception e){
        }

        httpclient.getConnectionManager().shutdown();

        return animeList;
    }

    public static Image getAnimeImageFromURL(String url){
        //!!!!!!!!!!!!!!!!!!!!!!!!!
        //System.out.println("Load image from url: " + url);
        //!!!!!!!!!!!!!!!!!!!!!!!!!
        Image image = null;
        HttpClient httpclient = new DefaultHttpClient();
        try{
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            Pattern patternImageURL = Pattern.compile("<img src=\"(.+?)\".*?>");

            int count = 0;
            Matcher matcherImageURL = patternImageURL.matcher(responseBody);

            if (matcherImageURL.find()){
                try{
                    image = ImageIO.read(new URL(matcherImageURL.group(1)));
                }catch (Exception e){
                }
            }
        }catch (Exception e){
            try{
                image = ImageIO.read(new File("Unknown.JPG"));
            }catch (Exception exc){
            }

        }finally {
            httpclient.getConnectionManager().shutdown();
        }

        if (url == ""){
            try{
                image = ImageIO.read(new File("Unknown.JPG"));
            }catch (Exception exc){
            }
        }
        return image;
    }

    public static Map<String, Icon> getImageMap(List<Anime> animeList){
        Map<String, Icon> imageMap = new HashMap<String, Icon>();

        int i = 0;
        for(Anime anime : animeList){
            imageMap.put(anime.getURL(), new ImageIcon(getAnimeImageFromURL(anime.getURL())));
            System.out.println(i++);
        }

        return imageMap;
    }

    public static List<Anime> getAnimeListFromFile(String filename){
        List<Anime> animeList = new LinkedList();

        try{
            BufferedReader in = new BufferedReader(new FileReader(filename));
            while (in.ready()){
                animeList.add(new Anime(in.readLine()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return animeList;
    }
}
