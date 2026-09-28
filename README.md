# CS 400 — Data Structures & Algorithms Portfolio

A curated portfolio of work from **COMP SCI 400: Programming III** at the University of Wisconsin–Madison.

Rather than publishing the raw course workspace, this repository focuses on the strongest integrated project and removes submission tooling, compiled files, editor state, nested Git histories, code-review copies, and other course-specific artifacts.

## Featured Project: European Rail Navigator

**[`projects/european-rail-navigator/`](projects/european-rail-navigator/)** is the final integrated project in this repository.

It is a Java web application that models European rail connections as a directed weighted graph and uses **Dijkstra's shortest-path algorithm** to find efficient routes between cities. The application loads **118 locations and 788 directed rail connections** from a DOT-format dataset, then serves a simple browser interface over Java's built-in HTTP server.

### Why this is the main project

The final application brings together the major data-structure and software-engineering topics from the course:

- **Dijkstra's algorithm** using a `PriorityQueue`
- a **custom hash table** used by the graph implementation
- directed, weighted graph traversal
- file parsing from DOT graph data
- frontend/backend interfaces
- component integration across a team project
- HTML generation and a lightweight Java HTTP server

The core pathfinding implementation is in [`DijkstraGraph.java`](projects/european-rail-navigator/DijkstraGraph.java). It tracks candidate routes with a minimum-priority queue, finalizes nodes through a visited map, and reconstructs the shortest path using predecessor links.

## Repository Structure

```text
cs400-github-ready/
├── README.md
├── .gitignore
└── projects/
    └── european-rail-navigator/
        ├── README.md
        ├── ATTRIBUTION.md
        ├── Makefile
        ├── WebApp.java
        ├── Frontend.java
        ├── Backend.java
        ├── DijkstraGraph.java
        ├── BaseGraph.java
        ├── HashTableMap.java
        ├── GraphADT.java
        ├── MapADT.java
        ├── BackendInterface.java
        ├── FrontendInterface.java
        ├── SmokeTest.java
        ├── europeanRail.dot
        └── template.html
```

## Run the Featured Project

Requirements: a recent JDK with `javac` and `java` available on your path.

```bash
cd projects/european-rail-navigator
make run
```

Then open:

```text
http://localhost:8000
```

You can also run a dependency-free smoke test:

```bash
make test
```

Or render one example request directly in the terminal:

```bash
make demo
```

## My Contributions

This project originated as a CS 400 team integration project. My work represented here includes:

- the **frontend implementation** that builds the route-query and response HTML;
- the **Dijkstra shortest-path implementation** merged from my individual graph assignment;
- the **custom hash-table implementation** merged from my individual data-structure assignment;
- integration fixes connecting the frontend, graph, backend, and web application;
- integration tests and build configuration used during the original project.

The backend component was originally developed by a teammate, and portions of the surrounding interfaces/web-app scaffold were supplied by the course. See [`ATTRIBUTION.md`](projects/european-rail-navigator/ATTRIBUTION.md) for a clearer component-level breakdown.

## About This Public Version

This repository is intentionally **not a verbatim dump of the original course directory**. Raw weekly assignments, submission checkers, nested Git repositories, compiled output, code-review copies from other students, and environment files were excluded so the repository reads as a software portfolio rather than a course hand-in archive.

No license is attached because the integrated project contains a mixture of my code, team code, and course-provided scaffolding.
