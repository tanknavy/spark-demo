package com.tanknavy.idea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Author: Alex Cheng 9/26/2020 2:22 PM
 */
public class DenseGraph {
    private int n, m;
    private boolean directed;
    private boolean[][] g;
    //private Vector<Boolean>[] g;

    public DenseGraph(int n, boolean directed) {
        assert n >=0;
        this.n = n;
        this.m = 0;
        this.directed = directed;
        //this.g = (Vector<Boolean>[]) new Vector[n];
        this.g = new boolean[n][n];
//        for (int i = 0; i < n; i++) {
//            g[i] = new boolean[n];
//            for(int j=0;j<n;j++){
//                g[i][j] = false;
//            }
//        }
    }

    public int V() {return n;}
    public int E() {return m;}


    public void showGraph() {
        for(int i=0;i<n;i++){
            System.out.println(Arrays.asList(g[i]));
        }
    }

    class AdjIterator{
        private DenseGraph G;
        int v;
        int index;

        public AdjIterator(DenseGraph G, int v) {
            this.G = G;
            this.v = v;
            this.index = -1;
        }

        public int begin(){
            index = -1;
            return next();
        }

        public int next(){
            for(index +=1;index <G.V();index++){
               if(G.g[v][index])
                   return index;
        }
            return -1;
        }

        public boolean end(){
            return index >= G.V();
        }
    }

    public static void main(String[] args) {
        DenseGraph denseGraph = new DenseGraph(5, false);
        denseGraph.showGraph();
    }
}
