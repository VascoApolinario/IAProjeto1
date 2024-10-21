import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Diogo Almeida 79810
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
                                                                 //os parênteses servem para criar grupos. grupo(1) = [A-Z] -> letra de A a Z , grupo(2) = \\d+ -> um ou mais digitos
        for(String s : splitedString){
            List<Character> containerStack = new ArrayList<>(); //vai criando uma stack para cada token
            if (s.matches(".*\\d.*")) { //Verifica se a string contém digitos. Se conter vai à procura do padrão.
                Matcher matcher = pattern.matcher(s); //Classe Matcher é usada para procurar os padrões na string
                while (matcher.find()) {   //enquanto o padrão for encontrado
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
        //newStacks.sort((s1, s2) -> Character.compare(s1.getFirst(), s2.getFirst()));
        this.energycost = 0;
    }

    /**
     * Metodo que insere uma nova stack de containers de maneira a manter por ordem alfabetica em relação ao container no chão.
     * @param newStack nova stack de conteiners que se quer inserir no conjunto de stacks de conteiners
     */
    private void insertInOrder(List<Character> newStack) {
        for (int i = 0; i < stacks.size(); i++) {
            if (Character.compare(stacks.get(i).getFirst(), newStack.getFirst()) > 0) {
                stacks.add(i, newStack);
                return;
            }
        }
        stacks.add(newStack);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Containers that)) return false;

        //Verifica se ambas as listas de stacks têm o mesmo tamanho
        if (this.stacks.size() != that.stacks.size())
            return false;


        return this.stacks.equals(that.stacks);
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (stacks == null ? 0 : stacks.hashCode());
        hash = hash/3;
        return hash;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(List<Character> stack : this.stacks){
            s.append(stack.toString());
            s.append("\n");
        }
        return s.toString();
    }


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

    @Override
    public boolean isGoal(Ilayout l) {
        return this.equals(l);
    }

    @Override
    public double getG() {
        return energycost;
    }

    public HashMap<Character, Integer> getContainerCosts() {
        return containerCosts;
    }


    //H2 - Nesta heuristica h e igual à soma de todos os custos dos containers fora do sitio
    @Override
    public double getH(Ilayout l){
        int h = 0;
        Containers goal = (Containers)l;
        Map<Character, List<Character>> goalStacks = new HashMap<>();
        for (List<Character> goalstack : goal.stacks) {
            goalStacks.put(goalstack.getFirst(), goalstack);
        }
        for(List<Character> stack : this.stacks){
            Character firstContainer = stack.getFirst();
            if(!goalStacks.containsKey(firstContainer)){
                for(Character c : stack){
                    h += this.containerCosts.get(c);
                }
            }
            else{
                List<Character> goalstack = goalStacks.get(firstContainer);
                boolean misplacedStackFound = false;
                Character currentContainer;
                for(int i = 0; i < stack.size(); i++){
                    currentContainer = stack.get(i);
                    if(!misplacedStackFound && (i >= goalstack.size() || !currentContainer.equals(goalstack.get(i)))){ //quando o current tem mais containers na stack do que o goal ou quando o char no indice do current e diferente no goal
                        misplacedStackFound = true;
                    }
                    if(misplacedStackFound){
                        h += this.containerCosts.get(currentContainer); //adiciona o custo de cada conteiner errado
                    }
                }
            }
        }
        return h;
    }
    /*
    //H3 - Nesta heuristica tentamos prever as movimentações que cada stack irá ter de fazer. posição certa h += 0; colocada na stack errada h += g; na stack certa mas posição errada h += 2*g
    @Override
    public double getH(Ilayout l){
        int h = 0;
        Containers goal = (Containers)l;
        Map<Character, List<Character>> goalStacks = new HashMap<>();
        for (List<Character> goalstack : goal.stacks) {
            goalStacks.put(goalstack.getFirst(), goalstack);
        }

        for(List<Character> stack : this.stacks){
            if(!goalStacks.containsKey(stack.getFirst())){
                for(Character c : stack){
                    if(goalStacks.containsKey(c)){
                        h += this.containerCosts.get(c);
                    }
                    else{
                        for(List<Character> goalstack : goalStacks.values()){
                            if(goalstack.contains(c)){
                                h += this.containerCosts.get(c);
                                break;
                            }
                        }
                    }

                }
            }
            else{
                List<Character> goalstack = goalStacks.get(stack.getFirst());
                boolean misplacedStackFound = false;
                for(int i = 0; i < stack.size(); i++){
                    if(i >= goalstack.size() || !stack.get(i).equals(goalstack.get(i))){ //quando o current tem mais containers na stack do que o goal ou quando o char no indice do current e diferente no goal
                        misplacedStackFound = true;
                    }
                    if(misplacedStackFound){
                        if(goalstack.contains(stack.get(i))){ //Arraylist.contains() tem complexidade temporal O(n)
                            h += 2*this.containerCosts.get(stack.get(i)); //se o container se encontra na stack certa mas na posição errada (ou alguma stack abaixo está errada) irá de ser movida no mínimo 2 vezes (uma para sair da stack e outra para voltar a ser colocada na posição certa)
                        }
                        else{
                            h += this.containerCosts.get(stack.get(i)); //se o container se encontra na stack errada irá de ser movida no mínimo 1 vezes
                        }
                    }
                }
            }
        }
        return h;
    }*/



    public double getHandre(Ilayout l){
        Containers goal = (Containers)l;
        int h = 0;
        return h;
    }


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
