import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.PriorityQueue;


public class Subway {

    //지하철 고유코드 테이블
    public static Hashtable<String,Vertex> CodeTable=new Hashtable<>();
    //지하철 역 테이블
    public static Hashtable<String,ArrayList<Vertex>> StationTable=new Hashtable<>();
    //지하철 고유코드를 int로 연결해주는 테이블
    public static Hashtable<String,Integer> IndexTable=new Hashtable<>();


    //io 부분은 과제 5 matching skeleton code 참고하였음.
    public static void main(String[] args) {
        datainit(args[0]);
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
        String[] inputstation=input.split("\\s+");
        dijkstra(inputstation[0],inputstation[1]);
    }
    //최단 경로 알고리즘. 강의자료 참고하였음
    private static void dijkstra(String start, String end){
        ArrayList<Vertex> startstation=StationTable.get(start);
        ArrayList<Vertex> destination=StationTable.get(end);
        int answer=Integer.MAX_VALUE;
        String answerpath="";
        if(startstation==null || destination==null) {
            System.out.println("잘못된 입력입니다. 해당 역이 존재하지 않습니다.");
            return;
        }
        for(int i=0;i<startstation.size();i++){
            Vertex startst=startstation.get(i);
            PriorityQueue<Edge> priorityQueue=new PriorityQueue<>();
            boolean[] check=new boolean[CodeTable.size()];
            String[] dist2=new String[CodeTable.size()];
            Integer[] dist=new Integer[CodeTable.size()];
            for(int j=0;j<dist.length;j++){
                check[j]=false;
                dist2[j]="";
                dist[j]=Integer.MAX_VALUE;
            }

            Edge startedge=new Edge(startst,0);
            priorityQueue.add(startedge);
            dist[IndexTable.get(startst.id)]=0;
            dist2[IndexTable.get(startst.id)]=startst.id;
            while(!priorityQueue.isEmpty()){
                Edge edge=priorityQueue.poll();

                Vertex nextst=edge.to;
                int dest=IndexTable.get(nextst.id);
                if(check[dest]) continue;
                else check[dest]=true;

                for(int l=0;l<nextst.adjacentedge.size();l++){
                    Edge next=nextst.adjacentedge.get(l);
                    if(dist[IndexTable.get(next.to.id)]>=dist[dest]+next.time){
                        dist2[IndexTable.get(next.to.id)]=nextst.id;
                        dist[IndexTable.get(next.to.id)]=dist[dest]+next.time;
                        priorityQueue.add(new Edge(next.to,dist[IndexTable.get(next.to.id)]));
                    }
                }



            }
            for(int k=0;k<destination.size();k++){
                if(dist[IndexTable.get(destination.get(k).id)]<answer){
                    answerpath=destination.get(k).name;
                    answer=dist[IndexTable.get(destination.get(k).id)];
                    Vertex currentstation=destination.get(k);

                    while(true){
                        String text=dist2[IndexTable.get(currentstation.id)];
                        currentstation=CodeTable.get(text);
                        String text2=currentstation.name;

                        String[] answerpathsplit=answerpath.split("\\s");

                        if(answerpathsplit[0].equals(text2)){
                            answerpathsplit[0]='['+answerpathsplit[0]+']';
                            answerpath="";
                            for(int a=0;a<answerpathsplit.length;a++){
                                answerpath+=answerpathsplit[a];
                                answerpath+=" ";
                            }
                            answerpath=answerpath.substring(0,answerpath.length()-1);
                        }
                        else{
                            answerpath=text2+" "+answerpath;
                        }
                        if(text2==startst.name) break;
                    }
                }
            }
        }
        System.out.println(answerpath);
        System.out.println(answer);




    }

    private static void datainit (String data){

        try {
            BufferedReader bf = new BufferedReader(new FileReader(data));
            int i=0;
            while(true){
                String line=bf.readLine();
                if (line.isEmpty()) break;
                String[] station=line.split("\\s+");
                Vertex newcode=new Vertex(station[0],station[1],station[2]);
                CodeTable.put(station[0],newcode);
                IndexTable.put(station[0],i);
                i++;
                if(StationTable.get(station[1])==null){
                    ArrayList<Vertex> newstation=new ArrayList<>();
                    newstation.add(newcode);
                    StationTable.put(station[1],newstation);
                }
                else{
                    ArrayList<Vertex> samestation=StationTable.get(station[1]);
                    samestation.add(newcode);
                    for(int j=0;j<samestation.size()-1;j++){
                        Vertex jthcode=samestation.get(j);
                        jthcode.addEdge(new Edge(newcode,5));
                        newcode.addEdge(new Edge(jthcode,5));
                    }
                }
            }

            while(true){
                String line=bf.readLine();
                if (line==null) break;
                String[] edge=line.split("\\s+");
                Edge newedge=new Edge(CodeTable.get(edge[1]),Integer.parseInt(edge[2]));
                CodeTable.get(edge[0]).addEdge(newedge);
            }

        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }


    }

    private static class Vertex{
        private final String id;
        private final String line;
        private final String name;
        public ArrayList<Edge> adjacentedge=new ArrayList<>();
        public Vertex(){
            id=null;
            line=null;
            name=null;
        }
        public Vertex(String i,String n,String l){
            id=i;
            line=l;
            name=n;
        }

        public void addEdge(Edge e){
            adjacentedge.add(e);
        }

    }

    private static class Edge implements Comparable<Edge>{
        private final Vertex to;
        private final Integer time;

        public Edge(Vertex t, Integer ti){
            time=ti;
            to=t;
        }

        public int compareTo(Edge e){
            if(this.time<e.time) return -1;
            else if (this.time>e.time) return 1;
            else return 0;
        }

    }
}