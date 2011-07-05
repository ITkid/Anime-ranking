import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 02.07.11
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class AnimeRankerView extends JFrame{

    private static final String TITLE = "Anime ranking (by Alexey ITkid Gridin)";
    private String mainTitle = "Anime ranking";
    private String loadInfo = "[] ";
    private String status = "";

    private JPanel mainPanel;
    private JPanel btnPanel;
    private JScrollPane infoPanel;
    private JButton button1;
    private JButton button2;
    private JTextField tfRound;
    private JButton btnNext;
    private JButton btnSort;
    private JButton btnOpen;
    private JButton btnStart;
    private SwingWorker iconLoader;
    private JTable table;
    private int round = 1;
    private int pairIndex = 0;
    private java.util.List<Pair<Anime>> pairs;
    private Map<String, Icon> iconMap;
    private AnimeRanker ranker;

    public AnimeRankerView() throws HeadlessException {
        super("Anime ranking");
        mainPanel = new JPanel(new BorderLayout());
        infoPanel = new JScrollPane(mainPanel);
        tfRound = new JTextField("");
        button1 = new JButton("");
        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
        button1.setHorizontalTextPosition(AbstractButton.CENTER);
        button2 = new JButton("");
        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
        button2.setHorizontalTextPosition(AbstractButton.CENTER);
        btnNext = new JButton("Next round");
        btnSort = new JButton("Sort");
        btnOpen = new JButton("Open file");
        btnStart = new JButton("Start");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectAnime(1);
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectAnime(2);
            }
        });
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goNext();
            }
        });
        btnSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sort();
            }
        });
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectList();
            }
        });
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startRanking();
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        ranker = new AnimeRanker();
        iconMap = new HashMap<String, Icon>();
        pairs = ranker.nextRound();

        table = new JTable(new AnimeListTableModel(ranker.getAnimeList()));
        infoPanel.setColumnHeaderView(table.getTableHeader());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateColumnsFromModel(true);
        mainPanel.add(table, BorderLayout.CENTER);
        button1.setEnabled(false);
        button2.setEnabled(false);
        btnNext.setEnabled(false);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().setLayout(gbl);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbl.setConstraints(tfRound, gbc);
        getContentPane().add(tfRound);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbl.setConstraints(btnOpen, gbc);
        getContentPane().add(btnOpen);
        gbc.gridx = 1;
        gbl.setConstraints(btnStart, gbc);
        getContentPane().add(btnStart);
        gbc.gridx = 2;
        gbl.setConstraints(btnNext, gbc);
        getContentPane().add(btnNext);
        gbc.gridx = 3;
        gbl.setConstraints(btnSort, gbc);
        getContentPane().add(btnSort);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbl.setConstraints(button1, gbc);
        getContentPane().add(button1);
        gbc.gridx = 2;
        gbl.setConstraints(button2, gbc);
        getContentPane().add(button2);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.gridheight = 4;
        gbl.setConstraints(infoPanel, gbc);
        getContentPane().add(infoPanel);
        refresh();
    }

    public static void main(String[] args){
        new AnimeRankerView().setVisible(true);
    }

    private void loadButtons(){
        //!!!!!!!!!!!!!!!!!!!!!!!!!
        //System.out.println("Buttons load: " + pairIndex);
        //!!!!!!!!!!!!!!!!!!!!!!!!!
        loadButton(button1, pairs.get(pairIndex).getFirst());
        loadButton(button2, pairs.get(pairIndex).getSecond());
    }

    private void loadButton(JButton btn, Anime anime){
        btn.setText(anime.getName());
        if (iconMap.containsKey(anime.getURL())){
            btn.setIcon(iconMap.get(anime.getURL()));
        }else{
            Icon icon = new ImageIcon(AnimeHelper.getAnimeImageFromURL(anime.getURL()));
            btn.setIcon(icon);
            iconMap.put(anime.getURL(), icon);
        }
    }

    private void selectAnime(int index){
        Anime first = pairs.get(pairIndex).getFirst();
        Anime second = pairs.get(pairIndex).getSecond();
        switch (index){
            case 1 :
                first.addWinned(second);
                second.addLosed(first);
                break;
            case 2 :
                second.addWinned(first);
                first.addLosed(second);
                break;
        }
        pairIndex += 1;
        if (pairIndex >= pairs.size()){
            button1.setText("");
            button2.setText("");
            button1.setEnabled(false);
            button2.setEnabled(false);
            status = "   [Round " + round + " completed]";
            refresh();
            pairs.clear();
            pairIndex = 0;
        }else{
            status = "   [Round " + round + "...   " + (pairIndex+1) + " / " + pairs.size()+"]";
            refresh();
            loadButtons();
            if ((pairIndex == pairs.size()-1) &&
                    (pairs.get(pairIndex).getSecond().getName().equals(AnimeRanker.FREESLOT))){
                selectAnime(1);
            }
        }
    }

    private void goNext(){
        pairs = ranker.nextRound();
        round += 1;
        setTitle(mainTitle + "   [Round " + round + "...   " + (pairIndex+1) + " / " + pairs.size()+"]");
        loadButtons();
        button1.setEnabled(true);
        button2.setEnabled(true);
    }

    private void sort(){
        java.util.List<Anime> animeList = ranker.getAnimeList();
        Collections.sort(animeList, new Comparator<Anime>() {
            public int compare(Anime o1, Anime o2) {
                if (o1.getScore() > o2.getScore())
                    return -1;
                if (o1.getScore() < o2.getScore())
                    return 1;
                if (o1.getScore() == o2.getScore()) {
                    if (o1.getSubScore() > o2.getSubScore())
                        return -1;
                    if (o1.getSubScore() < o2.getSubScore())
                        return 1;
                }
                return 0;
            }
        });
        table.repaint();
    }

    private void selectList(){
        JFileChooser jFileChooser = new JFileChooser();
        int returnVal = jFileChooser.showDialog(this, "Select");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            tfRound.setText(jFileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void startRanking(){
        String url = tfRound.getText();
        if(!url.equals("")){
            if(url.startsWith("http")){
                ranker.loadAnimeList(AnimeHelper.getAnimeListFromURL(url));
            }else{
                ranker.loadAnimeList(AnimeHelper.getAnimeListFromFile(url));
            }
            btnNext.setEnabled(true);
            pairs = ranker.nextRound();
            round = 1;
            status = "   [Round " + round + "...   " + (pairIndex+1) + " / " + pairs.size()+"]";
            refresh();
            loadButtons();
            button1.setEnabled(true);
            button2.setEnabled(true);
            iconLoader = new SwingWorker(){
                @Override
                protected Object doInBackground() throws Exception {
                    for(int i = 0; i < pairs.size(); i++){
                        if(i % 5 == 0){
                            Thread.sleep(500);
                        }

                        Thread loader1 = new IconLoaderThread(iconMap, pairs.get(i).getFirst().getURL());
                        Thread loader2 = new IconLoaderThread(iconMap, pairs.get(i).getSecond().getURL());
                        loader1.start();
                        loader2.start();
                        loadInfo = "[ " + Math.round(100 * i / pairs.size()) + "% ] ";
                        refresh();
                    }
                    return null;
                }
            };
            iconLoader.execute();
        }
    }

    private void refresh(){
        setTitle(loadInfo + TITLE + status);
    }
}


class IconLoaderThread extends Thread{
    private Map<String, Icon> iconMap;
    private String url;

    IconLoaderThread(Map<String, Icon> iconMap, String url) {
        this.iconMap = iconMap;
        this.url = url;
    }

    @Override
    public void run() {
        if (!iconMap.containsKey(url)){
            Icon icon = new ImageIcon(AnimeHelper.getAnimeImageFromURL(url));
            iconMap.put(url, icon);
            System.out.println(url + " loaded.");
        }
    }
}