package application;

import java.util.ArrayList;
import java.util.List;

public class Connection {
	
	
	private int inNode;
	private int outNode;
	private float weight;
	boolean enabled = true;
	private int innovation;
	
	public Connection(int inNode, int outNode, float weight, boolean enabled, int innov){
		this.inNode=inNode;
		this.outNode=outNode;
		this.weight=weight;
		this.enabled=enabled;
		this.innovation=innov;
	}
	public int getInputNode(){
		return inNode;
	}
	public int getOutputNode(){
		return outNode;
	}
	
	public void disable(){
		enabled = false;
	}
	public void reEnable(){
		enabled = true;
	}
	public int getInnovation(){
		return innovation;
	}
	public float getWeight(){
		return weight;
	}
	public Connection copy(){
		return new Connection(inNode,outNode,weight,enabled,innovation);
	}
	public void setWeight(float weight){
		this.weight = weight;
	}
}
