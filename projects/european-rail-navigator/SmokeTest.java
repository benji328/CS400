import java.util.List;

/** Lightweight dependency-free checks for the custom graph and hash table. */
public class SmokeTest {
    public static void main(String[] args) {
        testHashTable();
        testDijkstra();
        System.out.println("Smoke tests passed.");
    }

    private static void testHashTable() {
        HashTableMap<String, Integer> map = new HashTableMap<>(4);
        map.put("alpha", 1);
        map.put("beta", 2);
        map.put("gamma", 3); // triggers resize at the load-factor threshold
        require(map.getSize() == 3, "hash table size");
        require(map.get("beta") == 2, "hash table lookup");
        require(map.getCapacity() >= 8, "hash table resize");
    }

    private static void testDijkstra() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        for (String node : List.of("A", "B", "C", "D")) graph.insertNode(node);
        graph.insertEdge("A", "B", 2);
        graph.insertEdge("A", "C", 8);
        graph.insertEdge("B", "C", 1);
        graph.insertEdge("C", "D", 3);
        graph.insertEdge("B", "D", 10);

        require(graph.shortestPathData("A", "D").equals(List.of("A", "B", "C", "D")),
                "Dijkstra path");
        require(graph.shortestPathCost("A", "D") == 6.0, "Dijkstra cost");
    }

    private static void require(boolean condition, String name) {
        if (!condition) throw new AssertionError("Failed: " + name);
    }
}
