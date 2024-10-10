package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import java.io.IOException;
import java.util.List;

/**
 * 组合查询
 */
public class ES_Doc_Query_Bool {

    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("books")
                // 组合查询
                .query(
                        q -> q
                                .bool(b -> b
                                        .must(
                                                m -> m.match(mt -> mt.field("publisher")
                                                        .query("兄弟")
                                                )
                                        )
                                        .must(
                                                m -> m.match(mt -> mt.field("description")
                                                        .query("冒险")
                                                )
                                        )
                                        // 范围查询
                                        .filter(f -> f
                                                .range(fr -> fr
                                                        .number(n -> n.field("ratings")
                                                                .from(1d)
                                                                .to(2d)
                                                        )
                                                )
                                        )
                                )

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
