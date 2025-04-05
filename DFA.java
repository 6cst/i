import java.util.*;
public class DFA{
  public static void main(String[]a){
    Scanner s=new Scanner(System.in);
    System.out.print("Enter string: ");
    String str=s.nextLine();
    int st=0;
    for(char c: str.toCharArray()){
      if(st==0) st = (c=='a'?1:0);
      else if(st==1) st = (c=='b'?2:(c=='a'?1:0));
      else if(st==2) st = (c=='c'?3:(c=='a'?1:0));
      else if(st==3) st = (c=='a'?1:0);
    }
    System.out.println("Result: "+(st==3?"acc":"reject"));
  }
}
