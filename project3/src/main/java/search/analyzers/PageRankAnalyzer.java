package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        // build the graph with key being the node and values representing edges between
        // other URI node
        for (Webpage w: webpages) {
            IList<URI> links = w.getLinks();
            ISet<URI> edges = new ChainedHashSet<>();
            for (URI link: links) {
                Webpage temp = new Webpage(link, null, null, null, null);
                if (!link.equals(w.getUri()) && webpages.contains(temp)) {
                    edges.add(link);
                }
            }
            graph.put(w.getUri(), edges);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> pRank = new ChainedHashDictionary<>();
        
        // computing initial rank for every webpage.
        double initialRank = 1.0 / graph.size(); 
        for (KVPair<URI, ISet<URI>> node: graph) {
            pRank.put(node.getKey(), initialRank);
        }
        
        for (int i = 0; i < limit; i++) {
            //create a map to store new page ranks of the webpages
            IDictionary<URI, Double> newRank = new ChainedHashDictionary<>();
            // every webpage common rank value i.e 
            // (1-d)/N + (if no link webpage -> d*oldRank of no link webpage/N)
            double rank = (1 - decay) / graph.size();
            // traverse the graph to add to compute new page rank of the webpages
            for (KVPair<URI, ISet<URI>> node: graph) {
                URI link = node.getKey();
                
                // initialize new page rank of the webpage if not already done
                if (!newRank.containsKey(link)) {
                    newRank.put(link, 0.0);
                }
                
                // all the outgoing links of this webpage.
                ISet<URI> edges = node.getValue();
                // if no outgoing links then we must update rank for every webpage.
                if (edges.isEmpty()) {
                    // add d*oldRank/size to the rank
                    rank += decay * pRank.get(link) / graph.size();
                } else {
                    // compute the new rank for the outgoing linked webpages.
                    for (URI edge: edges) {
                        double oldRank = 0.0;
                        // get old rank if there was one computed in previous iteration(s).
                        if (newRank.containsKey(edge)) {
                            oldRank = newRank.get(edge);
                        }
                        
                        // new rank = old + decay * PR(T_i) / C(T_i)
                        // PR(T_i) is the page rank of ith webpage that links to this edge webpage
                        // C(T_i) is the unique number of outgoing links from this ith webpage
                        double updatedRank = oldRank + (decay * pRank.get(link) / edges.size());
                        
                        // add the outgoing webpage updated rank to the map for 
                        // later changes and convergence comparison
                        newRank.put(edge, updatedRank);
                    }
                }
            }
            
            boolean breakOuterLoop = true;
            
            // perform convergence check by comparing the old and the new pagerank
            // difference to the client specified epsilon value.
            for (KVPair<URI, Double> oldRank: pRank) {
                
                // if difference in pagerank > epsilon then update page rank of 
                // webpages for next outer most iteration
                if (oldRank.getValue() - newRank.get(oldRank.getKey()) > epsilon) {
                    // update to new rank.
                    for (KVPair<URI, Double> pair: newRank) {
                        pRank.put(pair.getKey(), pair.getValue() + rank);
                    }
                    breakOuterLoop = false;
                    // exit this inner loop
                    break;
                }
            }
            // if we reached convergence then exit the loop.
            if (breakOuterLoop) {
                break;
            }         
        }
        return pRank;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        return this.pageRanks.get(pageUri);
    }
}
