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
        Pattern pattern = Pattern.compile("([A-Z])(\\d+)"); //Classe Pattern é usada para definir um padrão. A regular expression "([A-Z])(\\d+)" é compilada em pattern.
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
