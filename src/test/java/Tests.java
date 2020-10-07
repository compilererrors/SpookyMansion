import org.junit.Assert;
import org.junit.Test;

public class Tests {
    @Test
    public void test() {
        Player player = new Player(5,8, 'X');
        int x = player.getX();
        Assert.assertEquals(5, x);
    }
}
