package com.cuzz.springaidemo;

import org.jetbrains.annotations.Nullable;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionTextParser;
import org.springframework.util.Assert;

public class SearchRequest {

    public static final double SIMILARITY_THRESHOLD_ACCEPT_ALL = 0.0;

    public static final int DEFAULT_TOP_K = 4;

    private String query = "";

    private int topK = DEFAULT_TOP_K;

    private double similarityThreshold = SIMILARITY_THRESHOLD_ACCEPT_ALL;

    private Filter.Expression filterExpression;
    // Static method to create a new Builder instance
    public static Builder builder() {
        return new Builder();
    }
    public static Builder from(SearchRequest originalSearchRequest) {
        return builder().query(originalSearchRequest.getQuery())
                .topK(originalSearchRequest.getTopK())
                .similarityThreshold(originalSearchRequest.getSimilarityThreshold())
                .filterExpression(originalSearchRequest.getFilterExpression());
    }

    public static class Builder {

        private final SearchRequest searchRequest = new SearchRequest();

        public Builder query(String query) {
            Assert.notNull(query, "Query can not be null.");
            this.searchRequest.query = query;
            return this;
        }

        public Builder topK(int topK) {
            Assert.isTrue(topK >= 0, "TopK should be positive.");
            this.searchRequest.topK = topK;
            return this;
        }

        public Builder similarityThreshold(double threshold) {
            Assert.isTrue(threshold >= 0 && threshold <= 1, "Similarity threshold must be in [0,1] range.");
            this.searchRequest.similarityThreshold = threshold;
            return this;
        }

        public Builder similarityThresholdAll() {
            this.searchRequest.similarityThreshold = 0.0;
            return this;
        }

        public Builder filterExpression(@Nullable Filter.Expression expression) {
            this.searchRequest.filterExpression = expression;
            return this;
        }

        public Builder filterExpression(@Nullable String textExpression) {
            this.searchRequest.filterExpression = (textExpression != null)
                    ? new FilterExpressionTextParser().parse(textExpression) : null;
            return this;
        }

        public SearchRequest build() {
            return this.searchRequest;
        }

    }

    public String getQuery() {return "111";}
    public int getTopK() {return 2;}
    public double getSimilarityThreshold() {return 2;}
    public Filter.Expression getFilterExpression() {return null;}
}