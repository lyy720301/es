package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;

import java.io.IOException;

public class ES_Doc_Insert {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        // 创建 IndexRequest，指定索引名称和文档 ID
        IndexRequest<Book> indexRequest = new IndexRequest.Builder<Book>()
                .index("books")     // 指定索引名称
                .document(new Book("我的人生", "没什么", "大卫", 2024, "出版", 1.3F))      // 文档内容（Map）
                .build();
        IndexResponse index = client.index(indexRequest);

        // 输出删除结果
        System.out.println(index.result());
    }
}
