
/******************************************************************************
 * MovieDB의 인터페이스에서 공통으로 사용하는 클래스.
 */
public class MovieDBItem implements Comparable<MovieDBItem> {

    private final String genre;
    private final String title;

    public MovieDBItem(String genre, String title) {
        if (genre == null) throw new NullPointerException("genre");
        if (title == null) throw new NullPointerException("title");

        this.genre = genre;
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    @Override
     public int compareTo(MovieDBItem other) {
       int g=genre.compareTo(other.genre);
       int t=title.compareTo(other.title);
       if(g!=0) return g;
       return t;
    }

}
