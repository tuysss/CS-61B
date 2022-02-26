import org.junit.*;
import static org.junit.Assert.*;

public class TestUnionFind {
    UnionFind uf=new UnionFind(6);

    @Test
    public void test1(){
        uf.union(1,3);
        uf.union(3,5);
        uf.union(0,2);
        assertTrue(uf.isConnected(1,5));

    }
    @Test
    public void test2(){
        uf.union(1,3);
        uf.union(3,5);
        uf.union(0,2);

        uf.union(2,5);
        assertTrue(uf.isConnected(2,3));

        assertEquals(3, uf.parent(2));
    }

}
