import java.util.List;

public interface Ilayout {

    /**
     * @return the children of the receiver
     */
    List<Ilayout> children();

    /**
     * @param l
     * @return true if the receiver equals the argument l;
     *          return false otherwise.
     */
    boolean isGoal(Ilayout l);

    /**
     *
     * @return the cost for moving from the input config to the receiver
     */
    double getG();

    /**
     *
     * @return the estimated movement cost to reach the goal
     */
    double getH(Ilayout l);




}
