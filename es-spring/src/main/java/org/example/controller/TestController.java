package org.example.controller;

import org.example.pojo.person;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TestController {

  private ElasticsearchOperations elasticsearchOperations;

  public TestController(ElasticsearchOperations elasticsearchOperations) { 
    this.elasticsearchOperations = elasticsearchOperations;
  }

  @PostMapping("/person")
  public String save(@RequestBody person person) {
    org.example.pojo.person savedEntity = elasticsearchOperations.save(person);
    return savedEntity.id();
  }

  @GetMapping("/person/{id}")
  public person findById(@PathVariable("id")  Long id) {
    person person = elasticsearchOperations.get(id.toString(), org.example.pojo.person.class);
    return person;
  }
}