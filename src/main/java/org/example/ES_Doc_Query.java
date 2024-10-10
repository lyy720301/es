package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;

import java.io.IOException;

public class ES_Doc_Query {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        // 创建 GetRequest，指定索引名称和文档 ID
        GetRequest getRequest = new GetRequest.Builder()
                .index("books")   // 指定索引名称
                .id("t9aLdpIB3JnUJwl-wW5X")
                .build();
        GetResponse<Book> bookGetResponse = client.get(getRequest, Book.class);

        // 输出查询结果
        System.out.println(bookGetResponse);
    }
}
