package org.example;

import org.example.pojo.person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

@SpringBootTest
class DemoApplicationTests {

  @Autowired
  ElasticsearchOperations elasticsearchOperations;

  @Autowired
  ElasticsearchTemplate elasticsearchTemplate;

  @Test
  void test1() {
    elasticsearchOperations.delete("2", person.class);
  }

  @Test
  void test2() {
    elasticsearchTemplate.indexOps(person.class).delete();
  }

}