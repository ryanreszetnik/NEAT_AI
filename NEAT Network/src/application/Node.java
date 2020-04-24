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
	private float bias = 1f;
	private float biasWeight;
	private boolean isCalculated = false;
	private float output=0f;
	private float totalInputs = 0f;

	
	
	public Node(TYPE type, int innov,float biasWeight){
		this.type=type;
		this.id=innov;
		this.biasWeight=biasWeight;
	}
	public boolean isCalculated(){
		return isCalculated;
	}

	public float run(HashMap<Integer, Node> nodes, HashMap<Integer, Connection> connections){
		for(Connection c: connections.values()){
			if(c.getOutputNode()==id){
				if(!nodes.get(c.getInputNode()).isCalculated()){
					totalInputs+=nodes.get(c.getInputNode()).run(nodes, connections)*c.getWeight();
				}else{
					totalInputs+=nodes.get(c.getInputNode()).getOutput()*c.getWeight();
				}
				
			}
		}
		output+=bias*biasWeight;
		output = activation();
		isCalculated = true;
		return output;
	}
	public float getOutput(){
		return output;
	}
	public void setOutput(float out){
		this.output=out;
		isCalculated = true;
	}
	
	public float activation(){
		return (float) (1f/(1f+Math.pow(Math.E,-4.9*totalInputs)));
	}
		
	public void reset(){
		isCalculated = false;
		totalInputs = 0f;
	}
		
	public TYPE getType(){
		return type;
	}
	public int getId(){
		return id;
	}
	public void setBiasWeight(float weight){
		this.biasWeight=weight;
	}
	public float getBiasWeight(){
		return biasWeight;
	}
	
	
//	public static ArrayList<Integer> futureNodes(HashMap<Integer,Connection> connections, int currNode){
//		ArrayList<Integer> nodeVals = new ArrayList<>();
//		for(Connection conn: connections.values()){
//			if(conn.getInputNode()==currNode){
//				nodeVals.add(conn.getOutputNode());
//				nodeVals = mergeArrayLists(nodeVals, Node.futureNodes(connections, conn.getOutputNode()));
//			}
//		}
//		return nodeVals;
//	}
	
	public static ArrayList<Integer> mergeArrayLists(ArrayList<Integer> a, ArrayList<Integer> b){
		ArrayList<Integer> bcopy = new ArrayList<>(b);
		bcopy.removeAll(a);
		a.addAll(bcopy);
		return a;
	}
	
	public Node copy(){
		return new Node(type, id,biasWeight);
	}
	
}
