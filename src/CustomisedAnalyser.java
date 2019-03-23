import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Arrays;
import java.util.List;

/**
 * @author kartikprabhu  15/12/18 7:10 PM
 */


/**
 * CustomAnalyser is based on {@link org.apache.lucene.analysis.standard.StandardAnalyzer}, with additional implementation of porter stemmer
 */
public class CustomisedAnalyser extends StopwordAnalyzerBase {
    /** @deprecated */
    @Deprecated
    public static final CharArraySet ENGLISH_STOP_WORDS_SET;
    private int maxTokenLength;

    public CustomisedAnalyser(CharArraySet stopWords) {
        super(stopWords);
        this.maxTokenLength = 255;
    }

    public CustomisedAnalyser() {
        this(ENGLISH_STOP_WORDS_SET);
    }

    /**
     * We use 3 filter on the terms in the document
     *
     * {@link LowerCaseFilter} for changing the upper cases to lower case so that the query is not case sensitive
     * {@link StopFilter} Used to remove stopward terms
     * {@link org.apache.lucene.analysis.en.PorterStemmer} Used to stem the words so that query can be made genric.
     */
    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        src.setMaxTokenLength(this.maxTokenLength);
        TokenStream filter1 = new LowerCaseFilter(src);
        TokenStream filter2 = new StopFilter(filter1, this.stopwords);
        TokenStream filter3 = new PorterStemFilter(filter2);
        return new TokenStreamComponents(src, filter3) ;
    }

    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }

    static {
        List<String> stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with");
        CharArraySet stopSet = new CharArraySet(stopWords, false);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }

}
