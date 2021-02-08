package com.blum.springbanking.repository;

import com.blum.springbanking.models.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.Optional;


public interface CardRepository extends CrudRepository<Card, Long> {
    @Override
    Optional<Card> findById(Long aLong);

    @Override
    Iterable<Card> findAll();

    Optional<Card> findCardByCardNumber(long cardNumber);
    


}
