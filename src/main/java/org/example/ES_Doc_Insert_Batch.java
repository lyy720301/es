package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;

import java.io.IOException;

public class ES_Doc_Insert_Batch {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        // 创建 IndexRequest，指定索引名称和文档 ID
        BulkRequest.Builder builder = new BulkRequest.Builder();
        builder.operations(op -> op
                .index(idx -> idx
                        .index("books")             // 指定索引名称
                        .document(new Book("我的未来", "没什么", "大卫", 2024, "出版", 1.1F))             // 文档内容
                ));
        builder.operations(op -> op
                .index(idx -> idx
                        .index("books")             // 指定索引名称
                        .document(new Book("我的未来2", "没什么", "大卫", 2025, "出版", 1.4F))             // 文档内容
                ));

        BulkResponse bulkResponse = client.bulk(builder.build());

        // 检查批量插入结果
        if (!bulkResponse.errors()) {
            System.out.println("批量插入成功");
        } else {
            System.out.println("批量插入时发生错误");
        }
    }
}
