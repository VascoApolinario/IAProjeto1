import java.util.*;

/**
 * Classe Astar
 * @author Diogo Almeida 79810, Andre Guerreiro 79809, Vasco Apolinario 79944
 * @version 1
 */
public class Astar {
    protected Queue<State> abertos;
    private Map<Ilayout,State> fechados;
    private State actual;
    private Ilayout objective;

    static class State {
        private Ilayout layout;
        private State father;
        private double g;
        private double h;
        private double f;

        /**
         * Construtor da classe State
         * @param l interface Ilayout
         * @param n estado pai
         * @param goal interface Ilayout do objetivo
         */
        public State(Ilayout l, State n, Ilayout goal) {
            layout = l;
            father = n;
            if(father != null)
                g = father.g + l.getG();

            else g = 0.0;
            h = l.getH(goal);
            f = g + h;
        }

        /**
         * Metodo toString da classe State
         * @return string
         */
        public String toString() {
            return layout.toString();
        }

        /**
         * Metodo que retorna a função de custo total
         * @return f
         */
        public double getF() {return f;}

        /**
         * Getter do atributo layout
         * @return layout
         */
        public Ilayout getLayout() {return layout;}

        /**
         * Metodo equals da classe State
         * @param o objeto
         * @return true se for igual, false se nao
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            State n = (State) o;
            return this.layout.equals(n.layout);
        }

        /**
         * Metodo hashCode da classe State
         * @return hashcode
         */
        @Override
        public int hashCode() {
            return toString().hashCode();
        }
    }

    /**
     * Metodo que retorna os sucessores de um estado
     * @param n estado
     * @return lista de estados
     */
    final private List<State> sucessores(State n){
        List<State> sucs = new ArrayList<>();
        List<Ilayout> children = n.layout.children();
        for(Ilayout e : children){
            if(n.father == null || !e.equals(n.father.layout)){
                State nn = new State(e,n,objective);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    /**
     * Metodo que retorna o caminho de um estado ate ao estado inicial
     * @param a estado
     * @return iterador
     */
    public Iterator<State> getLineage(State a) {
        List<State> ancestors = new ArrayList<>();
        while(a.father != null) {
            ancestors.add(a);
            a = a.father;
        }
        ancestors.add(a);
        Collections.reverse(ancestors);
        return ancestors.iterator();
    }

    /**
     * Metodo que retorna a profundidade de um estado
     * @param goalState estado objetivo
     * @return profundidade
     */
    public int getDepth(State goalState) {
        int depth = 0;
        State current = goalState;
        while (current.father != null) {
            depth++;
            current = current.father;
        }
        return depth;
    }


    /**
     * Metodo que resolve o problema com o algoritmo A*
     * @param s estado inicial
     * @param goal estado objetivo
     * @return estado
     */
    final public State solve(Ilayout s, Ilayout goal){
        objective = goal;
        abertos = new PriorityQueue<>(10,(s1,s2) -> (int) Math.signum(s1.getF() - s2.getF()));
        Map<Ilayout,State> abertosMap = new HashMap<>();
        fechados = new HashMap<>();
        abertos.add(new State(s, null,objective));
        abertosMap.put(s, abertos.peek());
        List<State> sucs;
        while(!abertos.isEmpty())
        {
            actual = abertos.remove();
            if (actual.layout.isGoal(objective)) {
                //System.out.println("\n|Resultado|\nNós Expandidos: " + fechados.size() + "\nNós Gerados: " + (fechados.size() + abertos.size()) + "\nComprimento da solução: " + getDepth(actual) + "\nPenetrância: " + (double) getDepth(actual) / fechados.size() + "\n");
                return actual;
            }
            else {
                if (!fechados.containsKey(actual.layout)) {
                    sucs = sucessores(actual);
                    fechados.put(actual.layout,actual);
                    abertosMap.remove(actual.layout);
                    for(State suc : sucs){
                        if (!fechados.containsKey(suc.layout) && !abertosMap.containsKey(suc.layout)) {
                            abertos.add(suc);
                            abertosMap.put(suc.layout,suc);
                        }
                    }
                }

            }
        }
        return null;
    }
}
