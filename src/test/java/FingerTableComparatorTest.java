import org.junit.Before;
import org.junit.Test;

public class FingerTableComparatorTest{
    FingerTableComparator comp1;
    FingerTableComparator comp2;

    @Before
    public void setUp(){
        comp1 = new FingerTableComparator("0000");
        comp2 = new FingerTableComparator("a000");
    }
    @Test
    public void compare() {

        assert (comp1.compare("eeee", "0000")== -1);
        assert (comp1.compare("1000", "2000")== -1);
        assert (comp1.compare("1000", "1000") == 0);
        assert (comp1.compare("2000", "1000") == 1);
        assert (comp1.compare("0000", "0000") == 0);
        assert (comp2.compare("1000", "e000")== 1);
        assert (comp2.compare("e000", "1000") == -1);
        assert (comp2.compare("1000", "2000")== -1);
        assert (comp2.compare("a001", "a002")== -1);
    }
}