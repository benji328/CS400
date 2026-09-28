import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Backend implementation for P209.
 * Uses a GraphADT to provide shortest path related functionality.
 */
public class Backend implements BackendInterface {

  private GraphADT<String, Double> graph;
  private List<String> locations;

  /**
   * Creates a backend using the provided graph object.
   * @param graph graph object used by the backend
   */
  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
    this.locations = new ArrayList<>();
  }

  /**
   * Loads graph data from a dot file. Clears any previously loaded graph data first.
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
    clearExistingGraph();

    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();

        if (line.length() == 0 || line.equals("{") || line.equals("}")
            || line.startsWith("digraph")) {
          continue;
        }

        if (line.contains("->")) {
          String from = parseFirstQuoted(line);
          String to = parseSecondQuoted(line);
          double weight = parseWeight(line);

          addLocation(from);
          addLocation(to);
          graph.insertEdge(from, to, weight);
        } else if (line.contains("\"")) {
          String node = parseFirstQuoted(line);
          addLocation(node);
        }
      }
    }
  }

  /**
   * Returns a list of all locations in the graph.
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAll() {
    return new ArrayList<>(locations);
  }

  /**
   * Returns the sequence of locations along the shortest path from start to end,
   * or an empty list if no such path exists.
   * @param start the start of the path
   * @param end the end of the path
   * @return list of locations on the shortest path, or empty list if none exists
   */
  @Override
  public List<String> findLocationsOnShortestPath(String start, String end) {
    try {
      return new ArrayList<>(graph.shortestPathData(start, end));
    } catch (NoSuchElementException e) {
      return new ArrayList<>();
    }
  }

  /**
   * Returns the times in minutes between each pair of consecutive locations
   * on the shortest path from start to end, or an empty list if no path exists.
   * @param start the start of the path
   * @param end the end of the path
   * @return list of edge weights along the shortest path, or empty list if none exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String start, String end) {
    List<Double> times = new ArrayList<>();
    List<String> path = findLocationsOnShortestPath(start, end);

    if (path.isEmpty()) {
      return times;
    }

    for (int i = 0; i < path.size() - 1; i++) {
      try {
        times.add(graph.getEdge(path.get(i), path.get(i + 1)));
      } catch (NoSuchElementException e) {
        return new ArrayList<>();
      }
    }

    return times;
  }

  /**
   * Returns the shortest path from start to the furthest reachable location.
   * @param start the location from which to start search paths
   * @return the shortest path to the furthest reachable location from start
   * @throws NoSuchElementException if start does not exist, or if no other locations are reachable
   */
  @Override
  public List<String> getFurthestFromList(String start) throws NoSuchElementException {
    if (!graph.containsNode(start)) {
      throw new NoSuchElementException("Start location does not exist.");
    }

    double maxCost = -1.0;
    List<String> furthestPath = null;

    for (String location : locations) {
      if (location.equals(start)) {
        continue;
      }

      try {
        double cost = graph.shortestPathCost(start, location);

        if (cost > maxCost) {
          maxCost = cost;
          furthestPath = graph.shortestPathData(start, location);
        }
      } catch (NoSuchElementException e) {
        // Ignore unreachable locations.
      }
    }

    if (furthestPath == null || furthestPath.isEmpty()) {
      throw new NoSuchElementException("No reachable locations from start.");
    }

    return new ArrayList<>(furthestPath);
  }

  /**
   * Adds a location to both the graph and backend list if it is not already known.
   * @param location location name to add
   */
  private void addLocation(String location) {
    if (!locations.contains(location)) {
      graph.insertNode(location);
      locations.add(location);
    }
  }

  /**
   * Clears the currently loaded graph contents before loading new graph data.
   */
  private void clearExistingGraph() {
    ArrayList<String> copy = new ArrayList<>(locations);
    for (String location : copy) {
      graph.removeNode(location);
    }
    locations.clear();
  }

  /**
   * Parses the first quoted token from a dot-file line.
   * @param line input line
   * @return first quoted string
   * @throws IOException if no quoted token is found
   */
  private String parseFirstQuoted(String line) throws IOException {
    int firstQuote = line.indexOf('"');
    int secondQuote = line.indexOf('"', firstQuote + 1);

    if (firstQuote < 0 || secondQuote < 0) {
      throw new IOException("Invalid dot file format.");
    }

    return line.substring(firstQuote + 1, secondQuote);
  }

  /**
   * Parses the second quoted token from a dot-file line.
   * @param line input line
   * @return second quoted string
   * @throws IOException if second quoted token is not found
   */
  private String parseSecondQuoted(String line) throws IOException {
    int firstQuote = line.indexOf('"');
    int secondQuote = line.indexOf('"', firstQuote + 1);
    int thirdQuote = line.indexOf('"', secondQuote + 1);
    int fourthQuote = line.indexOf('"', thirdQuote + 1);

    if (thirdQuote < 0 || fourthQuote < 0) {
      throw new IOException("Invalid dot file format.");
    }

    return line.substring(thirdQuote + 1, fourthQuote);
  }

  /**
   * Parses the minutes value from a dot-file edge line.
   * @param line input line
   * @return parsed edge time in minutes
   * @throws IOException if the minutes value cannot be parsed
   */
  private double parseWeight(String line) throws IOException {
    int minutesIndex = line.indexOf("minutes=");
    if (minutesIndex < 0) {
      throw new IOException("Invalid dot file format: missing minutes attribute.");
    }

    int start = minutesIndex + 8;
    while (start < line.length() && (line.charAt(start) == '"' || line.charAt(start) == ' ')) {
      start++;
    }

    int end = start;
    while (end < line.length() && (Character.isDigit(line.charAt(end))
        || line.charAt(end) == '.')) {
      end++;
    }

    try {
      return Double.parseDouble(line.substring(start, end));
    } catch (Exception e) {
      throw new IOException("Invalid edge minutes.");
    }
  }
}
