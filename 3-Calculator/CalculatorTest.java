import java.io.*;
import java.util.Stack;


public class CalculatorTest {

	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;
				command(input);
			} catch (Exception e) {
				System.out.println("ERROR");
			}

		}
	}

	private static void command(String input) {
		//infix to postfix
		//p is the postfix string, op is the stack to temporarily save operators
		String p = "";
		Stack<Character> op = new Stack<>();
		boolean digitprev = false;
		int bracket = 0;
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch == ' '||ch=='\t') continue;

			else if (Character.isDigit(ch)) {
				if(i>0&&input.charAt(i-1)==')'){
					System.out.println("ERROR");
					return;
				}
				if(digitprev==true && (input.charAt(i-1)==' '||input.charAt(i-1)=='\t')) {
					System.out.println("ERROR");
					return;
				}
				if (digitprev == true || p.length() == 0) p = p + ch;
				else {
					p = p + " ";
					p = p + ch;
				}


				digitprev = true;
				continue;

			} else if (ch == '(') {
				bracket++;
				if((op.isEmpty()&&p.length()!=0)||digitprev==true){
					System.out.println("ERROR");
					return;
				}
				op.push('(');
				continue;
			} else if (ch == ')') {
				bracket--;
				if(!op.isEmpty()&&digitprev==false&&op.peek()=='('){
					System.out.println("ERROR");
					return;
				}
				while (!op.isEmpty() && op.peek() != '(') {
					p = p + " ";
					p = p + op.pop();
				}
				digitprev = true;
				op.pop();
				continue;
			} else if (ch == '+' || ch == '-') {
				if(!op.isEmpty()&&digitprev==false&&ch=='+'&&op.peek()!=')'){
					System.out.println("ERROR");
					return;
				}


				if (digitprev == true || ch == '+') {

					if (!op.isEmpty()) {
						while(!op.isEmpty()&&op.peek()!='('){
							p = p + " ";
							p = p + op.pop();
						}
							op.push(ch);
						}
					else op.push(ch);
					}
				else op.push('~');
				digitprev = false;
				//System.out.println(op);
				//System.out.println(p);
				continue;

				}

			else if (ch == '^') {
				if(!op.isEmpty()&&digitprev==false&&ch=='^'&&op.peek()!=')'){
					System.out.println("ERROR");
					return;
				}
				op.push(ch);
				digitprev = false;
				continue;
			} else if (ch == '*' || ch == '%' || ch == '/') {
				if (!op.isEmpty()) {
					if(digitprev==false&&ch=='^'&&op.peek()!=')'){
						System.out.println("ERROR");
						return;
					}
					while(!op.isEmpty()&&op.peek()!='('&&(op.peek() != '+' && op.peek() != '-')) {
							p = p + " ";
							p = p + op.pop();
					}
					digitprev=false;
					op.push(ch);
				} else op.push(ch);
				//System.out.println(op);
				//System.out.println(p);
				digitprev=false;
				continue;
			}
		}

		while (!op.isEmpty()) {
			char temp = op.pop();
			if (temp != '(') {
				p = p + " ";
				p = p + temp;
			}
		}

		//evaluation of the postfix expression. Slight modified code from Lecture note "STACK"
		Stack<Long> s = new Stack<>();
		long A,B;
		digitprev=false;
		for (int i = 0; i < p.length(); i++) {
			char ch = p.charAt(i);
			if (Character.isDigit(ch)) {
				if (digitprev == true) {
					long tmp = s.pop();
					tmp = 10 * tmp + (long)(ch - '0');
					s.push(tmp);
				} else s.push((long)ch - '0');
				digitprev = true;

			} else if (isOperator(ch)) {
				//when unary '-'
				if(ch=='~') {
					A=s.pop();
					long val=operation(A,ch);
					s.push(val);
				}
				//other oprators
				else {
					A = s.pop();
					B = s.pop();
					if(B==0 && A<0 && ch=='^'){
						System.out.println("ERROR");
						return;
					}
					long val = operation(A, B, ch);
					s.push(val);
					digitprev = false;
				}
			} else digitprev = false;
		}

		if(bracket!=0||s.isEmpty()){
			System.out.println("ERROR");
			return;
		}

		System.out.println(p);
		System.out.println(s.pop());


	}

	private static long operation(long a, long b, char ch){
		long val=0;
		switch(ch){
			case '+': val = b+a; break;
			case '-': val = b-a; break;
			case '*': val = b*a; break;
			case '/': val = b/a; break;
			case '%': val = b%a; break;
			case '^': val = (long)Math.pow(b,a); break;
		}
		return val;
	}
	//for unary '-' operator
	private static long operation(long a, char ch){
		if(ch=='~') return -a;
		return 0;
	}
	private static boolean isOperator(char ch){
		return ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='%'||ch=='^'||ch=='~';
	}
}
	//evaluation of the postfix expression. Slight modified code from Lecture note "STACK"
