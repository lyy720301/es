package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.AvgAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.MaxAggregate;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import java.io.IOException;

/**
 * 组合查询
 */
public class ES_Doc_Query_Group {

    public static void main(String[] args) throws IOException {
        ElasticsearchClient client =
                Main.getClient();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("books")
                .aggregations(
                        "max_year", agg -> agg.max(max -> max.field("year"))
                )
                .aggregations("avg_year", agg -> agg.avg(avg -> avg.field("year")))
                .from(0)
                .size(1000)
                .build();

        SearchResponse<Book> searchResponse = client.search(searchRequest, Book.class);
        // 处理聚合结果
        MaxAggregate maxYear = searchResponse.aggregations().get("max_year").max();
        System.out.println("最大出版年: " + maxYear.value());
        AvgAggregate avgYear = searchResponse.aggregations().get("avg_year").avg();
        System.out.println("平均出版年: " + avgYear.value());
        // 输出查询结果
        System.out.println(searchResponse.toString());
    }
}
