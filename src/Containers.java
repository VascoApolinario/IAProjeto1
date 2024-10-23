import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que representa um conjunto de pilhas de containers
 * @author Diogo Almeida 79810, Andre Guerreiro 79809, Vasco Apolinario 79944
 * @version 1
 */
public class Containers implements Ilayout,Cloneable{

    private List<List<Character>> stacks;
    private final HashMap<Character,Integer> containerCosts;
    private int energycost;

    /**
     * Construtor da classe Containers
     * @param str string
     */
    public Containers(String str){
        String[] splitedString = str.split(" "); //Recebe string e separa-a em tokens por cada espaço
        this.stacks = new ArrayList<>();
        this.containerCosts = new HashMap<>();
        Pattern pattern = Pattern.compile("([a-zA-Z])(\\d+)"); //Classe Pattern é usada para definir um padrão. A regular expression "([A-Z])(\\d+)" é compilada em pattern.
                                                                 //os parenteses servem para criar grupos. grupo(1) = [A-Z] -> letra de A a Z , grupo(2) = \\d+ -> um ou mais digitos
        for(String s : splitedString){
            List<Character> containerStack = new ArrayList<>(); //vai criando uma stack para cada token
            if (s.matches(".*\\d.*")) { //Verifica se a string contem digitos. Se conter vai à procura do padrao.
                Matcher matcher = pattern.matcher(s); //Classe Matcher é usada para procurar os padroes na string
                while (matcher.find()) {   //enquanto o padrao for encontrado
                    char containerLetter = matcher.group(1).charAt(0); //char que identifica o contentor
                    int containerCost = Integer.parseInt(matcher.group(2)); //peso do contentor
                    this.containerCosts.put(containerLetter, containerCost); //adiciona no hashmap a key = letra contentor, value = peso contentor
                    containerStack.add(containerLetter); //adiciona à stack a letra de contentor.
                }
            }
            else{ //Se não conter digitos adiciona apenas os chars à stack.
                for(char c : s.toCharArray()){
                    containerStack.add(c);
                }
            }
            insertInOrder(containerStack);
        }
        this.energycost = 0;
    }

    /**
     * Metodo que insere uma nova stack de containers de maneira a manter por ordem alfabetica em relação ao container no chão.
     * @param newStack nova stack de containers que se quer inserir no conjunto de stacks de containers
     */
    private void insertInOrder(List<Character> newStack) {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).getFirst() > newStack.getFirst()) {
                stacks.add(i, newStack);
                return;
            }
        }
        stacks.add(newStack);
    }

    /**
     * Metodo equals da classe Containers
     * @param o objeto
     * @return true se for igual false se não
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Containers that)) return false;

        //Verifica se ambas as listas de stacks têm o mesmo tamanho
        if (this.stacks.size() != that.stacks.size())
            return false;


        return this.stacks.equals(that.stacks);
    }

    /**
     * Metodo hashCode da classe Containers
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (stacks == null ? 0 : stacks.hashCode());
        hash = hash/3;
        return hash;
    }

    /**
     * Metodo toString da classe Containers
     * @return string
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(List<Character> stack : this.stacks){
            s.append(stack.toString());
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Metodo children da classe Containers
     * @return nos filhos
     */
    @Override
    public List<Ilayout> children() {
        List<Ilayout> children=new ArrayList<>();
        char movedContainer;
        for(int i = 0; i < this.stacks.size(); i++){ // retiramos o contentor da stack no indice i
            if(this.stacks.get(i).size() > 1){ //Se o contentor não tiver no chão, cria-se nova stack de contentores e coloca-se esse contentor no chão.
                Containers child;
                try {
                    child = this.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                movedContainer = child.stacks.get(i).removeLast();
                ArrayList<Character> newContainerStack = new ArrayList<>();
                newContainerStack.add(movedContainer);
                child.insertInOrder(newContainerStack);
                child.energycost = containerCosts.get(movedContainer);
                children.add(child);
            }
            for(int j = 0; j < this.stacks.size(); j++){ //colocamos o contentor retirado da stack no indice i na stack de indice j

                    if(i!=j){
                        Containers child;
                        try {
                            child = this.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                        movedContainer = child.stacks.get(i).removeLast();
                        child.stacks.get(j).add(movedContainer);
                        if(child.stacks.get(i).isEmpty()){ //Se o contentor estava no chão eliminamos a stack
                            child.stacks.remove(i);
                        }
                        child.energycost = containerCosts.get(movedContainer);
                        children.add(child);
                    }
            }
        }
        return children;
    }

    /**
     * Metodo isGoal da classe Containers
     * @param l interface Ilayout
     * @return true se for o goal, false se nao
     */
    @Override
    public boolean isGoal(Ilayout l) {
        return this.equals(l);
    }

    /**
     * Metodo getG da classe Containers
      * @return custo
     */
    @Override
    public double getG() {
        return energycost;
    }

    /**
     * Getter do atributo containerCosts
     * @return containerCosts
     */
    public HashMap<Character, Integer> getContainerCosts() {
        return containerCosts;
    }

    /**
     * Metodo getH da classe Containers
     * @param l interface Ilayout
     * @return heuristica h
     */
    @Override
    public double getH(Ilayout l){
        double h = 0;
        Containers goal = (Containers)l;
        Map<Character, List<Character>> goalStacks = new HashMap<>();
        for (List<Character> goalstack : goal.stacks) {
            goalStacks.put(goalstack.getFirst(), goalstack);
        }
        for(List<Character> stack : this.stacks){
            if(!goalStacks.containsKey(stack.getFirst())){
                h += evaluateWrongContainers(0,stack,goalStacks);
            }
            else{
                List<Character> goalstack = goalStacks.get(stack.getFirst());
                for(int i = 0; i < stack.size(); i++){
                    if(i >= goalstack.size() || !stack.get(i).equals(goalstack.get(i))){
                        if(goalstack.contains(stack.get(i))){
                            h += this.containerCosts.get(stack.get(i));
                        }
                        h += evaluateWrongContainers(i,stack,goalStacks);
                        break;
                    }
                }
            }
        }
        return h;
    }

    /**
     * Metodo que calcula a heuristica com base no custo de movimentacao dos Containers na posição errada e os seus movimentos minimos necessarios numa stack.
     * @param start indice do primeiro containers errado encontrado na stack (debaixo para cima).
     * @param stack stack onde foi encontrado o containers errado.
     * @param goalstacks stacks do objetivo a ser alcancado
     * @return h
     */
    public double evaluateWrongContainers(int start, List<Character> stack, Map<Character,List<Character>> goalstacks){
        double h = 0;
        Set<Character> charsBelow = new HashSet<>();
        Set<Character> charsBelowGoal;
        char containerOnIndex;
        char containerBase = stack.getFirst();
        boolean shouldBeAbove;
        for(int i = start ; i < stack.size(); i++){
            containerOnIndex = stack.get(i);
            charsBelowGoal = new HashSet<>();
            shouldBeAbove = false;
            if(i > start) {
                charsBelow.add(stack.get(i-1));

                for (List<Character> goalstack : goalstacks.values()) {
                    if (goalstack.contains(containerOnIndex)) {
                        if(containerBase == goalstack.getFirst()){
                            shouldBeAbove = true;
                        }
                        charsBelowGoal.addAll(goalstack.subList(0,goalstack.indexOf(containerOnIndex)));
                    }
                }

                Set<Character> intersection = new HashSet<>(charsBelow);
                intersection.retainAll(charsBelowGoal);
                if (intersection.isEmpty()) {
                    if(shouldBeAbove)
                        h += 2*this.containerCosts.get(containerOnIndex);
                    else
                        h += this.containerCosts.get(containerOnIndex);

                } else {
                    h += 2 * this.containerCosts.get(containerOnIndex);
                }
            }
            else {
                h += this.containerCosts.get(containerOnIndex);
            }

        }
        return h;
    }

    /**
     * Metodo clone da classe Containers
     * @return clone
     * @throws CloneNotSupportedException cloning is not supported
     */
    @Override
    protected Containers clone() throws CloneNotSupportedException {
            Containers clone = (Containers) super.clone();
            clone.stacks = new ArrayList<>();
            for(List<Character> stack : this.stacks){
                clone.stacks.add(new ArrayList<>(stack));
            }
            clone.energycost = this.energycost;
            return clone;
    }

}
