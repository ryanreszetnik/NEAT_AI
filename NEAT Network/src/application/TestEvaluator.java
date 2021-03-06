package application;

import java.util.ArrayList;
import java.util.Random;

import application.Node.TYPE;

public class TestEvaluator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Random r = new Random();
		Genome gene = new Genome();
		for (int i = 0; i < 5; i++) {
			gene.addNodeGene(new Node(TYPE.INPUT, gene.nodeCount.addToCount(),r.nextFloat()));
		}
		gene.addNodeGene(new Node(TYPE.OUTPUT,  gene.nodeCount.addToCount(),r.nextFloat()));
		gene.addNodeGene(new Node(TYPE.OUTPUT,  gene.nodeCount.addToCount(),r.nextFloat()));
//		gene.addConnectionGene(new Connection(1, 3, 0.5f, true,  gene.connectionCount.addToCount()));
//		gene.addConnectionGene(new Connection(2, 3, 0.5f, true,  gene.connectionCount.addToCount()));
//		System.out.println(gene.innovationGenerator.getInnovationNode());
		Evaluator eval = new Evaluator(gene, 100, 1f, 1f, 0.4f, 2f, 0.5f, 0.1f, 0.1f){
			@Override
			void runRound(ArrayList<Network> nets) {
				// TODO Auto-generated method stub
				//need to update fitness values for each network
			}

			@Override
			float evalGenomeInNetwork(Network n) {
				// TODO Auto-generated method stub
				return n.getFitness();
			}

			

			
		};
		for(int i = 0; i < 100; i++){
			eval.evaluate();
			System.out.print("Generation " + i);
			System.out.print("\t Highest Fitness: " + eval.getFittestGenome().getFitness());
			System.out.print("\t Amount of Species: " + eval.species.size());
			System.out.print("\t highestIndividual: "+ eval.highestIndividual);
			System.out.println();
			if(i%10==0){
				ShowGenome.show(eval.getFittestGenome(), "Best Round "+i);
			}
			
			
		}
	}


}
