import org.junit.Test;

public class NodeComparatorTest {
    NodeComparator comp;
    NodeComparator comp2;

    @Test
    public void compare() {
        comp = new NodeComparator("0000");
        comp2 = new NodeComparator("a000");

        assert (comp.compare("0000", "0001")== -1);
        assert (comp.compare("1000", "2000")== -1);
        assert (comp.compare("1000", "1000") == 0);
        assert (comp.compare("2000", "1000") == 1);
        assert (comp.compare("0000", "0000") == 0);
        assert (comp2.compare("1000", "e000")== 1);
        assert (comp2.compare("e000", "1000") == -1);
        assert (comp2.compare("1000", "2000")== -1);
        assert (comp2.compare("a001", "a002")== -1);
        assert (comp2.compare("9999", "a000")== 1);
    }
}