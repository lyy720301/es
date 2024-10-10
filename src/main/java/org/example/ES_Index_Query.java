package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;

import java.io.IOException;

public class ES_Index_Query {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        GetIndexRequest books = new GetIndexRequest.Builder().index("books").build();
        GetIndexResponse getIndexResponse = client.indices().get(books);
        System.out.println("查询索引");
        System.out.println(getIndexResponse.toString());

    }
}
