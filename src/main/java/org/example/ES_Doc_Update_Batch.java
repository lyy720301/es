package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ES_Doc_Update_Batch {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        // 删除文档
        BulkRequest request = new BulkRequest.Builder()
                .operations(op -> op.delete(
                        del -> del.index("books")
                                .id("udamdpIB3JnUJwl-uG42"))
                ).build();

        BulkResponse bulkResponse = client.bulk(request);
        // 输出删除结果
        System.out.println(bulkResponse.toString());
    }
}
