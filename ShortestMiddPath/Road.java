package ShortestMiddPath;

/*
* Author: Shelby Kimmel
* Creates a Road object to store information about a road
* 

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


public class Road {

	// Object that contains the startNode, endNode, street name, and length of a Middlebury road.
	int startNode;
	int endNode;
	String name;
	Double miles;


	public Road(int startN, int endN, String nm, Double mil) {
		this.startNode=startN;
		this.endNode=endN;
		this.name=nm;
		this.miles=mil;
	}

	public Double getMiles(){
		return miles;
	}

	public Integer getStart(){
		return startNode;
	}

	public Integer getEnd(){
		return endNode;
	}

	public String getName(){
		return name;
	}

	@Override
	public String toString(){
		return this.startNode+ " "+this.endNode+ " "+this.name + " "+ this.miles;
	}
}