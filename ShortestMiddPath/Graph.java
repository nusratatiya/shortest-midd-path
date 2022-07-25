package ShortestMiddPath;

/*
* Author: Shelby Kimmel
* Creates a adjacency list object to store information about the graph of roads, and contains the main functions used to 
* run the Bellman Ford algorithm 

*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
import java.io.File;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Graph {

	// Object that contains an adjacency list of a road network and a dictionary from elements of the list to indeces from 0 to |V|-1, since the roads are labeled in the data by arbitrary indices. Because we are considering a walking application, we construct the adjacency list so that if there is an edge {u,v}, then u appears in the list of v's neighbors, and v appears in the list of u's neighbors. This means that the adjacency matrix and the reverse adjacency matrix are the same. In other words, the adjacency matrix that is already written here is a reverse adjacency matrix.
	HashMap<Integer, ArrayList<Road>> adjList;
	HashMap<Integer,Integer> nodeDict;


	public Graph(String file) throws IOException{
		// We will store the information about the road graph in an adjacency list
		// We will use a HashMap to store the Adjacency List, since each vertex in the graph has a more or less random integer name.
		// Each element of the HashMap will be an ArrayList containing all roads (edges) connected to that vertex
		adjList = new HashMap<>();
		nodeDict = null;

		// Based on https://stackoverflow.com/questions/49599194/reading-csv-file-into-an-arrayliststudent-java
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		//BufferedReader br = new BufferedReader(new FileReader("ShortestMiddPath/Data/VT_Road_Centerline.csv"));

		if ((line=br.readLine())==null){
			return;
		}
		while ((line = br.readLine())!=null) {
			String[] temp = line.split(",");
			//Assume all roads are two-way, and using ArcMiles as distance:
			this.addToAdjList(new Road(Integer.parseInt(temp[60]),Integer.parseInt(temp[61]),temp[9],Double.parseDouble(temp[31])));
			this.addToAdjList(new Road(Integer.parseInt(temp[61]),Integer.parseInt(temp[60]),temp[9],Double.parseDouble(temp[31])));
		}


		//For dynamic programming, we will have an array with indeces 0 to |V|-1, 
		// where |V| is the number of vertices. Thus we need to associate each element of adjList with a number between 0 and |V|-1
		// We will use a Dictionary (HashMap) to do this.
		nodeDict = new HashMap<>();
		int j = 0;
		for (Integer nodeName: adjList.keySet()){
			nodeDict.put(nodeName, j);
			j++;
		}
	}


	// get functions
	public HashMap<Integer, ArrayList<Road>> getAdjList(){
		return adjList;
	}
	public HashMap<Integer,Integer> getDict(){
		return nodeDict;
	}


	public synchronized void addToAdjList(Road road) {
		//Adds the Road (edge) to the appropriate list of the adjacency list. 
		//This method is used by the constructor method
		//Based on https://stackoverflow.com/questions/12134687/how-to-add-element-into-arraylist-in-hashmap 
		Integer node = road.getStart();
    	ArrayList<Road> roadList = this.getAdjList().get(node);

    	// if node is not already in adjacency list, we create a list for it
    	if(roadList == null) {
    	    roadList = new ArrayList<Road>();
    	    roadList.add(road);
   		    this.getAdjList().put(node, roadList);
  	  	} 
  	  	else {
        	// add to appropraite list if item is not already in list
        	if(!roadList.contains(road)) roadList.add(road);
    	}
    	
    }
	
	//gets the key value according to the value
	public int getKey(int value) {
		int myKey = -1;
		for (Map.Entry<Integer, Integer> entry: nodeDict.entrySet()) {
	        if (value == entry.getValue()) {
	        	myKey = entry.getKey();
	        }

		}
		
		return myKey;
	}

	//fill in the array of all shortest distances from any given startNode
    public Double[][] ShortestDistance(Integer startNode){
    	// This method should create the array storing the objective function values of subproblems used in Bellman Ford.
    	
    	
    	//INITIAL SETUP
		
    	//the total number of vertices
    	int n = adjList.size();
		Double[][] dpArray=new Double[n][n];
		
		//set every value to infinity
		for (int row = 0; row < dpArray.length; row++) {
			for (int col = 0; col < dpArray[row].length; col++) {
				dpArray[row][col] = Double.POSITIVE_INFINITY;
			}
		}
		
		//find road associated with startnode, then from nodeDict, get the vertex of that one
		//find vertex associated with start road
		int startVertex = nodeDict.get(startNode);
		
		//base case where A[s,0] = 0
		dpArray[startVertex][0] = 0.0;
		
		//FILL IN THE ARRAY
		for (int i = 1; i < n; i++) {
			//for v in Vertex list (retrieved through adjacency list)
			for (Map.Entry<Integer, ArrayList<Road>> vertexList: adjList.entrySet()) {
				//store v
				//get the current key in the adjList and then use that to get the vertex numnber in the nodeDict
				int v = nodeDict.get(vertexList.getKey());
				
				//we need to initialize the value before finding a better one
				dpArray[v][i] = dpArray[v][i-1];
				//go through each node going to v
				for (Road roadU: vertexList.getValue()) {
					// first gets the endNode associated with road object
					//then gets the vertex associated with it in nodeDict
					int u =  nodeDict.get(roadU.getEnd()); 
					Double weight = roadU.getMiles(); //w[u,v] so the weight of the edge between u and v					
					//if A[u,i-1] + w[u,v] < A[v,i]
					double option = dpArray[u][i -1] + weight;
					if (option < dpArray[v][i]) {
						//A[v,i] <-- A[u,i-1] + w[u,v]
						dpArray[v][i] = option;
					}
				}
			}
		}
		
		return dpArray;
    }
    

    
    
    //backtracks through the dpArray and gets the most optimal path from 
    //the startNode (in shortestDistance Method) to the endNode
    public ArrayList<Road> ShortestPath(Integer endNode, Double[][] dpArray){
		// This method should work backwards through the array you created in ShortestDistance and output the 
		// sequence of streets you should take to get from your starting point to your ending point.
    	
    	int endVertex = nodeDict.get(endNode);
    	int n = adjList.size();
    	double infinity = Double.POSITIVE_INFINITY;
    	
    	// will use i and v to track out position in A as we backtrack
    	int i = n -1;
    	int v = endVertex;  //v <-- t
    	ArrayList<Road> path = new ArrayList<Road>(); // P <-- empty set
    	
    	// if no path exists between startNode and endNode
    	if (dpArray[endVertex][i] == infinity){
    		System.out.println("No Path");
    	}
    	    	
    	// while we have more than 0 edges
    	while (i > 0) {
    		// first check if the optimal path is less than i edges. if it is not, then
    		// we find what the ith edge is
    		if (dpArray[v][i] != dpArray[v][i-1]) {
    			
				ArrayList<Road> myRoadList = adjList.get(getKey(v));
				
				//iterate through the list of verticies going to v
				for (Road adjacentRoad: myRoadList) {
					//get the endNode with the road object
					//then get the vertex associated with it in the nodeDict
					int u = nodeDict.get(adjacentRoad.getEnd()); // the vertex we visit before v
					Double weight = adjacentRoad.getMiles(); //w[u,v] so the weight of the edge between u and v
					if (dpArray[v][i] == dpArray[u][i -1] + weight) {
						path.add(0,adjacentRoad); // add this road to the beginning of the path
						v = u; // update the vertex
						break; 
						//we can break out of loop because we've found the ith edge and don't
						//need to check any further edges
					}
				}
    		}
    		i--;
    	}
    	//prints out the path 
        for (Road road: path) {
        	System.out.println(road.getName());
        }
    	return path;
    	
	}
    
}