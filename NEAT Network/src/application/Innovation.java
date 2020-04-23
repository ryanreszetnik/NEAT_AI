package application;

public class Innovation {
	
	private static int nodeCounter = 0;
	private static int connectionCounter = 0;
	
	public static int getInnovationConnection(int a, int b){
		for(Connection c: Genome.allConnections.values()){
			if(c.getInputNode()==a&&c.getOutputNode()==b){
				return c.getInnovation();
			}
		}
		connectionCounter++;
		return connectionCounter;
	}
	
	public static int getInnovationNode(){
		nodeCounter++;
		return nodeCounter;
	}
	
}
