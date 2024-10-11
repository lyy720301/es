package org.example.pojo;

import org.springframework.data.annotation.Id;

public record person (
  @Id
  String id,
  String firstname,
  String lastname
){}