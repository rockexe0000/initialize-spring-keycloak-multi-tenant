package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.City;

@Transactional
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  City findByCid(Long cid);

  City findByName(String name);

  void deleteByName(String name);
}
