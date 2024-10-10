package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ES_Doc_Update {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        // 更新文档
        // 定义文档更新的内容，使用Map表示部分更新
        Map<String, Object> doc = new HashMap<>();
        doc.put("title", "罪与罚1"); // 更新的字段和值
        UpdateRequest<Book, Map> updateRequest = new UpdateRequest.Builder<Book, Map>()
                .index("books")      // 指定索引名称
                .id("o9Z_dpIB3JnUJwl-gm78")             // 指定文档ID
                .doc(doc) // 需要更新的文档内容（部分字段）
                .build();

        UpdateResponse<Book> updateResponse = client.update(updateRequest, Book.class);

        // 输出更新结果
        System.out.println(updateResponse.result());


    }
}
