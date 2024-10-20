import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;



public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        BestFirst s = new BestFirst();
        //Iterator<BestFirst.State> it = s.solve(new Containers(sc.nextLine()), new Containers(sc.nextLine()));
        BestFirst.State solution = s.solve(new Containers(sc.nextLine()), new Containers(sc.nextLine()));
        if (solution ==null) System.out.println("no solution found");
        else{
            System.out.println(solution.getLayout());
            System.out.println((int)solution.getF());
            /*
            while (it.hasNext()){
                BestFirst.State i = it.next();
                System.out.println(i);
                if(!it.hasNext()) System.out.println((int) i.getF());
            }*/
        }
        sc.close();
    }
}
