package com.devclient.dsclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devclient.dsclient.dto.ClientDTO;
import com.devclient.dsclient.entities.Client;
import com.devclient.dsclient.repositories.ClientRepository;
import com.devclient.dsclient.services.exceptions.DatabaseException;
import com.devclient.dsclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly= true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequets){
		Page<Client> list = repository.findAll(pageRequets);
		return list.map(x-> new ClientDTO(x));
	}
	
	@Transactional(readOnly= true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(()-> new ResourceNotFoundException("Recurso Inexistente"));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		DtoToEntity(entity, dto);
		return new ClientDTO(repository.save(entity));
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id);
			DtoToEntity(entity, dto);
			return new ClientDTO(repository.save(entity));
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID inexistente " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID not found: " + id);
		}catch(DataIntegrityViolationException e) {
			throw new ResourceNotFoundException("Integrity violation");
		}
	}
	
	private void DtoToEntity(Client client, ClientDTO dto) {
		client.setName(dto.getName());
		client.setIncome(dto.getIncome());
		client.setCpf(dto.getCpf());
		client.setChildren(dto.getChildren());
		client.setBirthDate(dto.getBirthDate());
		
	}
}
