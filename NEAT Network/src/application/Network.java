package application;

import java.util.ArrayList;

public class Network {
	
	Genome gene;
	ArrayList<Node> inputs;
	ArrayList<Node> outputs;
	private float fitness;
	public Network(Genome g){
		gene = g;
		for(Node n :g.getNodeGenes().values()){
			if(n.getType()==Node.TYPE.INPUT){
				inputs.add(n);
			}else if(n.getType()==Node.TYPE.OUTPUT){
				outputs.add(n);
			}
		}
	}
	public Genome getGenome(){
		return gene;
	}
	public float getFitness(){
		return fitness;
	}
	public void setFitness(float fitness){
		this.fitness=fitness;
	}
	public float[] run(float[] in){
		if(in.length!=this.inputs.size()){
			return null;
		}
		for(int i = 0; i < in.length; i++){
			inputs.get(i).setOutput(in[i]);
		}
		float[] output = new float[outputs.size()];
		int counter = 0;
		for(Node out: outputs){
			output[counter] = out.run(gene.getNodeGenes(), gene.getConnectionGenes());
			counter++;
		}
		
		return output;
	}
}
