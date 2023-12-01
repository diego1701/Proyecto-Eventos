package com.example.ram.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ram.model.Reserva;
import com.example.ram.model.Salon;

public interface ReservaRepository extends MongoRepository<Reserva ,String> {


	boolean existsBySalonAndFecha(Salon salon, Date fecha);

	List<Reserva> findByEmail(String email);

}
