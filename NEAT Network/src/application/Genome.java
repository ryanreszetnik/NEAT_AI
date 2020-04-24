package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import application.Node.TYPE;

public class Genome {

	public static final float weightRandomProbability = 0.1f;
	public static ArrayList<Integer> firstInnNums = new ArrayList<>();
	public static ArrayList<Integer> secondInnNums = new ArrayList<>();
	// public static HashMap<Integer, Connection> allConnections = new
	// HashMap<>();

	public Counter connectionCount;
	public Counter nodeCount;
	// public Innovation innovationGenerator;
	private float fitness = 0;
	private HashMap<Integer, Node> nodes;
	private HashMap<Integer, Connection> connections;

	public Genome() {
		nodes = new HashMap<Integer, Node>();
		connections = new HashMap<>();
		// innovationGenerator = new Innovation();
		connectionCount = new Counter();
		nodeCount = new Counter();
	}

	public Genome(Genome origional) {
		nodes = new HashMap<Integer, Node>();
		connections = new HashMap<>();
		for (Integer n : origional.nodes.keySet()) {
			nodes.put(n, origional.nodes.get(n).copy());
		}
		for (Integer c : origional.connections.keySet()) {
			connections.put(c, origional.connections.get(c).copy());
		}
		connectionCount = new Counter(origional.connectionCount.count);
		nodeCount = new Counter(origional.nodeCount.count);
		// System.out.println("New Genome: "+connectionCount.count + "
		// "+nodeCount.count);
		// innovationGenerator = origional.innovationGenerator.copy();
	}

	public float getFitness() {
		return fitness;
	}

	public void setFitness(float fit) {
		fitness = fit;
	}

	public HashMap<Integer, Connection> getConnectionGenes() {
		return connections;
	}

	public HashMap<Integer, Node> getNodeGenes() {
		return nodes;
	}

	public void addNodeGene(Node gene) {
		nodes.put(gene.getId(), gene);
	}

	public void addConnectionGene(Connection gene) {
		connections.put(gene.getInnovation(), gene);
		// allConnections.put(gene.getInnovation(), gene);
	}

	public void mutateWeights(Random r) {
		for (Connection c : connections.values()) {
			if (r.nextFloat() < weightRandomProbability) {
				c.setWeight(r.nextFloat() * 4f - 2f);
			} else {
				c.setWeight(c.getWeight() * (r.nextFloat() * 4f - 2f));
			}
		}

	}

	public void newRandConnection(Random r, int maxAttempts) {

		for (int i = 0; i < maxAttempts; i++) {
			int temp = r.nextInt(nodes.size() - 1) + 1;
			int temp2 = r.nextInt(nodes.size() - 1) + 1;
			Node a = nodes.get(temp);//temp
			Node b = nodes.get(temp2);//temp2
			boolean bad = false;
			if (a.equals(b)) {
				bad = true;
			}
			// System.out.println(a+ " connect to " +b +" from " + temp + " and
			// "+ temp2 +" within "+nodes.size() );
			float weight = r.nextFloat() * 2f - 1f;
			// a --> b

			boolean flipped = false;
			if (a.getType() == Node.TYPE.OUTPUT && b.getType() == Node.TYPE.INPUT) {
				flipped = true;
			} else if (a.getType() == Node.TYPE.HIDDEN && b.getType() == Node.TYPE.INPUT) {
				flipped = true;
			} else if (a.getType() == Node.TYPE.OUTPUT && b.getType() == Node.TYPE.HIDDEN) {
				flipped = true;
			} else if (a.getType() == Node.TYPE.INPUT && b.getType() == Node.TYPE.INPUT) {
				// bad
				bad = true;
			} else if (a.getType() == Node.TYPE.OUTPUT && b.getType() == Node.TYPE.OUTPUT) {
				// bad
				bad = true;
			} else if (a.getType() == Node.TYPE.HIDDEN && b.getType() == Node.TYPE.HIDDEN) {
				// possibly needs to be reversed if a is in future of b
				if (inFuture(a,b)) {
//					System.out.println("Flipped");
					flipped = true;
				}
			}

			if (ConnectionExists(a.getId(), b.getId(), connections) == -1 && !bad) {
				int inn = connectionCount.addToCount();
				// add connection
				Connection c = new Connection(flipped ? b.getId() : a.getId(), flipped ? a.getId() : b.getId(), weight,
						true, inn);
				connections.put(inn, c);
				// allConnections.put(inn, c);
				return;
			}
		}
		// System.out.println("Could not make new connection");

	}

	static ArrayList<Integer> future = new ArrayList<>();
	static HashMap<Integer, Boolean> tested = new HashMap<>();

	public boolean inFuture(Node lookingFor, Node b) {
		if (lookingFor.equals(b)) {
			return true;
		} else {
			for (Connection c : connections.values()) {
				if (c.getInputNode() == b.getId()) {
					if (tested.get(c.getOutputNode()) == null) {
						tested.put(c.getOutputNode(), true);
						if (inFuture(lookingFor, nodes.get(c.getOutputNode()))) {
							return true;
						}
					}
				}
			}

		}
		return false;
	}

	public int ConnectionExists(int nodeA, int nodeB, HashMap<Integer, Connection> connect) {
		for (Connection conn : connect.values()) {
			if (conn.getInputNode() == nodeA && conn.getOutputNode() == nodeB) {
				return conn.getInnovation();
			} else if (conn.getInputNode() == nodeB && conn.getOutputNode() == nodeA) {
				return conn.getInnovation();
			}
		}
		return -1;
	}

	public void addNodeMutation(Random r) {
		int conn = r.nextInt(connections.size()) + 1;
		Connection c = connections.get(conn);
		int input = c.getInputNode();
		int output = c.getOutputNode();
		c.disable();
		int innov = nodeCount.addToCount();
		Node newNode = new Node(TYPE.HIDDEN, innov);
		// System.out.println("Go to" + innov + "size: " + nodes.size() + " id1:
		// " + input + " id2:" + newNode.getId()
		// + " id3: " + output);
		Connection in = new Connection(input, newNode.getId(), 1, true, connectionCount.addToCount());
		Connection out = new Connection(newNode.getId(), output, c.getWeight(), true, connectionCount.addToCount());
		connections.put(in.getInnovation(), in);
		connections.put(out.getInnovation(), out);
		nodes.put(innov, newNode);
		// System.out.println(connections.size());
		// allConnections.put(in.getInnovation(), in);
		// allConnections.put(out.getInnovation(), out);
	}

	public int nodeExists(int input, int output, HashMap<Integer, Connection> connect) {
		for (Connection conn : connect.values()) {
			if (conn.getInputNode() == input) {
				int out = conn.getOutputNode();
				for (Connection conn2 : connect.values()) {
					if (conn2.getInputNode() == out && conn2.getOutputNode() == output) {
						// node already exists
						return out;
					}
				}
			}
		}
		return -1;
	}

	public static Genome crossOver(Genome morefit, Genome lessFit, Random r) {
		Genome child = new Genome(morefit);

		// for (Node n : morefit.nodes.values()) {
		// child.addNodeGene(n.copy());
		// }
		child.connections.clear();
		child.connectionCount.count = 0;

		for (Connection cfit : morefit.connections.values()) {
			if (lessFit.getConnectionGenes().containsKey(cfit.getInnovation())) {// genes
																					// match
																					// history
				child.connectionCount.addToCount();
				if (r.nextBoolean()) {
					child.addConnectionGene(cfit.copy());
				} else {
					child.addConnectionGene(lessFit.getConnectionGenes().get(cfit.getInnovation()).copy());
				}
			} else {
				child.addConnectionGene(cfit.copy());
			}
		}
		return child;
	}

	public static int numExcessGenes(Genome g1, Genome g2) {
		int excess = 0;
		ArrayList<Integer> nodeInn1 = new ArrayList<>(g1.getNodeGenes().keySet());
		ArrayList<Integer> nodeInn2 = new ArrayList<>(g2.getNodeGenes().keySet());
		int maxInn1 = Collections.max(nodeInn1);
		int maxInn2 = Collections.max(nodeInn2);
		int maxInnNum = Math.max(maxInn1, maxInn2);
		for (int i = 0; i <= maxInnNum; i++) {
			if (!nodeInn1.contains(i) && maxInn1 < i && nodeInn2.contains(i)) {
				excess++;
			} else if (!nodeInn2.contains(i) && maxInn2 < i && nodeInn1.contains(i)) {
				excess++;
			}
		}
		ArrayList<Integer> connInn1 = new ArrayList<>(g1.getConnectionGenes().keySet());
		ArrayList<Integer> connInn2 = new ArrayList<>(g2.getConnectionGenes().keySet());
		int maxConnInn1 = Collections.max(connInn1);
		int maxConnInn2 = Collections.max(connInn2);
		int maxConnInnNum = Math.max(maxInn1, maxInn2);
		for (int i = 0; i <= maxConnInnNum; i++) {
			if (!connInn1.contains(i) && maxConnInn1 < i && connInn2.contains(i)) {
				excess++;
			} else if (!connInn2.contains(i) && maxConnInn2 < i && connInn1.contains(i)) {
				excess++;
			}
		}
		return excess;
	}

	public static float compatibilityDis(Genome g1, Genome g2, float c1, float c2, float c3) {
		int disjoint = 0;
		int excess = 0;
		int matching = 0;
		float weightDiff = 0;
		firstInnNums.clear();
		secondInnNums.clear();
		firstInnNums.addAll(g1.getNodeGenes().keySet());
		secondInnNums.addAll(g2.getNodeGenes().keySet());
		int maxInn1 = Collections.max(firstInnNums);
		int maxInn2 = Collections.max(secondInnNums);
		int maxInnNum = Math.max(maxInn1, maxInn2);
		int numOfGenes = Math.max(firstInnNums.size(), secondInnNums.size());

		for (int i = 0; i <= maxInnNum; i++) {
			if (!firstInnNums.contains(i) && maxInn1 > i && secondInnNums.contains(i)) {
				disjoint++;
			} else if (!secondInnNums.contains(i) && maxInn2 > i && firstInnNums.contains(i)) {
				disjoint++;
			} else if (!firstInnNums.contains(i) && maxInn1 < i && secondInnNums.contains(i)) {
				excess++;
			} else if (!secondInnNums.contains(i) && maxInn2 < i && firstInnNums.contains(i)) {
				excess++;
			}
		}
		firstInnNums.clear();
		secondInnNums.clear();
		firstInnNums.addAll(g1.getConnectionGenes().keySet());
		secondInnNums.addAll(g2.getConnectionGenes().keySet());
		maxInn1 = Collections.max(firstInnNums);
		maxInn2 = Collections.max(secondInnNums);
		maxInnNum = Math.max(maxInn1, maxInn2);
		for (int i = 0; i <= maxInnNum; i++) {
			if (firstInnNums.contains(i) && secondInnNums.contains(i)) {
				matching++;
				weightDiff += Math
						.abs(g1.getConnectionGenes().get(i).getWeight() - g2.getConnectionGenes().get(i).getWeight());
				// System.out.println("Checking: " +
				// g1.getConnectionGenes().get(i).getWeight() +" vs "+
				// g2.getConnectionGenes().get(i).getWeight() );
			} else if (!firstInnNums.contains(i) && maxInn1 > i && secondInnNums.contains(i)) {
				disjoint++;
			} else if (!secondInnNums.contains(i) && maxInn2 > i && firstInnNums.contains(i)) {
				disjoint++;
			} else if (!firstInnNums.contains(i) && maxInn1 < i && secondInnNums.contains(i)) {
				excess++;
			} else if (!secondInnNums.contains(i) && maxInn2 < i && firstInnNums.contains(i)) {
				excess++;
			}
		}
		float temp = weightDiff;
		weightDiff /= matching;
		double output = excess * c1 / numOfGenes + disjoint * c2 / numOfGenes + c3 * weightDiff;
		if (output > 2) {
			// System.out.println("excess: " + excess + " dis: " + disjoint + "
			// weightDiff: " + weightDiff + " matching: " +matching + "before "
			// + temp);
		}
		return excess * c1 / numOfGenes + disjoint * c2 / numOfGenes + c3 * weightDiff;
	}

}
