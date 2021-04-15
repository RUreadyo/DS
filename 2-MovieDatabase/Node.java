public class Node<T extends Comparable<T>>{
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }

    public Node(T obj, Node<T> next) {
        this.item = obj;
        this.next = next;
    }

    public final T getItem() {
        return item;
    }

    public final void setItem(T item) {
        this.item = item;
    }

    public final void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public final void insertNext(T obj) {
        Node<T> newNode=new Node<>(obj,this.next);
        this.next=newNode;
        return;

    }

    public final void removeNext() {
        this.next=this.next.next;
    }

    public int compareTo(T that){
        return this.getItem().compareTo(that);
    }
}