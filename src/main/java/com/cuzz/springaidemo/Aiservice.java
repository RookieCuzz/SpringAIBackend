package com.cuzz.springaidemo;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Aiservice {


    @Value("classpath:/prompt/data.txt")
    private Resource textResource;

    @jakarta.annotation.Resource
    VectorStore vectorStore2;

    public List<Document> getQueryDocs(String input){
        TextReader textReader = new TextReader(textResource);
        List<Document> documents= textReader.get();

        System.out.println(documents);
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        vectorStore2.add(tokenTextSplitter.apply(documents));
        List<Document> documents1 = vectorStore2.similaritySearch(input);
        return documents1;
    }
}
