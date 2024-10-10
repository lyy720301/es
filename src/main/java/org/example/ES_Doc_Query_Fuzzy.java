package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import java.io.IOException;

/**
 * 组合查询
 */
public class ES_Doc_Query_Fuzzy {

    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("books")
                .query(
                        // 模糊查询
                        q -> q
                                .fuzzy(fu -> fu.field("title")
                                        .value("一")
                                        .fuzziness("AUTO"))

                )
                // 分页查询
                .from(0)
                .size(10)
//                .sort(SortOptions.of(f -> f.field(FieldSort.of(f1 -> f1.field("ratings").order(SortOrder.Desc)))))
                .sort(s -> s
                        .field(f -> f
                                .field("ratings")
                                .order(SortOrder.Desc)
                        )
                )
                .build();

        SearchResponse<Book> searchResponse = client.search(searchRequest, Book.class);

        // 输出查询结果
        System.out.println(searchResponse.hits().toString());
    }
}
