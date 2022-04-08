/**
 * @Author Nicholas Phan
 */

//Linear time = O(n) - where I believe n is the amount of Vertices
//DFS-based algorithm - Depth First Search in slides day 17-18
//Euler Cycle, where each edge is visited exactly once, and where all vertices have an even degree.
//To review, Edges, vertices, Degrees, cycles.
//Edges - a connection between two vertex. If we had a vertex a & it connects to vertex b, it creates 1 edge connection
//Vertex - in a sense they are nodes that have references to each other
//Degree - Number of edges that are connected with the Vertex, if A is connected to B,C,D, it has a degree of 3.
//Cycle - A path where the first and Last vertices are the same, say we have a vertex of A that connects to b, c, d, e.
//          e connects to A, there for we have a cycle.

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import java.util.ArrayList;

public class EulerFinder {
    private final boolean[] markedArray;
    private ArrayList<Integer> listOfVertices = new ArrayList<Integer>();
    private int count;

    /**
     * @param enteredGraphs - takes what graph is given to begin finding the cycle
     */

    public EulerFinder(Graph enteredGraphs){

        //In the constructor we absolutely begin with the algorithm where we find the Euler Cycle.
        markedArray = new boolean[enteredGraphs.V()]; //creates marked with the size of the entered graphs Vertices
        for (int i = 0; i < enteredGraphs.V(); i++){
            markedArray[i] = false; //initializes as not visited initially.
        }

        int beginningVertex = 0;

        depthFirstSearch(enteredGraphs, beginningVertex);
        hasCycle(enteredGraphs);
        verticesInCycle();
        System.out.println(enteredGraphs.toString());

    }

    /**
     * @return count number of vertices connected to source vertex
     */

    public int getCount(){
        return count;
    }

    /**DepthFirstSearch Methods from Sedgewick
     * @param enteredGraph - Given graph either user entry OR randomized
     * @param sourceVertex - Vertex it starts at to connect
     */

    private void depthFirstSearch(Graph enteredGraph, int sourceVertex){

        //I believe in DFS we can add which vertex we're in the order we're visiting each vertex

        count++;
        markedArray[sourceVertex] = true; //marks the current vertex we're in true, and the count incremented
        listOfVertices.add(sourceVertex); //adds the current source vertex to see where we're at
        for(int currentVertex : enteredGraph.adj(sourceVertex)){ //recursive statement to mark vertex visited.
            if(!markedArray[currentVertex]){ //if we're at the currentVertex and it wasn't marked, recursive call into DFS
                depthFirstSearch(enteredGraph, currentVertex); //with the currentVertex
            }
        }

    }

    //Required Test Methods

    /**
     * @return private arraylist that contains each vertex that visited in sequential order
     */

    public ArrayList<Integer> verticesInCycle(){ //supposed to return the vertices in the cycle, in order visited

        System.out.println("The order in which the vertices were visited in were: " + (listOfVertices.toString()));
        return listOfVertices;
    }


    /**
     *
     * @param enteredGraph - graph that we're examining if it has a cycle
     * @return resultant of whether or not it's eulerian or not, or even contains a cycle
     */

    public boolean hasCycle(Graph enteredGraph){

        for (int i = 0; i < enteredGraph.V(); i++) {
            if (!markedArray[i] && enteredGraph.degree(i) > 0) {//if the entire array is not visited and the degree is greater than 0
                System.out.println("The Graph does not contain a cycle");
                return false; //it wouldn't be a cycle, since if everything wasn't visited but there are Degrees, then it's connected but not cycled
            }
        }

        // To determine if cycle is an Euler cycle it requires two things, it must visit each edge exactly once, and has an even degree

        if(getCount() != enteredGraph.V()){ //checks if the count matches the amount of vertices
            //checks if the graph is all connected, and if it isn't that means we have no cycle
            System.out.println("There's no cycle as everything isn't connected");
            return false;
        }

        int oddVertices = 0;
        // if it has any vertices with an odd degree, then this will determine any eulerian cycles
        for (int i = 0; i < enteredGraph.V(); i++){
            if(enteredGraph.degree(i) %2!=0){
                oddVertices++;
            }
        }


        if(oddVertices>0){
            System.out.println("The Given graph contains a cycle, but it isn't Eulerian");
            return true;
        }

        //if it doesn't contain any oddVertices then it's an euler cycle!
        System.out.println("The given graph is a euler cycle!");
        return true;
    }

    public static void main(String[] args) {

        //we have a cycle that isn't connecting everything
        Graph noCycle = new Graph(4);
        noCycle.addEdge(0,1);
        noCycle.addEdge(1,2);


        //guaranteed cycle + euler
        Graph cycleAndEuler = new Graph(4);
        cycleAndEuler.addEdge(0,1);
        cycleAndEuler.addEdge(1,2);
        cycleAndEuler.addEdge(2,3);
        cycleAndEuler.addEdge(3,0);

        Graph oddGraph = new Graph(3);
        oddGraph.addEdge(0, 1);
        oddGraph.addEdge(1,2);

        EulerFinder noCycled = new EulerFinder(noCycle);
        EulerFinder cycleAndEulers = new EulerFinder(cycleAndEuler);
        EulerFinder oddVertex = new EulerFinder(oddGraph);
        EulerFinder randomCycleFinder = new EulerFinder(GraphGenerator.simple(5,5));
    }



}
