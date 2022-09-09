import java.util.*;

//Note:
// Initially only the distance to immediate neighbours is only know.
// We run Bellman For algorithm on each of the node with that node
// as the source to determine the shortest distance to other nodes.

class RoutingEntry {
	public int outgoinig;
	public int distance;

	public RoutingEntry(int outgoing, int distance) {
		this.outgoinig = outgoing;
		this.distance = distance;
	}
}

public class DVR {

	private static void print(Object o) {
		System.out.print(o);
	}

	// For undirected graphs
	private static void bellmanFord(int node_count, int source, int graph[][], RoutingEntry routingTable[][]) {
		int distance[] = new int[node_count];
		for (int i = 0; i < node_count; i++)
			distance[i] = Integer.MAX_VALUE / 2;
		distance[source] = 0;
		for (int k = 0; k < node_count - 1; k++)
			for (int i = 0; i < node_count; i++)
				for (int j = 0; j < node_count; j++)
					if (i != j && graph[i][j] != 0)
						if (distance[j] > distance[i] + graph[i][j]) {
							distance[j] = distance[i] + graph[i][j];
							//routingTable[i][j].outgoinig = i;
						}
		for (int i = 0; i < node_count; i++)
			routingTable[source][i].distance = distance[i];

	}

	public static void main(String args[]) {
		int v, neighbour_count;
		int neighbour, weight;
		Scanner sc = new Scanner(System.in);

		print("Enter the number of nodes:");
		v = sc.nextInt();
		int graph[][] = new int[v][v];
		RoutingEntry routingTable[][] = new RoutingEntry[v][v];
		for (int i = 0; i < v; i++)
			for (int j = 0; j < v; j++)
				routingTable[i][j] = new RoutingEntry(-1, -1);

		for (int i = 0; i < v; i++) {
			print("Enter the number of neighbours of " + i + ": ");
			neighbour_count = sc.nextInt();
			print("Enter the neighbours and weights:");
			for (int j = 0; j < neighbour_count; j++) {
				neighbour = sc.nextInt();
				weight = sc.nextInt();
				graph[i][neighbour] = weight;
				graph[neighbour][i] = weight;
				routingTable[i][neighbour].distance = weight;
				routingTable[i][neighbour].outgoinig = neighbour;
			}
		}

		for (int i = 0; i < v; i++)
			bellmanFord(v, i, graph, routingTable);

		for (int i = 0; i < v; i++) {
			print("\nVector of " + i + ":\n");
			print("Node Distance\n");
			for (int j = 0; j < v; j++)
				System.out.printf("%4d%9d\n", j, routingTable[i][j].distance);
		}

		sc.close();
	}
}