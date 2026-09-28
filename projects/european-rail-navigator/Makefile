JAVAC = javac
JAVA = java
SOURCES = Backend.java BackendInterface.java BaseGraph.java DijkstraGraph.java \
          Frontend.java FrontendInterface.java GraphADT.java HashTableMap.java \
          MapADT.java WebApp.java

.PHONY: compile run demo test clean

compile:
	$(JAVAC) $(SOURCES)

run: compile
	$(JAVA) WebApp 8000

demo: compile
	$(JAVA) WebApp "start=Paris&end=Berlin"

test: compile
	$(JAVAC) SmokeTest.java
	$(JAVA) SmokeTest

clean:
	rm -f *.class
