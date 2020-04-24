package application;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public abstract class Evaluator {

	private float c1;
	private float c2;
	private float c3;
	private float DM;
	private float mutationRate;
	private float addConnectionRate;
	private float addNodeRate;
	private int popSize;

	private Genome fittestGenome;
	public float highestIndividual;
	private float highestFitness;

	public static final int maxConnectionAttempts = 30;

	private Random r = new Random();

	ArrayList<Network> networks = new ArrayList<>();
	ArrayList<Genome> genomes;
	ArrayList<Genome> nextGenomes;
	ArrayList<Species> species;
	HashMap<Genome, Species> speciesMap = new HashMap<>();

	public Evaluator(Genome startingGenome, int popSize, float c1, float c2, float c3, float DM, float mutationRate,
			float addConnectionRate, float addNodeRate) {
		genomes = new ArrayList<>();
		nextGenomes = new ArrayList<>();
		species = new ArrayList<>();
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.DM = DM;
		this.mutationRate = mutationRate;
		this.addConnectionRate = addConnectionRate;
		this.addNodeRate = addNodeRate;
		this.popSize = popSize;

		for (int i = 0; i < popSize; i++) {
			genomes.add(new Genome(startingGenome));
		}

	}

	public void evaluate() {
		// reset needed things
		for (Species s : species) {
			s.reset(r);
		}
		nextGenomes.clear();
		speciesMap.clear();
		highestIndividual = 0;
		networks.clear();
		int newCounter = 0;
		// put genomes in species
		for (Genome g : genomes) {
			networks.add(new Network(g));
			boolean putInSpecies = false;
			for (Species s : species) {
				if (Genome.compatibilityDis(g, s.mascot, c1, c2, c3) < DM) {
					// System.out.println(Genome.compatibilityDis(g, s.mascot,
					// c1, c2, c3));
					s.addMember(g);
					speciesMap.put(g, s);
					putInSpecies = true;
					break;
				}
			}
			if (!putInSpecies) {
				Species s = new Species(g);
				// System.out.println("New Species");
				species.add(s);
				newCounter++;
				speciesMap.put(g, s);
			}
		}
		// remove empty species
		int counter = 0;
//		ArrayList<Integer> removing = new ArrayList<>();
		for (int i = species.size() - 1; i >= 0; i--) {
			if (species.get(i).members.size() == 0) {
				species.remove(i);

				counter++;
			}
//			removing.add(species.get(i).members.size());

		}
		// System.out.println("added: " + newCounter + " removed: "+counter +" "
		// + Arrays.toString(removing.toArray()));

		
		//run round
		runRound(networks);
		
		
		// eval genomes and set fitnesses
		for (Network n : networks) {
			Genome g =n.getGenome();
			float fit = evalGenomeInNetwork(n);
			float adjustedFit = fit / speciesMap.get(g).members.size();
			g.setFitness(adjustedFit);
			Species s = speciesMap.get(g);
			s.addToAdjustedFit(adjustedFit);
			if (adjustedFit > highestFitness) {
				// System.out.println(adjustedFit);
				highestFitness = adjustedFit;
				fittestGenome = g;
			}
			if (fit > highestIndividual) {
				highestIndividual = fit;
			}

		}

		// put best genomes from each species into next gen (not sure if this is actually good)

		for (Species s : species) {
			nextGenomes.add(s.bestGenome());
		}

		// Breed
		// System.out.println(species.size());
		while (nextGenomes.size() < popSize) {
			Species s = Species.weightedRandomSpecies(r, species);

			Genome a = s.weightedRandomGenome();
			Genome b = s.weightedRandomGenome();
			Genome child;
			if (a.getFitness() >= b.getFitness()) {
				child = Genome.crossOver(a, b, r);
			} else {
				child = Genome.crossOver(b, a, r);
			}
			if (r.nextFloat() < mutationRate) {
				child.mutateWeights(r);
			}
			if (r.nextFloat() < addConnectionRate) {
				child.newRandConnection(r, maxConnectionAttempts);
//				System.out.println("Adding Connection");
			}
			if (r.nextFloat() < addNodeRate) {

				child.addNodeMutation(r);
				// System.out.println("After" + child.getNodeGenes().size());
			}

			nextGenomes.add(child);
		}
		genomes = nextGenomes;
		nextGenomes = new ArrayList<>();
	}

	public Genome getFittestGenome() {
		return fittestGenome;
	}

	abstract float evalGenomeInNetwork(Network n);
	
	abstract void runRound(ArrayList<Network> nets);

}
