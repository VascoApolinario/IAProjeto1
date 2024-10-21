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
        private double h;
        private double f;

        public State(Ilayout l, State n, Ilayout goal) {
            layout = l;
            father = n;
            if(father != null)
                g = father.g + l.getG();

            else g = 0.0;
            h = l.getH(goal);
            f = g + h;
        }

        public String toString() {
            return layout.toString();
        }

        public double getG() {return g;}

        public double getH() {return h;}

        public double getF() {return f;}

        public Ilayout getLayout() {return layout;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            State n = (State) o;
            return this.layout.equals(n.layout);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + toString().hashCode();
            return hash;
        }
    }


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
                //System.out.println(fechados.size());
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
