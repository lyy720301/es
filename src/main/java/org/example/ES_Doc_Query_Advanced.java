package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;

import java.io.IOException;

public class ES_Doc_Query_Advanced {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        SearchRequest searchRequest = new SearchRequest.Builder().index("books").query(q -> q.matchAll(m -> m)).build();
        SearchResponse<Book> searchResponse = client.search(searchRequest, Book.class);

        // 输出查询结果
        System.out.println(searchResponse.hits().toString());
    }
}
