import org.junit.Assert;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class PuzzleUnitTest {

    @Test
    public void testConstructor() {
        Board b = new Board("023145678");
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter ( writer ) ;
        pw.println(" 23");
        pw.println("145");
        pw.println("678");
        Assert.assertEquals(b.toString(), writer.toString());
        pw.close();
    }

    @Test
    public void testConstructor2() {
        Board b = new Board("123485670");
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter (writer) ;
        pw.println("123");
        pw.println("485");
        pw.println("67 ");
        Assert.assertEquals(b.toString(), writer.toString());
        pw.close();
    }

    @Test
    public void testIsGoal(){
        Board goal = new Board("023145678");
        Board b1 = new Board("123485670");
        Board b2 = new Board("023145678");
        Assert.assertFalse(b1.isGoal(goal));
        Assert.assertTrue(b2.isGoal(goal));
    }

    @Test
    public void testListChildren() {
        Board b = new Board("023145678");

        Board b1 = new Board("203145678");
        Board b2 = new Board("123045678");
        List<Ilayout> BChildren = b.children();
        for (Ilayout child : BChildren) {
            Assert.assertTrue(child.isGoal(b1) || child.isGoal(b2));
        }

        Board a = new Board("123405678");
        Board a1 = new Board("123450678");
        Board a2 = new Board("123045678");
        Board a3 = new Board("103425678");
        Board a4 = new Board("123475608");

        List<Ilayout> AChildren = a.children();
        for (Ilayout child : AChildren) {
            Assert.assertTrue(child.isGoal(a1) || child.isGoal(a2) || child.isGoal(a3) || child.isGoal(a4));
        }
    }

    @Test
    public void testG() {
        Board A = new Board("123450678");
        Assert.assertTrue(A.getG() == 1);
    }

}