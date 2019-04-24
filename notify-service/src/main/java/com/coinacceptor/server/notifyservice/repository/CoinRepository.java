package com.coinacceptor.server.notifyservice.repository;

import com.coinacceptor.server.notifyservice.model.CoinAcceptor;
import org.springframework.data.repository.CrudRepository;

public interface CoinRepository extends CrudRepository<CoinAcceptor, String> {
}
