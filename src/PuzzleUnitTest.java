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
        List<Ilayout> BChildren = b.children();
        for (Ilayout child : BChildren) {
            Assert.assertEquals(child.toString(), "12");
            Assert.assertEquals(child.toString(), "");
        }
    }


}