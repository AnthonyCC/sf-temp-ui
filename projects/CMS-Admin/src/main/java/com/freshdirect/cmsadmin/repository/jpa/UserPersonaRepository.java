package com.freshdirect.cmsadmin.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.UserPersona;

/**
 * Repository for user persona association.
 */
public interface UserPersonaRepository extends CrudRepository<UserPersona, String> {

    List<UserPersona> findAll(Sort sort);

    /**
     * Find all users which are associated with given persona.
     *
     * @param persona
     *            Persona persona
     *
     * @return userPersonas
     */
    List<UserPersona> findAllByPersona(Persona persona, Sort sort);

}
