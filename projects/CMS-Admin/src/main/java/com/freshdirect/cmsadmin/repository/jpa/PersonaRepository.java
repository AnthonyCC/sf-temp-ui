package com.freshdirect.cmsadmin.repository.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cmsadmin.domain.Persona;

/**
 * Repository for persona.
 */
public interface PersonaRepository extends CrudRepository<Persona, Long> {

    @Override
    List<Persona> findAll();

}
