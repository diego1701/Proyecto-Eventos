package com.example.ram.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ram.model.Elementos;

public interface ElementoRepository extends MongoRepository<Elementos,String>{
	
}


