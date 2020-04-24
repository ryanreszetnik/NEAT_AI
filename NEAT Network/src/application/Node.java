package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {

	enum TYPE{
		INPUT,
		HIDDEN,
		OUTPUT;
	}
	private TYPE type;
	private int id;
	
	public Node(TYPE type, int innov){
		this.type=type;
		this.id=innov;
	}
	
	
	public TYPE getType(){
		return type;
	}
	public int getId(){
		return id;
	}
	
	
	/*
	public static ArrayList<Integer> futureNodes(HashMap<Integer,Connection> connections, int currNode){
		ArrayList<Integer> nodeVals = new ArrayList<>();
		for(Connection conn: connections.values()){
			if(conn.getInputNode()==currNode){
				nodeVals.add(conn.getOutputNode());
				nodeVals = mergeArrayLists(nodeVals, Node.futureNodes(connections, conn.getOutputNode()));
			}
		}
		return nodeVals;
	}
	*/
	public static ArrayList<Integer> mergeArrayLists(ArrayList<Integer> a, ArrayList<Integer> b){
		ArrayList<Integer> bcopy = new ArrayList<>(b);
		bcopy.removeAll(a);
		a.addAll(bcopy);
		return a;
	}
	
	public Node copy(){
		return new Node(type, id);
	}
	
}
