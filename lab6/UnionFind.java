public class UnionFind {

    // DONE - Add instance variables?
    private int[] parent;//仅是声明
    private int[] weight;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        // DONE
        parent=new int[n];//分配内存给int数组
        weight=new int[n];
        for(int i=0;i<n;i++){
            parent[i]=-1;
            weight[i]=1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        // DONE
        if(vertex>parent.length||vertex<0)
            throw new IllegalArgumentException(vertex+"is not a valid index");
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        // DONE
        validate(v1);
        return weight[findRoot(v1)];
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        // DONE
        validate(v1);
        return parent[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean isConnected(int v1, int v2) {
        // DONE
        validate(v1);
        validate(v2);
        return findRoot(v1)==findRoot(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        // DONE
        validate(v1);
        validate(v2);

        if(sizeOf(v1)>sizeOf(v2)){
            weight[findRoot(v1)]+=sizeOf(v2);
            parent[findRoot(v2)]=v1;

        }else{
            weight[findRoot(v2)]+=sizeOf(v1);
            parent[findRoot(v1)]=v2;

        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int findRoot(int vertex) {
        // DONE
        validate(vertex);
        int root=vertex;
        while(parent(root)!=-1){
            root=parent[root];
        }
        //renew parent[] array!!
        int currParent;
        while(vertex!=root){
            currParent=parent(vertex);
            parent[vertex]=root;
            vertex=currParent;
        }
        return root;
    }

}
