# Shortest Midd Path
Programming assignment for CSCI0302: Algorithms and Comlpexity at Middlebury College

An implentation of the Bellman Ford's Algorithm to find the shortest route between two points. The default outputed path is a list of streets one must take to get from 75 Shannon Street (Computer Science Building) to a restaurant in Middlebury. 

## Description
This algorithm uses dynamic programming to determine the shortest path between the two points stated in Main.java. By using a reverse adjaceny list, I was able to reduce the overall time complexity. 

In order to try out the shortest route between a different set of points, go to [Vermont Geodata Portal](https://geodata.vermont.gov/datasets/1dee5cb935894f9abe1b8e7ccec1253e/explore?location=44.013415%2C-73.166902%2C15.00), and click on the road that your destination on. You next need to figure out if your destination is closest to the start or end id. The problem is that you don't know which end of the road is the start and which is the end, so go to a neighboring road and see which start id or end id it has in common.

## Executing the Program
1. To run from the command line, open a terminal in the directory where ShortestMiddPath is located
2. To compile:
  ```
  javac ShortestMiddPath/*.java
  ```
3. To run:
  ```
  java -cp . ShortestMiddPath.Main
  ```
  *(-cp allows you to tell java where to look for the compiled code, and . tells it to look in the       current directory)*
  
## Authors
Nusrat Atiya
