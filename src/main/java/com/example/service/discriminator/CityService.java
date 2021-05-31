package com.example.service.discriminator;

import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import com.example.entity.City;
import com.example.repository.CityRepository;

// @Service
public class CityService {

  // @Autowired
  private CityRepository cityRepository;

  // @PersistenceContext
  public EntityManager entityManager;

  public void save(City city) {
    cityRepository.save(city);
  }

  public List<City> getAll() throws SQLException {
    return cityRepository.findAll();
  }

  public City get(Long cid) {
    return cityRepository.findByCid(cid);
  }

  public City getByName(String name) {
    return cityRepository.findByName(name);
  }

  public void delete(String name) {
    cityRepository.deleteByName(name);
  }
}
