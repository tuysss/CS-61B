package bearmaps.proj2ab;

import java.util.List;

public class KDTree implements PointSet{
    private Node root;

    public KDTree(List<Point> points){
        for(int i=0;i<points.size();i++){
            insert(points.get(i));
        }
    }

    public void insert(Point curr){
        root=insert(curr,root,0);
    }

    private Node insert(Point curr,Node x,int height){
        if(x==null) return new Node(curr,height);
        height=height+1;
        if(x.getIsUpdown()){
            if(curr.getY()<x.getY()) x.leftChild=insert(curr,x.leftChild,height);
            else                     x.rightChild=insert(curr,x.rightChild,height);
        }else{
            if(curr.getX()<x.getX()) x.leftChild=insert(curr,x.leftChild,height);
            else                     x.rightChild=insert(curr,x.rightChild,height);
        }
        return x;
        //warning:height++:++ operation won't do until return
    }



    @Override
    public Point nearest(double x, double y){
        Node nearest=nearest(root,new Point(x,y),root);
        return nearest.point;
    }

    private Node nearest(Node n,Point goal,Node best){
        Node goodSide,badSide;
        if(n==null)
            return best;
        if(n.distance(goal)<best.distance(goal))
            best=n;
        if(issmallerthan(goal,n)){
            goodSide=n.leftChild;
            badSide=n.rightChild;
        }else{
            goodSide=n.rightChild;
            badSide=n.leftChild;
        }
        best=nearest(goodSide,goal,best);
        //pruning rule: if the best of badSide still worse than goodSide, then prune it.
        if (isWorthLooking(n,goal,best))
            best=nearest(badSide,goal,best);
        return best;
    }

    private boolean isWorthLooking(Node n, Point goal, Node best){
        double distToBest= best.distance(goal);
        double distToBad;
        if(n.getIsUpdown()){
            distToBad=Point.distance(goal,new Point(goal.getX(),n.getY()));
        }else
            distToBad=Point.distance(goal,new Point(n.getX(),goal.getY()));
        return Double.compare(distToBad,distToBest)<0;
    }


    //return : if goal is smaller(lefter,downer) than n
    private boolean issmallerthan(Point goal,Node n){
        if(n.getIsUpdown()){
            if(goal.getY()<n.getY()) return true;
            else                    return false;
        }else{
            if(goal.getX()<n.getX()) return true;
            else                    return false;
        }
    }


    public class Node{
        private Point point;
        private Node leftChild;
        private Node rightChild;
        private boolean isUpDown;
        private int height;

        private Node(Point p,int height){
            //this.point=point;  reference
            point=new Point(p.getX(),p.getY());
            this.height=height;
            setIsUpdowm(height);
        }
        public double getX(){
            return point.getX();
        }
        public double getY(){
            return point.getY();
        }

        private void setIsUpdowm(int height){
            if(height%2==1) isUpDown=true;
            else isUpDown=false;
        }
        private boolean getIsUpdown(){
            return isUpDown;
        }

        public double distance(Point goal){
            return Point.distance(point,goal);
        }
    }



}
