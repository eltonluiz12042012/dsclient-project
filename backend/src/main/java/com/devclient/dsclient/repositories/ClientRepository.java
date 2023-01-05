package com.devclient.dsclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devclient.dsclient.entities.Client;

@Repository
public interface  ClientRepository extends JpaRepository<Client, Long> {

}
