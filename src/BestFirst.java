import java.util.*;

public class BestFirst {
    protected Queue<State> abertos;
    private Map<Ilayout,State> fechados;
    private State actual;
    private Ilayout objective;

    static class State {
        private Ilayout layout;
        private State father;
        private double g;

        public State(Ilayout l, State n) {
            layout = l;
            father = n;
            if(father != null)
                g = father.g + l.getG();
            else g = 0.0;
        }

        public String toString() {
            return layout.toString();
        }

        public double getG() {return g;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            State n = (State) o;
            return this.layout.equals(n.layout);
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }
    }


    final private List<State> sucessores(State n){
        List<State> sucs = new ArrayList<>();
        List<Ilayout> children = n.layout.children();
        for(Ilayout e : children){
            if(n.father == null || !e.equals(n.father.layout)){
                State nn = new State(e,n);
                sucs.add(nn);
            }
        }
        return sucs;
    }

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

    public double ManhattanDistance(State current, State goal){
        double g = current.getG();
        double h = 0;
        return g + h;
    }

    final public Iterator<State> solve(Ilayout s, Ilayout goal){
        objective = goal;
        abertos = new PriorityQueue<>(10,(s1,s2) -> (int) Math.signum(s1.getG() - s2.getG()));
        Map<Ilayout,State> abertosMap = new HashMap<>();
        fechados = new HashMap<>();
        abertos.add(new State(s, null));
        abertosMap.put(s, abertos.peek());
        List<State> sucs;

        while(!abertos.isEmpty())
        {
            actual = abertos.remove();
            if (actual.layout.isGoal(objective)) {
                return getLineage(actual);
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
