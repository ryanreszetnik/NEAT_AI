package application;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import application.Node.TYPE;

public class ShowGenome {
	static BufferedImage output = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
	static Graphics2D gr = (Graphics2D) output.getGraphics();

	public static void main(String[] args) {
//		Genome parent1 = Parent1();
		Genome parent2 = Parent3();
		show(parent2,"Recurse1");
		parent2.newRandConnection(new Random(), 20);
		show(parent2,"Recurse2");
		
//		System.out.println(parent2.inFuture(, b));
//		parent1.mutateWeights(new Random());
//		show(parent1,"Parent1");
//		show(parent2,"Parent2");
		
//		Genome child= Genome.crossOver(parent2,parent1, new Random());
//		show(child,"Child");
//		System.out.println(Genome.compatibilityDis(parent1, parent2,1,1,1));
////		parent1.addNodeMutation(new Random());
//		show(parent1,"Parent1");
//		show(parent2,"Parent2");
	}

	public static Genome Parent3(){
		Random r = new Random();
		Genome parent1 = new Genome();
		for (int i = 0; i < 2; i++) {
			parent1.addNodeGene(new Node(TYPE.INPUT, parent1.nodeCount.addToCount(),r.nextFloat()));//1,2
		}
		
		parent1.addNodeGene(new Node(TYPE.HIDDEN,  parent1.nodeCount.addToCount(),r.nextFloat()));//3
		parent1.addNodeGene(new Node(TYPE.HIDDEN,  parent1.nodeCount.addToCount(),r.nextFloat()));//4
		parent1.addNodeGene(new Node(TYPE.HIDDEN,  parent1.nodeCount.addToCount(),r.nextFloat()));//5
		parent1.addNodeGene(new Node(TYPE.OUTPUT,  parent1.nodeCount.addToCount(),r.nextFloat()));//6
		parent1.addConnectionGene(new Connection(1, 3, 1f, true, parent1.connectionCount.addToCount()));
		parent1.addConnectionGene(new Connection(2, 3, 1f, true,   parent1.connectionCount.addToCount()));
		parent1.addConnectionGene(new Connection(3, 4, 1f, true,   parent1.connectionCount.addToCount()));
		parent1.addConnectionGene(new Connection(4, 5, 1f, true,   parent1.connectionCount.addToCount()));
		parent1.addConnectionGene(new Connection(5, 6, 1f, true,   parent1.connectionCount.addToCount()));
		return parent1;
	}
	
//	public static Genome Parent1() {
//		Genome parent1 = new Genome();
//		for (int i = 0; i < 3; i++) {
//			parent1.addNodeGene(new Node(TYPE.INPUT, Innovation.getInnovationNode()));
//		}
//		parent1.addNodeGene(new Node(TYPE.OUTPUT, Innovation.getInnovationNode()));
//		parent1.addNodeGene(new Node(TYPE.HIDDEN, Innovation.getInnovationNode()));
//
//		parent1.addConnectionGene(new Connection(1, 4, 1f, true, Innovation.getInnovationConnection(1, 4)));
//		parent1.addConnectionGene(new Connection(2, 4, 1f, false, Innovation.getInnovationConnection(2, 4)));
//		parent1.addConnectionGene(new Connection(3, 4, 1f, true, Innovation.getInnovationConnection(3, 4)));
//		parent1.addConnectionGene(new Connection(2, 5, 1f, true, Innovation.getInnovationConnection(2, 5)));
//		parent1.addConnectionGene(new Connection(5, 4, 1f, true, Innovation.getInnovationConnection(5, 4)));
//		parent1.addConnectionGene(new Connection(1, 5, 1f, true, Innovation.getInnovationConnection(1, 5)));
//		return parent1;
//
//	}
//	public static Genome Parent2(){
//		Genome parent2 = new Genome();
//		for (int i = 0; i < 3; i++) {
//			parent2.addNodeGene(new Node(TYPE.INPUT, i+1));
//		}
//		parent2.addNodeGene(new Node(TYPE.OUTPUT, 4));
//		parent2.addNodeGene(new Node(TYPE.HIDDEN, 5));
//		parent2.addNodeGene(new Node(TYPE.HIDDEN, Innovation.getInnovationNode()));
//
//		parent2.addConnectionGene(new Connection(1, 4, 1f, true, Innovation.getInnovationConnection(1, 4)));
//		parent2.addConnectionGene(new Connection(2, 4, 1f, false, Innovation.getInnovationConnection(2, 4)));
//		parent2.addConnectionGene(new Connection(3, 4, 1f, true, Innovation.getInnovationConnection(3, 4)));
//		parent2.addConnectionGene(new Connection(2, 5, 1f, true, Innovation.getInnovationConnection(2, 5)));
//		parent2.addConnectionGene(new Connection(5, 4, 1f, false, Innovation.getInnovationConnection(5, 4)));
//		parent2.addConnectionGene(new Connection(5, 6, 1f, true, Innovation.getInnovationConnection(5, 6)));
//		parent2.addConnectionGene(new Connection(6, 4, 1f, true, Innovation.getInnovationConnection(6, 4)));
//		parent2.addConnectionGene(new Connection(3, 5, 1f, true, Innovation.getInnovationConnection(3, 5)));
//		parent2.addConnectionGene(new Connection(1, 6, 1f, true, Innovation.getInnovationConnection(1, 6)));
//		return parent2;
//		
//	}

	public static void show(Genome g, String name) {
		int yposInput = 20;
		int yposOutput = 20;
		gr.setComposite(AlphaComposite.Src);
		gr.setColor(new Color(0x00FFFFFF, true));
		gr.setBackground(new Color(0x00FFFFFF, true));
		gr.fillRect(0, 0, output.getWidth(), output.getHeight());
		int[][] nodePos = new int[g.getNodeGenes().size()+10][2];
		for (Node n : g.getNodeGenes().values()) {
			int xpos = 0;
			int ypos = 0;
			if (n.getType() == Node.TYPE.INPUT) {
				xpos = 20;
				ypos = yposInput;
				yposInput += 150;
			} else if (n.getType() == Node.TYPE.OUTPUT) {
				xpos = 900;
				ypos = yposOutput;
				yposOutput += 150;
			} else {
				xpos = (int) (Math.random() * 800 + 100);
				ypos = (int) (Math.random() * 200 + 100);
			}
			nodePos[n.getId() - 1][0] = xpos;
			nodePos[n.getId() - 1][1] = ypos;
			gr.setColor(Color.BLACK);
			gr.drawOval(xpos, ypos, 40, 40);
			gr.setColor(Color.BLACK);
			gr.setFont(new Font("Arial Black", Font.PLAIN, 20));
			gr.drawString(n.getId() + "", xpos + 15, ypos + 25);
		}
		double counter = 0;
		for (Connection c : g.getConnectionGenes().values()) {
			int x1 = nodePos[c.getInputNode()-1][0];
			int y1 = nodePos[c.getInputNode()-1][1];
			int x2 = nodePos[c.getOutputNode()-1][0];
			int y2 = nodePos[c.getOutputNode()-1][1];
			if (c.enabled) {
				gr.setColor(Color.BLACK);
			}else{
				gr.setColor(Color.RED);
			}
			gr.setFont(new Font("Arial Black", Font.PLAIN, 15));
			gr.drawString(Math.round(c.getWeight()*100)/100.0 + "", (x1+x2)/2, (y1+y2)/2);
			gr.drawLine(x1 + 40, y1 + 20, x2, y2 + 20);
			counter += c.getWeight();
		}
		gr.drawString("Final Sum: "+ counter, 20, 500);

		File outputfile = new File(name);
		try {
			ImageIO.write(output, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
