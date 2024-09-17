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
        ancestors = ancestors.reversed();
        return ancestors.iterator();
    }

    final public Iterator<State> solve(Ilayout s, Ilayout goal){
        objective = goal;
        abertos = new PriorityQueue<>(10,(s1,s2) -> (int) Math.signum(s1.getG() - s2.getG()));
        fechados = new HashMap<>();
        abertos.add(new State(s, null));
        List<State> sucs;
        while(!abertos.isEmpty())
        {
            actual = abertos.remove();
            if (actual.layout.isGoal(objective)) {
                return getLineage(actual);
            }
            else {
                sucs = sucessores(actual);
                fechados.put(actual.layout,actual);
                for(State suc : sucs){
                    if (!fechados.containsKey(suc.layout)) {
                        abertos.add(new State(suc.layout,actual));
                    }
                }
            }
        }
        return null;
    }
}
