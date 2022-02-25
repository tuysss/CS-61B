package bearmaps.proj2c;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import java.util.*;



public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private LinkedList<Vertex> solution;
    private double timeSpent;
    private int numStatesExplored;

    Map<Vertex,Double> distTo;
    Map<Vertex,Vertex> edgeTo;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        this.solution=new LinkedList<>();
        solutionWeight=0;
        numStatesExplored=0;

        ExtrinsicMinPQ<Vertex> PQ=new ArrayHeapMinPQ<>();
        this.distTo=new HashMap<>();
        this.edgeTo=new HashMap<>(); //<to,from>

        Stopwatch sw=new Stopwatch();

        distTo.put(start,0.0);
        PQ.add(start,input.estimatedDistanceToGoal(start,end));

        while(PQ.size()>0){
            //SOLVABLE
            if(PQ.getSmallest().equals(end)){
                Vertex curVertex=PQ.getSmallest();

                solution.add(curVertex);
                while(!curVertex.equals(start)){
                    curVertex=edgeTo.get(curVertex);
                    solution.addFirst(curVertex);
                }
                outcome=SolverOutcome.SOLVED;
                solutionWeight=distTo.get(end);
                timeSpent=sw.elapsedTime();
                return;
            }

            //TIMEOUT
            if(sw.elapsedTime()>timeout){
                outcome=SolverOutcome.TIMEOUT;
                timeSpent=sw.elapsedTime();
                return;
            }

            Vertex p=PQ.removeSmallest();
            numStatesExplored++;

            for(WeightedEdge<Vertex> edge: input.neighbors(p)){
                relax(edge,PQ,input,end);
            }
        }
        //UNSOLVABLE
        timeSpent=sw.elapsedTime();
        outcome=SolverOutcome.UNSOLVABLE;
        solution.clear();
        solutionWeight=0;
    }

    private void relax(WeightedEdge<Vertex> e, ExtrinsicMinPQ<Vertex> pq,AStarGraph<Vertex> graph,Vertex end){
        Vertex p=e.from();
        Vertex q=e.to();
        double w=e.weight();
        if(!distTo.containsKey(q) || distTo.get(p)+w<distTo.get(q)){
            distTo.put(q,distTo.get(p)+w);
            edgeTo.put(q,p);

            if(pq.contains(q)) {
                pq.changePriority(q, distTo.get(q) + graph.estimatedDistanceToGoal(q, end));
            } else {
                pq.add(q, distTo.get(q) + graph.estimatedDistanceToGoal(q, end));
            }
        }
    }

    @Override
    public SolverOutcome outcome() {
        return this.outcome;
    }

    @Override
    public List<Vertex> solution() {
        return this.solution;
    }

    @Override
    public double solutionWeight() {
        return this.solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }
}