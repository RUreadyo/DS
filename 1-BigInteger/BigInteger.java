import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";

    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\s*([+-]?)\\s*(\\d+)\\s*([+\\-*])\\s*([+-]?)\\s*(\\d+)\\s*");


    //BigInteger 절댓값, 부호 저장
    private byte[] absnum=new byte[201];
    private boolean sign= true;


    //BigInteger 값 저장
    public BigInteger(int i)
    {
        sign=(i>=0);
        int absi=(i>=0)?i:-i, index=0;

        while(absi>0){
            absnum[index]=(byte)(absi%10);
            absi/=10;
            index++;
        }


    }

    public BigInteger(int[] num1)
    {
        sign=(num1[0]>=0);
        num1[0]=(num1[0]>=0)?num1[0]:-num1[0];

        for(int index=0;index<num1.length;index++){
            absnum[index]=(byte)(num1[num1.length-1-index]);
        }



    }

    public BigInteger(String s)
    {
        char first=s.charAt(0);
        int start=0;
        if(first=='-'){
            start = 1;
            for(int i=1;i<s.length();i++){
                if(s.charAt(i)!='0'){
                    sign=false;
                    break;
                }
            }

        }
        if(first=='+'){
            start=1;
        }

        for(;start<s.length();start++){
            absnum[s.length()-1-start]=(byte)(s.charAt(start)-'0');
        }

    }

    public BigInteger add(BigInteger big)
    {
        BigInteger ans=new BigInteger(0);
        if(sign==big.sign) {
            ans.sign = sign;
            byte c = 0;
            for (int index = 0; index < 101; index++) {
                byte x = (byte)(absnum[index] + big.absnum[index] + c);
                ans.absnum[index] = (byte)((x > 9) ? x - 10 : x);
                c = (byte)((x > 9) ? 1 : 0);
            }
        }
        else {
            big.sign = !big.sign;
            ans=subtract(big);
        }

        return ans;


    }

    public BigInteger subtract(BigInteger big) {
        BigInteger ans = new BigInteger(0);

        //두 수의 부호가 다른 경우는 덧셈으로 처리 가능
        int index;
        if (sign != big.sign) {
            big.sign=!big.sign;
            ans = add(big);
            return ans;
        }


        //두 수의 부호가 같은 경우 뺼셈을 하면 되는데, 먼저 절댓값의 크기를 비교함.
        boolean biggerabs=true;
        for (index = 99; index >= 0; index--) {
            biggerabs = (absnum[index] >= big.absnum[index]) ? true : false;
            if (absnum[index] != big.absnum[index]) break;
        }

        ans.sign = (biggerabs) ? sign : !sign;

        //두 수가 같은 경우 바로 0을 return함
        if (index == -1) {
            ans.sign=true;
            return ans;
        }


        //두 수가 다르고, 부호는 같은 경우 뺄셈

        else {
            byte c = 0;
            for (index = 0; index < 100; index++) {
                byte x = (byte)((biggerabs) ? absnum[index] - big.absnum[index] - c : big.absnum[index]-absnum[index] - c);
                c = (byte)((x >= 0) ? 0 : 1);
                ans.absnum[index] =(byte)((x >=0) ? x : x + 10);
            }
        }

        return ans;
    }



    public BigInteger multiply(BigInteger big)
    {
        BigInteger ans= new BigInteger(0);
        int [] ans2=new int[201];
        ans.sign=(sign==big.sign);
        for(int i=0;i<100;i++) {
            for (int j = 0; j < 100; j++) {
                int x = absnum[i] * big.absnum[j];
                ans2[i + j] += x % 10;
                ans2[i + j + 1] += x / 10;

            }
        }
        for(int i=0;i<199;i++) {
            ans2[i + 1] += ans2[i] / 10;
            ans2[i] %= 10;
        }
        for(int i=0;i<200;i++) {
            ans.absnum[i]=(byte)(ans2[i]);
        }


        return ans;
    }

    @Override
    public String toString()
    {
        String ans="";
        int last=absnum.length-1;
        for(;last>=0&&absnum[last]==0;last--);
        if(last==-1) {
            ans=ans + "0";
            return ans;
        }
        if(!sign) ans=ans+"-";
        for(;last>=0;last--){
            ans=ans+absnum[last];
        }
        return ans;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {   //(0개 이상의 공백) (부호 0~1개) (0개 이상의 공백) (숫자) (0개 이상의 공백) (+/-/*) (0개 이상의 공백) (부호 0~1개) (0개 이상의 공백) (숫자) (0개 이상의 공백)
        // implement here
        // parse input
        // using regex is allowed
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);

        String a="";
        String b="";
        String c="";

        while(matcher.find()){
            a=matcher.group(1)+matcher.group(2);
            c=matcher.group(3);
            b=matcher.group(4)+matcher.group(5);




        }


        BigInteger num1 = new BigInteger(a);
        BigInteger num2 = new BigInteger(b);
        BigInteger ans = new BigInteger(0);

        if(c.equals("+")) ans=num1.add(num2);
        if(c.equals("-")) ans=num1.subtract(num2);
        if(c.equals("*")) ans=num1.multiply((num2));

        return ans;
    }

    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();

                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }




                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);

        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());

            return false;
        }
    }

    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
