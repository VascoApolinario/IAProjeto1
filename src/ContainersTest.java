import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class ContainersTest {

    @Test
    public void testConstructor1() {
        Containers c1 = new Containers("C1 A1B1");
        Containers c2 = new Containers("A37B12 C2 D4");
        Containers c3 = new Containers("A1");
        Containers c4 = new Containers("F2A1 B1C1 E2 D1");

        Assert.assertEquals("[A, B]\n[C]\n", c1.toString());
        assertEquals("{A=1, B=1, C=1}",c1.getContainerCosts().toString());
        assertEquals( "[A, B]\n[C]\n[D]\n",c2.toString());
        assertEquals( "{A=37, B=12, C=2, D=4}",c2.getContainerCosts().toString());
        assertEquals( "[A]\n",c3.toString());
        assertEquals( "{A=1}",c3.getContainerCosts().toString());
        Assert.assertEquals("[B, C]\n[D]\n[E]\n[F, A]\n", c4.toString());
        assertEquals( "{A=1, B=1, C=1, D=1, E=2, F=2}",c4.getContainerCosts().toString());
    }

    @Test
    public void testConstructor2() {
        Containers c1 = new Containers("A B C");
        Containers c2 = new Containers("ABCD FG H");
        Containers c3 = new Containers("ABCDEFG");

        assertEquals("[A]\n[B]\n[C]\n",c1.toString());
        assertTrue(c1.getContainerCosts().isEmpty());
        assertEquals("[A, B, C, D]\n[F, G]\n[H]\n",c2.toString());
        assertTrue(c2.getContainerCosts().isEmpty());
        assertEquals("[A, B, C, D, E, F, G]\n",c3.toString());
        assertTrue(c3.getContainerCosts().isEmpty());
    }

    @Test
    public  void testToString1(){
        Containers c1 = new Containers("A37B12 C2 D4");
        Assert.assertEquals("[A, B]\n[C]\n[D]\n",c1.toString());
        Containers c2 = new Containers("F2A1 B1C1 E2 D1");
        Assert.assertEquals("[B, C]\n[D]\n[E]\n[F, A]\n",c2.toString());
    }


    @Test
    void children() {
        Containers c1 = new Containers("A1B1 C1");
        List<Ilayout> c1Children = c1.children();
        Containers child1 = new Containers("A B C");
        Containers child2 = new Containers("A CB");
        Containers child3 = new Containers("ABC");
        for (Ilayout c : c1Children) {
            assertTrue(c.isGoal(child1) || c.isGoal(child2) || c.isGoal(child3));
        }


    }

    @Test
    void isGoal() {
        Containers c1 = new Containers("A37B12 C2 D4");
        Containers c2 = new Containers("D4 C2 A37B12");
        Containers c3 = new Containers("AB C D");
        Containers c4 = new Containers("BA D C");
        Containers c5 = new Containers("A B C D");
        assertTrue(c1.isGoal(c2));
        assertTrue(c1.isGoal(c3));
        assertTrue(c2.isGoal(c3));
        assertFalse(c1.isGoal(c4));
        assertFalse(c1.isGoal(c5));
    }

    @Test
    void getG() {
        Containers c1 = new Containers("A1B1");
        List<Ilayout> c1Children = c1.children();
        for (Ilayout child : c1Children) {
            assertEquals(1,child.getG());
        }

        List<Ilayout> grandChildren = c1Children.getFirst().children();
        for (Ilayout child : grandChildren) {
            assertEquals(1,child.getG());
        }
    }

    @Test
    void getH(){
        Containers c1 = new Containers("A1B1D1E1 C1F1G1 H1 I1");
        Containers goal1 = new Containers("A1B1H1 C1F1G1 I1E1D1");
        assertEquals(4,c1.getH(goal1));
        Containers c2 = new Containers("A37B12 C2 D4");
        Containers goal2 = new Containers("D4 C2 A37B12");
        assertEquals(0,c2.getH(goal2));
        Containers c3 = new Containers("AB C D");
        Containers goal3 = new Containers("BA D C");
        assertEquals(2,c3.getH(goal3));
    }
}