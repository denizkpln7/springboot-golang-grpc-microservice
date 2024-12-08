package com.denizkpln.user_service.repository;


import com.denizkpln.user_service.model.Adress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdressRepository extends JpaRepository<Adress, Long> {
}
