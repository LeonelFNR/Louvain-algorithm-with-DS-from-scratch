Personal implementation and adaptation of the Louvain algorithm that implements the made-from-scratch data strcutures from the Data Structures course that I took as Mathematical and Physical Engineering student.

I wanted to build something bigger in order to make sense of all what was learned in the subject, plus the fact that no one was able to replicate the original algorithm with its efficiency. As a personal project I decided to go again through all my self-implemented Data Structures, clean them as much as possible, and re-create the Louvain algorithm once again, with a small adaptation that I called a **refiner**.

Once having applied the Louvain algorithm to the given graph, the refiner goes through each vertex of the graph once agains and checks if any single movement of a single vertex to another community would increase the modularity. I have found this extra function to enhance the effectiveness of the algorithm, for example in file "dolphins.net", the obtained distribution is of greater modularity than the one obtained using Pajek software or Gephi.

Moreover, is important to note that this implementation is able to read Pajek format files.

