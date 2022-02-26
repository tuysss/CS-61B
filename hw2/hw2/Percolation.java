/****************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:  java Percolation
 *  Dependencies: algs4.jar stdlib.jar
 *
 *  Percolation class for Monte Carlo simulation.
 *
 ****************************************************************************/
package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation{

    private boolean[][] grid;
    private int N;
    private int visualTop;
    private int visualBottom;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufExcludeBottom;  //to avoid backwash
    private int openSites;
    private int[][] neighbours=new int[][]{{-1,0},{0,-1},{0,1},{1,0}};

    /* create N-by-N grid, with all sites initially blocked*/
    public Percolation(int N) {
        if(N<=0){
            throw new IllegalArgumentException();
        }
        grid=new boolean[N][N];
        this.N=N;
        visualTop=0;
        visualBottom=N*N+1;
        uf=new WeightedQuickUnionUF(N*N+2);
        ufExcludeBottom=new WeightedQuickUnionUF(N*N+1);
    }

    /* transfrom (row,col) into 1D coordinate */
    private int xyTo1D(int row,int col){
        return row*N+col;
    }

    /*open the site (row, col) if it is not open already*/
    public void open(int row, int col) {
        validate(row,col);
        if(!isOpen(row,col)){
            grid[row][col]=true;
            openSites+=1;
        }
        if(row==0){
            uf.union(xyTo1D(row,col),visualTop);
            ufExcludeBottom.union(xyTo1D(row,col),visualTop);
        }
        if(row==N-1){
            uf.union(xyTo1D(row,col),visualBottom);
        }
        for(int[] neighbour : neighbours){
            int adjacentRow=row+neighbour[0];
            int adjacentCol=col+neighbour[1];
            if(adjacentRow>=0&&adjacentRow<N&&adjacentCol>=0&&adjacentCol<N){
                if(isOpen(adjacentRow,adjacentCol)){
                    uf.union(xyTo1D(row,col),xyTo1D(adjacentRow,adjacentCol));
                    ufExcludeBottom.union(xyTo1D(row,col),xyTo1D(adjacentRow,adjacentCol));
                }
            }
        }
    }

    /*is the site (row, col) open?*/
    public boolean isOpen(int row, int col){
        return grid[row][col];
    }

    /**
     * is the site (row, col) full?
     * Spoiler: is connected to the visualTopSite?
     * */
    public boolean isFull(int row, int col)  {
        validate(row,col);
        return ufExcludeBottom.connected(visualTop,xyTo1D(row,col));
    }

    /**
     * number of open sites
     * requires: must take constant time
     * */
    public int numberOfOpenSites(){
        return openSites;
    }

    /*does the system percolate?*/
    public boolean percolates(){
        return uf.connected(visualTop,visualBottom);
    }



    private void validate(int row, int col){
        if(row<0 || row>=N || col<0 ||col>=N)
            throw new IndexOutOfBoundsException();
    }

    /* use for unit testing (not required, but keep this here for the autograder)*/
    public static void main(String[] args)  {

    }
}
