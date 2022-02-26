package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int N;
    private int numTrails;
    private double[] fraction;

    /*  perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf){
        if(N<=0||T<=0)
            throw new java.lang.IllegalArgumentException();
        this.N=N;
        fraction =new double[N];
        numTrails=T;
        for(int i=0;i<T;i++){
            int openSites=0;
            Percolation p=pf.make(N);
            if(!p.percolates()){
                int row= StdRandom.uniform(N);
                int col= StdRandom.uniform(N);
                if(!p.isOpen(row,col)){
                    p.open(row,col);
                    openSites++;
                }
            }
            fraction[i]=openSites/N*N;
        }
    }

    /* sample mean of percolation threshold*/
    public double mean(){
        return StdStats.mean(fraction);
    }

    /*   sample standard deviation of percolation threshold   */
    public double stddev(){
        return StdStats.stddev(fraction);
    }

    /*  low endpoint of 95% confidence interval    */
    public double confidenceLow(){
        return mean()-1.96*stddev()/Math.sqrt(numTrails);
    }

    /*  high endpoint of 95% confidence interval    */
    public double confidenceHigh(){
        return mean()+1.96*stddev()/Math.sqrt(numTrails);
    }

}
