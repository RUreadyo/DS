
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 *
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를
 * 유지하는 데이터베이스이다.
 */
public class MovieDB {
    MyLinkedList<Genre> list;
    public MovieDB() {
       list = new MyLinkedList<>();
    }

    public void insert(MovieDBItem item) {
        Genre inputgenre = new Genre(item.getGenre(), item.getTitle());
        Node<Genre> last2 = list.head;
            while (last2.getNext() != null) {
                int comp2=last2.getNext().compareTo(inputgenre);
                if (comp2>0) break;
                else if(comp2==0){
                    last2.getNext().getItem().add(item.getTitle());
                    return;
                }
                last2=last2.getNext();
            }
            last2.insertNext(inputgenre);
            list.numItems+=1;
            return;
    }



    public void delete(MovieDBItem item) {
        // FIXME implement this
        Genre inputgenre = new Genre(item.getGenre(), item.getTitle());
        Node<Genre> last2 = list.head;
        while (last2.getNext() != null) {
            int comp2 = last2.getNext().compareTo(inputgenre);
            if (comp2 > 0) return;
            else if (comp2 == 0) {
                Node<String> last3 = last2.getNext().getItem().head;
                while (last3.getNext() != null) {
                    int comp3 = last3.getNext().compareTo(item.getTitle());
                    if (comp3 > 0) return;
                    else if (comp3 == 0) {
                        last3.removeNext();
                        last2.getNext().getItem().numItems -= 1;
                        if (last2.getNext().getItem().numItems == 0) {
                            last2.removeNext();
                            list.numItems -= 1;
                        }
                        return;
                    }
                    last3 = last3.getNext();
                }
            }
            last2 = last2.getNext();
        }
    }


    public MyLinkedList<MovieDBItem> search(String term) {
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        for (Genre g: list){
            for(String t : g){
                MovieDBItem x = new MovieDBItem(g.gen,t);
                if(t.contains(term)) results.add(x);
            }
        }

        return results;
    }

    public MyLinkedList<MovieDBItem> items() {

        MyLinkedList<MovieDBItem> results = new MyLinkedList<>();
        for (Genre g : list) {
            for (String t : g) {
                MovieDBItem x = new MovieDBItem(g.gen, t);
                results.add(x);
            }
        }
        return results;
    }
}

class Genre extends MyLinkedList<String> implements Comparable<Genre> {
    String gen;
    public Genre(String name) {
        super();
        gen = name;
    }
    public Genre(String name, String title) {
        super();
        this.add(title);
        gen = name;
    }

    public int compareTo(Genre that) {
        return this.gen.compareTo(that.gen);
    }


}