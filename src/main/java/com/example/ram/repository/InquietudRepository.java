package com.example.ram.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ram.model.Inquietud;

public interface InquietudRepository extends MongoRepository<Inquietud, String>{

}
