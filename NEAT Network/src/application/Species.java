package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Species {
	Genome mascot;
	public List<Genome> members;
	public float sumAdjustedFit = 0f;
	
	public Species(Genome mascot){
		this.mascot =mascot;
		members = new ArrayList<>();
		members.add(mascot);
	}
	
	public void addMember(Genome g){
		members.add(g);
	}
	
	public void addToAdjustedFit(float fit){
		sumAdjustedFit+=fit;
	}
	public void reset(Random r){
//		mascot = weightedRandomGenome();
		int temp = r.nextInt(members.size());
		mascot = members.get(temp);
		members.clear();
		sumAdjustedFit = 0;
	}
	
	public Genome bestGenome(){
		float maxScore = mascot.getFitness();
		Genome newMascot = mascot;
		for(Genome g: members){
			if(g.getFitness()>maxScore){
				maxScore = g.getFitness();
				newMascot = g;
			}
		}
		return newMascot;
	}
	
	public Genome weightedRandomGenome(){
		double totalFit =0;
		for(Genome g: members){
			totalFit+=g.getFitness();
		}
		
		int rand = (int)(Math.random()*totalFit);
		float counter = 0;
		for(Genome g: members){
			counter+= g.getFitness();
			if(counter>= rand){
				return g;
			}
		}
		return null;
	}
	public static Species weightedRandomSpecies(Random r, ArrayList<Species> species){
		float total = 0;
		for(Species s: species){
			total+= s.sumAdjustedFit;
		}
		
		double rand = Math.random()*total;
		float counter = 0;
		for(Species s: species){
			counter+= s.sumAdjustedFit;
			if(counter>= rand){
				return s;
			}
		}
		return null;
	}
	
}
