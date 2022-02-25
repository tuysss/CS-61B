package bearmaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    private Random r=new Random(500);


    private static KDTree bulitLetureTree(){
        Point A = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point B = new Point(4, 2);
        Point C = new Point(4, 5);
        Point D = new Point(3, 3);
        Point E = new Point(1, 5);
        Point F = new Point(4, 4);

        KDTree kd=new KDTree(List.of(A,B,C,D,E,F));
        return kd;
    }

    @Test
    public void testNearestDEmoSlides(){
        KDTree kd=bulitLetureTree();
        Point actual=kd.nearest(0,7);
        Point expected=new Point(1,5);

        assertEquals(expected,actual);
    }

    private Point randomPoints(){
        double x=r.nextDouble()*100;
        double y=r.nextDouble()*100;
        return new Point(x,y);
    }

    private List<Point> randomPoints(int N){
        List<Point> points=new ArrayList<>();
        for(int i=0;i<N;i++){
            points.add(randomPoints());
        }
        return points;
    }

    private void testWithNpointsMquries(int n,int m) {
        List<Point> points = randomPoints(n);
        NaivePointSet nps = new NaivePointSet(points);
        KDTree kd = new KDTree(points);

        List<Point> queries = randomPoints(m);
        for (Point p : queries) {
            Point expected = nps.nearest(p.getX(), p.getY());
            Point actual = kd.nearest(p.getX(), p.getY());

            assertEquals(expected, actual);
        }
    }

    @Test
    public void testWith1000Points200Queries(){
        int points=1000;
        int queries=200;
        testWithNpointsMquries(points,queries);
    }


    @Test
    public void testWith10000Points2000Queries(){
        int points=10000;
        int queries=2000;
        testWithNpointsMquries(points,queries);
    }


}
