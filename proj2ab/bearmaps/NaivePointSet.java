package bearmaps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NaivePointSet implements PointSet{
    List<Point> myPoints;

    public NaivePointSet(List<Point> myPointSet){
        myPoints= new ArrayList<>();
        Iterator<Point> it=myPointSet.iterator();
        while(it.hasNext()){
          myPoints.add(it.next());
        }
        //this.myPoints =myPointSet;  *reference is insecure
    }

    @Override
    public Point nearest(double x, double y) {
        Point Goal=new Point(x,y);
        Point p=myPoints.get(0);
        double nearest=Point.distance(Goal,p);
        Point nearestP=myPoints.get(0);
        for(int i=1;i<myPoints.size();i++){
            if(Point.distance(Goal, myPoints.get(i))<nearest){
                nearest=Point.distance(Goal,myPoints.get(i));
                nearestP= myPoints.get(i);
            }
        }
        return nearestP;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
    }
}
