import java.io.*;
import java.util.*;

public class SortingTest
{
    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
            int[] value;	// 입력 받을 숫자들의 배열
            String nums = br.readLine();	// 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r')
            {
                // 난수일 경우
                isRandom = true;	// 난수임을 표시

                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

                Random rand = new Random();	// 난수 인스턴스를 생성한다.

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            }
            else
            {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true)
            {
                int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0))
                {
                    case 'B':	// Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':	// Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':	// Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':	// Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':	// Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':	// Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return;	// 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom)
                {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                }
                else
                {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++)
                    {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        }
        catch (IOException e)
        {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //강의자료 참고하였음.
    private static int[] DoBubbleSort(int[] value)
    {
        // TODO : Bubble Sort 를 구현하라.
        // value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
        // 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
        // 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
        // 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
        for(int i =value.length-1;i>0;i--){
            for(int j=0;j<i;j++){
                if(value[j]>value[j+1]){
                    int temp=value[j];
                    value[j]=value[j+1];
                    value[j+1]=temp;
                }
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //강의자료 참고하였음.
    private static int[] DoInsertionSort(int[] value)
    {
        for(int i=1;i<value.length;i++){
            int j=i-1;
            int insertitem=value[i];
            for(;j>=0;j--){
                if(value[j]<=insertitem) break;
                else value[j+1]=value[j];
            }
            value[j+1]=insertitem;
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //강의자료 참고하였음.
    private static int[] DoHeapSort(int[] value)
    {
        // TODO : Heap Sort 를 구현하라.
        buildHeap(value);
        for(int i=value.length-1;i>0;i--){
            value[i]=deleteMax(value,i+1);
        }

        return (value);
    }

    private static void percolateDown(int [] A, int i,int size){
    int child=2*i+1;
    int right=2*i+2;
    if(child<size){
        if(right<size && A[child]<A[right]) child=right;
        if(A[i]<A[child]) {
            int tmp=A[i];
            A[i]=A[child];
            A[child]=tmp;
            percolateDown(A,child,size);
        }
    }
    }
    private static void buildHeap(int [] A){
        for (int i=(int)((A.length-2)/2);i>=0;i--){
            percolateDown(A,i,A.length);
        }
    }
    private static int deleteMax(int [] A, int size){
        int max=A[0];
        A[0]=A[size-1];
        percolateDown(A,0,size-1);
        return max;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //강의자료+중간시험문제 참고하였음
    private static int[] DoMergeSort(int[] value){
        int[] newclone = (int[])value.clone();
        return MergeSort(newclone,value,0,value.length-1);
    }

    private static int[] MergeSort(int[] newclone,int[] value,int p,int r)
    {
        //newclone에 있는 것을 정렬하여 value에 넣음
        // TODO : Merge Sort 를 구현하라.
        if(r>p){
            int q=(int)((p+r)/2);
            MergeSort(value,newclone,p,q);
            MergeSort(value,newclone,q+1,r);
            merge(newclone,value,p,q,r);
        }
        return (value);
    }
    //newclone에 있는거 merge하여 value에 넣음
    private static void merge(int[] newclone, int[] value,int p, int q, int r){
        int i=p, j=q+1, t=p;
        while(i<=q && j<=r){
            if(value[i]<=value[j]) value[t++]=newclone[i++];
            else value[t++]=newclone[j++];
        }
        while(i<=q) value[t++]=newclone[i++];
        while(j<=r) value[t++]=newclone[j++];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //강의자료 참고하였음.
    private static int[] DoQuickSort(int[] value){
        return QuickSort(value,0,value.length-1);
    }


    private static int[] QuickSort(int[] value,int p,int r)
    {
        // TODO : Quick Sort 를 구현하라.
        int q=partition(value,p,r);
        if(q>p) QuickSort(value,p,q-1);
        if(q<r) QuickSort(value,q+1,r);
        return (value);
    }

    private static int partition(int[] value, int p, int r){
        int a=value[r];
        int i=p-1;
        for(int j=p;j<r;j++){
            if(value[j]<a) {
                int tmp=value[++i];
                value[i]=value[j];
                value[j]=tmp;
            }
        }
        int tmp=value[i+1];
        value[i+1]=value[r];
        value[r]=tmp;
        return i+1;

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //강의자료 참고하였음.
    private static int[] DoRadixSort(int[] value)
    {
        // TODO : Radix Sort 를 구현하라.

        return DoQuickSort(value);
    }
}
