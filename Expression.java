import javax.swing.JOptionPane;

public class Expression {

    Expression(){
        while(true){
            String userInput=null;
            boolean correctInput = false;

//            while not correct input
            while(!correctInput){
//                get user's input
                userInput=getInput();
//            check user's input
                correctInput=checkInput(userInput);
//            if not correct input
                if(!correctInput){
                    JOptionPane.showMessageDialog(null,"Incorrect input please try again!");
                }
            }
//            no need to convert user's input into a char array
//            change user's input into a postfix expression
            String postfix = toPostfix(userInput);

//            calculate the mathematical result of the expression
            double result = evaluatePostfix(postfix);

//            convert the expression in to a "spaced" version
//            to make it more readable before displaying it
            String postfixString = spaced(postfix);
//            final output
            JOptionPane.showMessageDialog(null,"The result of the expression is:\nInfix: "+ userInput +"\nPostfix: "+postfixString+"\nResult: "+result);
        }
    }

//----------------------------------------------------------------------
//    ***METHODS***

//gets user's input
    public String getInput(){
        String tempInput=
                JOptionPane.showInputDialog("Please enter an infix numerical expression between 3 and 20 characters.\nOnly the following characters are valid: +,-,*,/,^,(,) and numbers 0-9");
        return tempInput;
    }

//    checks if valid input
    public boolean checkInput(String string){
        if(string==null){System.exit(0);}
        int length= string.length();
//        check if correct length
        if(length>20 || length<3){ return false;}

        for(int i=0; i<length; i++) {
            char c = string.charAt(i);
//            if an operator
            if (c == '+') { continue; }
            if (c == '-') { continue; }
            if (c == '*') { continue; }
            if (c == '/') { continue; }
            if (c == '^') { continue; }
            if (c == '(') { continue; }
            if (c == ')') { continue; }

//            ASCII, 0-9 in order !
//            hence easy conversion between chars and numbers
            int num = c - '0';
            if (num < 0 || num > 9) { return false; }
//            if not last character
            if(i!=length-1){
//                check if double digits e.g 22
                int numNext = string.charAt(i+1)-'0';
                if(numNext>=0 && numNext<=9){
                    JOptionPane.showMessageDialog(null,"Only use numbers in the range of 0 to 9!");
                    return false;
                }
            }
        }
//        all tests passed return true;
        return true;
    }

//    converts an infix expression to a postfix expression
    public String toPostfix(String string){
        int length=string.length();
        ArrayStack stack = new ArrayStack(length);
        String temp= "";

//        go through each char
        for(int i=0; i<length; i++){
            char c=string.charAt(i);
//            if a number
            int num=c-'0';
            if(num>=0 && num<=9){
//            is a number, append to the output
                temp+= c;
            }
            else{
//                is an operator or '()'
//                if stack is empty, push
                if(stack.isEmpty()){stack.push(c); continue;}
//                if the scanned operator is an opening parenthesis, push
                if(c=='('){stack.push(c); continue;}
//                if stack contains opening parenthesis, push
                if((char)stack.top()=='('){stack.push(c); continue;}
//                if c is a closing parenthesis, pop and append until opening parenthesis
                if(c==')'){
//                    while not a '('
                    while ((char)stack.top()!='('){
                        temp+=(char)stack.pop();
                    }
//                    remove the remaining '(' from the stack
                    stack.pop();
                    continue;
                }
//                if higher precedence, push
                if(precedence(c, (char)stack.top())){stack.push(c); continue;}

//                If lower precedence, pop and append until higher precedence or empty
//                break on encountering '()'
                while (!stack.isEmpty() && !precedence(c, (char)stack.top())){
                    if((char)stack.top()=='(' || (char)stack.top()==')'){
                        break;
                    }
                    temp+=(char)stack.pop();
                }
//                push scanned character
                stack.push(c);
            }
        }

//        pop and append remaining operators
        while (!stack.isEmpty()){
            if((char)stack.top()=='(' || (char)stack.top()==')'){
                stack.pop();
                continue;
            }
            temp+=(char)stack.pop();
        }
        return temp;
    }

//    checks precedence of the passed in operators
//    true c0 has higher precedence, false c1 has higher precedence or equal
    public boolean precedence(char c0, char c1){
        char[] c = new char[2];
        c[0]=c0;
        c[1]=c1;

        for (int i=0; i<2; i++){
            if(c[i]=='^'){c[i]=3;}
            if(c[i]=='*' || c[i]=='/'){c[i]=2;}
            if(c[i]=='+' || c[i]=='-'){c[i]=1;}
        }
        return c[0]>c[1];
    }

    public double evaluatePostfix(String string){
        int lenght=string.length();
        ArrayStack stack = new ArrayStack(20);

        for(int i=0; i<lenght; i++){
            char c= string.charAt(i);
//            convert to number
            int num= c - '0';
            if(num>=0 && num<=9){
//                if a number, push
                stack.push((double)num);
            }
            else {
//                is an operator
                double num2 = (double) stack.pop();
                double num1 = (double) stack.pop();
                double result=0;
                if(c=='^'){result=Math.pow(num1,num2);}
                else if(c=='*'){result=num1*num2;}
                else if(c=='/'){result=num1/num2;}
                else if(c=='+'){result=num1+num2;}
                else if(c=='-'){result=num1-num2;}
//                push the end result of the mathematical operation
                stack.push(result);
            }
        }
//        return the end result of the expression
        return (double)stack.top();
    }

//    adds spaces in between each character so that the
//    postfix expression can be shown clearly
    public String spaced (String string){
        int length= string.length();
        String temp="";

        for (int i=0; i<length; i++){
            char c= string.charAt(i);
            temp+=c;
            temp+=' ';
        }
        return temp;
    }
}
