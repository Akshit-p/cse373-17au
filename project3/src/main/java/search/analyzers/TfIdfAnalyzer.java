package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> idfScores = new ChainedHashDictionary<>();
        for (Webpage w: pages) {
           IList<String> words = w.getWords();
           ISet<String> visited = new ChainedHashSet<>();
           for (String word: words) {
               if (!visited.contains(word)) {
                   if (!idfScores.containsKey(word)) {
                       idfScores.put(word, 1.0);
                   } else {
                       double score = idfScores.get(word);
                       idfScores.put(word, ++score);
                   }
                   visited.add(word);
               }
           }
        }
        for (KVPair<String, Double> pair: idfScores) {
            double idf = Math.log(pages.size()/pair.getValue());
            idfScores.put(pair.getKey(), idf);
        }
        return idfScores;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<>();
        double incrementScore = 1.0/words.size();
        for (String word: words) {
            if (!tfScores.containsKey(word)) {
                tfScores.put(word, incrementScore);
            } else {
                double score = tfScores.get(word);
                tfScores.put(word, score + incrementScore);
            }
        }
        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> relevance = new ChainedHashDictionary<>();
        for (Webpage w: pages) {
            IList<String> words = w.getWords();
            IDictionary<String, Double> tfScore = this.computeTfScores(words);
            IDictionary<String, Double> tfIdfvector = new ChainedHashDictionary<>();
            for (String word: words) {
                tfIdfvector.put(word, tfScore.get(word)*this.idfScores.get(word));
            }            
            relevance.put(w.getUri(), tfIdfvector);
        }
        return relevance;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // TODO: Replace this with actual, working code.

        // TODO: The pseudocode we gave you is not very efficient. When implementing,
        // this smethod, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        IDictionary<String, Double> tfIdfvector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> tfScoreQuery = this.computeTfScores(query);
        IDictionary<String, Double> queryTfIdf = new ChainedHashDictionary<>();
        for (String word: query) {
            double idf = 0.0;
            if (this.idfScores.containsKey(word)) {
                idf = this.idfScores.get(word);
            }
            double tf = tfScoreQuery.get(word);
            queryTfIdf.put(word, tf*idf);
        }
        
        double numerator = 0.0;
        for (String word: query) {
            double docWordScore = 0.0;
            if (tfIdfvector.containsKey(word)) {
                docWordScore = tfIdfvector.get(word);
            }
            double queryWordScore = queryTfIdf.get(word);
            numerator += docWordScore*queryWordScore;
        }
        double denominator = norm(tfIdfvector) * norm(queryTfIdf);
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }

    private double norm(IDictionary<String, Double> tfIdfvector) {
        double result = 0.0;
        for (KVPair<String, Double> pair: tfIdfvector) {
            double score = pair.getValue();
            result += score * score;
        }
        return Math.sqrt(result);
    }
}
