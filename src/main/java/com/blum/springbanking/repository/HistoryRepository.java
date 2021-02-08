package com.blum.springbanking.repository;

import com.blum.springbanking.models.History;
import org.springframework.data.repository.CrudRepository;

public interface HistoryRepository extends CrudRepository<History, Long> {

}
