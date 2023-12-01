package com.example.ram.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ram.model.Blog;

public interface BlogRepository extends MongoRepository<Blog,String>{

}
