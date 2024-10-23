import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;


/**
 * Classe Main que executa o programa
 * @author Diogo Almeida 79810, Andre Guerreiro 79809, Vasco Apolinario 79944
 * @version 1
 */
public class Main {
    /**
     * Metodo main que executa o programa
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Astar s = new Astar();
        //Iterator<Astar.State> it = s.solve(new Containers(sc.nextLine()), new Containers(sc.nextLine()));
        Astar.State solution = s.solve(new Containers(sc.nextLine()), new Containers(sc.nextLine()));
        if (solution ==null) System.out.println("no solution found");
        else{
            System.out.println(solution.getLayout());
            System.out.println((int)solution.getF());
            /*
            while (it.hasNext()){
                Astar.State i = it.next();
                System.out.println(i);
                if(!it.hasNext()) System.out.println((int) i.getF());
            }*/
        }
        sc.close();
    }
}
