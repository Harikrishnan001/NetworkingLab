import java.util.Scanner;

public class DVR {

	private static void print(Object o) {
		System.out.print(o);
	}

	private static void bellmanFord(int node_count, int source, int graph[][], int routingTable[][]) {
		final int infinity = Integer.MAX_VALUE / 2;
		for (int i = 0; i < node_count; i++)
			routingTable[source][i] = infinity;
		routingTable[source][source] = 0;

		for (int k = 0; k < node_count - 1; k++)
			for (int i = 0; i < node_count; i++)
				for (int j = 0; j < node_count; j++)
					if (i != j && graph[i][j] != 0)
						if (routingTable[source][j] > routingTable[source][i] + graph[i][j])
							routingTable[source][j] = routingTable[source][i] + graph[i][j];

	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, neighbour_count;
		int graph[][], routingTable[][];

		print("Enter the number of nodes:");
		n = sc.nextInt();
		graph = new int[n][n];
		routingTable = new int[n][n];

		for (int i = 0; i < n; i++) {
			print("\nEnter the number of neighbours of " + i + " :");
			neighbour_count = sc.nextInt();
			print("Enter the neigbour and weight pairs:");
			for (int j = 0; j < neighbour_count; j++) {
				int neighbour = sc.nextInt();
				int weight = sc.nextInt();
				graph[i][neighbour] = weight;
			}
		}

		for (int i = 0; i < n; i++)
			bellmanFord(n, i, graph, routingTable);

		print("Shortest Distances:\n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				print(routingTable[i][j] + " ");
			print("\n");
		}
	}
}