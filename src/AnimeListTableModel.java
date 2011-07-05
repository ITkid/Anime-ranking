import javax.swing.table.AbstractTableModel;
import java.util.List;

public class AnimeListTableModel extends AbstractTableModel{
    private List<Anime> animeList;
    private final int COLUMN_COUNT = 3;

    public AnimeListTableModel(List<Anime> animeList) {
        this.animeList = animeList;
    }

    public int getRowCount() {
        return animeList.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return animeList.get(rowIndex).getName();
            case 1 :
                return animeList.get(rowIndex).getScore();
            case 2 :
                return animeList.get(rowIndex).getSubScore();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0 :
                return "Anime";
            case 1 :
                return "Score";
            case 2 :
                return "Subscore";
        }
        return "";
    }

    public String[] getColumnNames(){
        String[] columns = {"Anime", "Score", "Subscore"};
         return columns;
    }
}
