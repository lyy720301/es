package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ES_Doc_Delete {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        // 删除文档
        DeleteRequest updateRequest = new DeleteRequest.Builder()
                .index("books") // 指定索引名称
                .id("o9Z_dpIB3JnUJwl-gm78")             // 指定文档ID
                .build();

        DeleteResponse delete = client.delete(updateRequest);

        // 输出删除结果
        System.out.println(delete.result());


    }
}
