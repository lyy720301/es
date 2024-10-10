package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;

import java.io.IOException;

public class ES_Index_Delete {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        DeleteIndexRequest books = new DeleteIndexRequest.Builder().index("books").build();
        DeleteIndexResponse delete = client.indices().delete(books);
        System.out.println("删除索引");
        System.out.println(delete.toString());

    }
}
