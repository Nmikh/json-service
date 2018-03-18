package com.jsonservice.DAO;


import com.jsonservice.model.JMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<JMessage, Integer> {
}
