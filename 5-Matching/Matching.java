import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;

public class Matching {
    private static final int k = 6;
    public static newHashTable hashresult=new newHashTable();
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;
                command(input);
            } catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }
     private static void command(String input) {
            char first = input.charAt(0);
            String in = input.substring(2);
            // 1. '<' data input
            if (first == '<') {
                datainput(in);
            }

            // 2. '@' print
            if(first=='@'){
                int indexnum=Integer.parseInt(in);
                dataprint(indexnum);
            }

            // 3. '?' pattern search
            if(first=='?'){
                datasearch(in);
            }

        }


    //1. '<' data input
    private static void datainput(String in) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(in));
            int i=0;
           hashresult.clear();
            while (true) {
                String line = bf.readLine();
                i++;
                if (line == null) break;
                for (int j = 0; j <= line.length() - k; j++) {
                    String subline = line.substring(j, j + k);
                    Index indexin=new Index(i,j+1);
                   hashresult.insert(subline,indexin);

                }
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }

    }

    //2. '@' dataprint
    private static  void dataprint(int indexnum){
        if(hashresult.table.get(indexnum)==(null)) System.out.println("EMPTY");
        else{
            String result =hashresult.table.get(indexnum).preorder(hashresult.table.get(indexnum).root);
            System.out.println(result.substring(0,result.length()-1));
        }


    }

    //3. '?' datasearch
    private static void datasearch(String in){
        int l = in.length();
        int q = l/k, r=l%k;
        int r2=(r>0)?1:0;
        String resultoutput = "";
        //System.out.println(resultoutput.length());
        Index [][] result = new Index[q+r2][];
        for(int i=0;i<q;i++){
            String subin=in.substring(k*i,k*i+k);
            boolean hashin=(hashresult.table.get(hashresult.hash(subin))==null);
            if(hashin!=true){
                AVLTree<String,Index> foundtree = hashresult.table.get(hashresult.hash(subin));
                AVLNode<String,Index> foundnode = foundtree.search(subin);
                if((foundnode.item!=null)&&foundnode.item.compareTo(subin)==0) {
                    Index[] resultline = new Index[foundnode.list.size()];
                    for (int j = 0; j < foundnode.list.size(); j++) {
                        resultline[j] = new Index(foundnode.list.get(j).x, foundnode.list.get(j).y);
                    }
                    result[i] = resultline;
                }
            }

        }
        if(r2!=0){
            String subin=in.substring(l-k,l);
            boolean hashkey=(hashresult.table.get(hashresult.hash(subin))==null);
            if(hashkey!=true){
                AVLTree<String,Index> foundtree = hashresult.table.get(hashresult.hash(subin));
                AVLNode<String,Index> foundnode = foundtree.search(subin);
                if((foundnode.item!=null)&&foundnode.item.compareTo(subin)==0) {
                    Index[] resultline = new Index[foundnode.list.size()];
                    for (int j = 0; j < foundnode.list.size(); j++) {
                        resultline[j] = new Index(foundnode.list.get(j).x, foundnode.list.get(j).y);
                    }
                    result[q] = resultline;
                }
            }
        }

        if(result[0]==null){
            System.out.println("(0, 0)");
            return;
        }

        for(int i=0;i<result[0].length;i++){
            int line=result[0][i].x;
            int start=result[0][i].y;
            int end=result[0][i].y+6;
            for(int j=1;j<result.length-r2;j++){
                if(result[j]!=null) {
                    for (int k = 0; k < result[j].length; k++) {
                        int nline = result[j][k].x;
                        int nstart = result[j][k].y;
                        if ( (line==nline)&&(end==nstart)) {
                            end+=6;
                            break;
                        }
                    }
                    if(end-start!=6*j+6) break;
                }
            }
            if(r2!=0){
                if(result[q]!=null){
                    for(int k=0;k<result[q].length;k++){
                        int nline=result[q][k].x;
                        int nstart=result[q][k].y;
                        if((line==nline)&&(nstart==end-6+r)){
                            end+=r;
                            break;
                        }
                    }
                }
            }
            if((end-start)==in.length()) {
                resultoutput+="(";
                resultoutput+=String.valueOf(line);
                resultoutput+=", ";
                resultoutput+=String.valueOf(start);
                resultoutput+=") ";
            }
        }
        if(resultoutput.length()==0){
            System.out.println("(0,0)");
            return;
        }

        resultoutput=resultoutput.substring(0,resultoutput.length()-1);
        System.out.println(resultoutput);
        return;



        }




    //HashTable, Avltree 등은 수업자료의 코드를 참고하였음.

    public interface InterfaceHashTable<K,V>{
        public int hash(K x);
        public void insert(K x, V y);
        public Integer search(K x, V y);
    }

    public static class HashTable<K extends Comparable<K>,V> implements  InterfaceHashTable<K,V> {
        public final Integer size = 100;
        public ArrayList<V> table = new ArrayList<>();
        public int numitems;


        public HashTable() {
            numitems = 0;
            for (int i = 0; i < size; i++) {
                table.add(null);
            }


        }

        public int hash(K key) {
            return key.hashCode() % 100;
        }

        //simple search for generic version , 실제 문제상황에서는 tree으로 collision 처리하니 여기서는 따로 probing 등의 collision 처리 X
        public Integer search(K key, V value) {
            int slot = hash(key);
            if (this.table.get(slot) == null) return -1;
            else if (this.table.get(slot) == value) return slot;
            return -1;
        }


        //simple insert for generic version (overloaded in the newHashTable class), 실제 문제상황에서는 tree으로 collision 처리하니 여기서는 따로 probing 등의 collision 처리 X
        public void insert(K key, V value) {
            int slot = hash(key);
            if (table.get(slot) == (null)) {
                table.set(slot, value);
            }
        }
    }


    //generic 상황이 아닌 문제상황의 경우 newHashTable 클래스를 만들어 HashTable을 상속함,
    public static class newHashTable extends HashTable<String, AVLTree<String, Index>>{
        public newHashTable(){
            super();
        }
        public void clear(){
            this.table=null;
            table=new ArrayList<AVLTree<String,Index>>();
            numitems=0;
            for(int i=0;i<size;i++){
                table.add(null);
            }
        }
        @Override
        public int hash(String key){
            int hashresult=0;
            for(int j=0;j<((String) key).length();j++){
                hashresult+=(((String) key).charAt(j)%100);
                hashresult%=100;
            }
            return hashresult;
        }
        public void insert(String key,Index ind){
         int slot=hash(key);
         if(table.get(slot)==null){
             AVLTree<String,Index> x= new AVLTree<>();
             x.insert(key,ind);
             super.insert(key,x);
             numitems++;
         }
         else {
             table.get(slot).insert(key,ind);
             numitems++;
            }
        }

    }


    public static class AVLNode<E extends Comparable<E>,T> {
        public E item;
        public LinkedList<T> list;
        public int height;
        public AVLNode<E,T> left,right;


        public AVLNode(){
            this.item=null;
            list=new LinkedList<T>();
            height=0;
            this.left=null;
            this.right=null;
        }

        public AVLNode(E x, T node){
            this.item=x;
            list=new LinkedList<T>();
            list.add(node);
            height=1;
            left=AVLTree.NIL;
            right=AVLTree.NIL;
        }
        public AVLNode(E x, T node,AVLNode<E,T> leftchild, AVLNode<E,T> rightchild, int h){
            this.item=x;
            list=new LinkedList<T>();
            list.add(node);
            height=h;
            this.left=leftchild;
            this.right=rightchild;
        }
        public void add(T node){
            list.add(node);
            return;
        }



    }

    public static class AVLTree<E extends Comparable<E>,T> {
        private AVLNode<E,T> root;
        static final AVLNode NIL = new AVLNode();
        public AVLTree(){
            root=NIL;
        }
        public boolean isEmpty(){
            if(root.height==0) return true;
            else return false;
        }
        public void clear(){
            root=NIL;
            return;
        }

        public AVLNode<E,T> search(E x){
            return searchItem(root,x);
        }
        public AVLNode<E,T> searchItem(AVLNode<E,T> tNode, E x){
            if(tNode==NIL) return NIL;
            else if(x.compareTo(tNode.item)==0) return tNode;
            else if(x.compareTo(tNode.item)<0) return searchItem(tNode.left,x);
            else return searchItem(tNode.right,x);
        }

        public void insert(E x, T index){
            root=insertItem(root,x,index);
        }

        private AVLNode<E,T> insertItem(AVLNode<E,T> tNode, E item, T index){
            int type;
            if(tNode==NIL){
                tNode=new AVLNode(item, index);
            }
            else if(item.compareTo(tNode.item)<0){
                tNode.left=insertItem(tNode.left,item,index);
                tNode.height=1+Math.max(tNode.right.height,tNode.left.height);
                type=needBalance(tNode);
                if(type!=NO_NEED)
                    tNode=balanceAVL(tNode,type);
            }
            else if(item.compareTo(tNode.item)>0){
                tNode.right=insertItem(tNode.right,item,index);
                tNode.height=1+Math.max(tNode.right.height,tNode.left.height);
                type=needBalance(tNode);
                if(type!=NO_NEED)
                    tNode=balanceAVL(tNode,type);
            }
            else{
                tNode.add(index);
            }
            return tNode;
        }
        //balancing operations
        private final int LL=1, LR=2, RR=3, RL=4, NO_NEED=0, ILLEGAL=-1;
        private int needBalance(AVLNode<E,T> t){
            int type=ILLEGAL;
            if(t.left.height+2<=t.right.height){
                if((t.right.left.height)<=t.right.right.height) type=RR;
                else type=RL;
            }
            else if(t.left.height>=t.right.height+2){
                if((t.left.left.height)>=t.left.right.height) type=LL;
                else type=LR;
            }
            else type=NO_NEED;
            return type;

        }
        private AVLNode<E,T> balanceAVL(AVLNode<E,T> tNode,int type){
            AVLNode<E,T> returnNode =NIL;
            switch (type){
                case LL:
                    returnNode=rightRotate(tNode);
                    break;
                case LR:
                    tNode.left=leftRotate(tNode.left);
                    returnNode=rightRotate(tNode);
                    break;
                case RR:
                    returnNode=leftRotate(tNode);
                    break;
                case RL:
                    tNode.right=rightRotate(tNode.right);
                    returnNode=leftRotate(tNode);
                    break;
                default:
                    System.out.println("Impossible type!");
                    break;
            }
            return returnNode;
        }

        private AVLNode<E,T> leftRotate(AVLNode<E,T> t){
            AVLNode<E,T> Rchild=t.right;
            if(Rchild==NIL){
                System.out.println("ERROR: Rchild shouldn't be NIL!");
            }
            AVLNode<E,T> RLchild=Rchild.left;
            Rchild.left=t;
            t.right=RLchild;
            t.height=1+Math.max(t.left.height,t.right.height);
            Rchild.height=1+Math.max(Rchild.left.height,Rchild.right.height);
            return Rchild;
        }
        private AVLNode<E,T> rightRotate(AVLNode<E,T> t){
            AVLNode<E,T> Lchild=t.left;
            if(Lchild==NIL){
                System.out.println("ERROR: Lchild shouldn't be NIL!");
            }
            AVLNode<E,T> LRchild=Lchild.right;
            Lchild.right=t;
            t.left=LRchild;
            t.height=1+Math.max(t.left.height,t.right.height);
            Lchild.height=1+Math.max(Lchild.left.height,Lchild.right.height);
            return Lchild;
        }

        private String preorder(AVLNode<E,T> t){
            String result="";
            if(t!=NIL){
                result+=(String)(t.item)+" ";
                result+=preorder(t.left);
                result+=preorder((t.right));
            }
            return result;
        }

    }

    public static class Index {
        int x, y;
        public Index(int a, int b) {
            this.x = a;
            this.y = b;
        }
    }

}