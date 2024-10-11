package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.esql.jdbc.ResultSetEsqlAdapter;
import co.elastic.clients.elasticsearch._helpers.esql.objects.ObjectsEsqlAdapter;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    /**
     * 获取es客户端
     *
     * @return
     */
    public static ElasticsearchClient getClient() {
        // URL and API key
        String serverUrl = "https://localhost:9200";

        RestClient restClient = RestClient.builder(HttpHost.create(serverUrl)).setDefaultHeaders(
                        new Header[]{new BasicHeader("Authorization", "Basic ZWxhc3RpYzoxMjM0NTY=")})
//                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setSSLContext(getUnsafeSslContext()))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    /**
     * 导入数据
     *
     * @param args
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, SQLException {

        ElasticsearchClient client = getClient();
        if (!client.indices().exists(ex -> ex.index("books")).value()) {
            CreateIndexResponse createIndexResponse = client.indices()
                    .create(c -> c
                            .index("books")
                            .mappings(mp -> mp
                                    .properties("title", p -> p.text(t -> t))
                                    .properties("description", p -> p.text(t -> t))
                                    .properties("author", p -> p.text(t -> t))
                                    .properties("year", p -> p.short_(s -> s))
                                    .properties("publisher", p -> p.text(t -> t))
                                    .properties("ratings", p -> p.halfFloat(hf -> hf))
                            ));
            System.out.println(createIndexResponse.acknowledged());
        }

        Instant start = Instant.now();
        System.out.println("Starting BulkIndexer... \n");

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("title") // same order as in the csv
                .addColumn("description")
                .addColumn("author")
                .addColumn("year")
                .addColumn("publisher")
                .addColumn("ratings")
                .setColumnSeparator(',')
                .setSkipFirstDataRow(true)
                .build();
        String csvPath = "C:\\Users\\44410\\IdeaProjects\\es-client\\src\\main\\resources\\books_en.csv";
        MappingIterator<Book> it = csvMapper
                .readerFor(Book.class)
                .with(schema)
                .readValues(new FileReader(csvPath));

        BulkIngester ingester = BulkIngester.of(bi -> bi
                .client(client)
                .maxConcurrentRequests(20)
                .maxOperations(5000));

        boolean hasNext = true;

        while (hasNext) {
            try {
                Book book = it.nextValue();
                ingester.add(BulkOperation.of(b -> b
                        .index(i -> i
                                .index("books")
                                .document(book))));
                hasNext = it.hasNextValue();
            } catch (JsonParseException | InvalidFormatException e) {
                // ignore malformed data
            }
        }

        ingester.close();

        client.indices().refresh();

        Instant end = Instant.now();

        System.out.println("Finished in: " + Duration.between(start, end).toMillis() + "\n");

        String queryAuthor =
                """
                            from books
                            | where author == "荷马"
                            | sort year desc
                            | limit 10
                        """;

        List<Book> queryRes = (List<Book>) client.esql().query(ObjectsEsqlAdapter.of(Book.class), queryAuthor);

        System.out.println("~~~\nObject result author:\n" + queryRes.stream().map(Book::title).collect(Collectors.joining("\n")));

        ResultSet resultSet = client.esql().query(ResultSetEsqlAdapter.INSTANCE, queryAuthor);

        System.out.println("~~~\nResultSet result author:");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("title"));
        }

        String queryPublisher =
                """
                            from books
                            | where publisher == "小布朗公司"
                            | sort ratings desc
                            | limit 10
                            | sort title asc
                        """;

        queryRes = (List<Book>) client.esql().query(ObjectsEsqlAdapter.of(Book.class), queryPublisher);
        System.out.println("~~~\nObject result publisher:\n" + queryRes.stream().map(Book::title).collect(Collectors.joining("\n")));
    }

    /**
     * 禁用SSL验证，如果信任了es的自签名证书则不需要。
     */
    private static SSLContext getUnsafeSslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}