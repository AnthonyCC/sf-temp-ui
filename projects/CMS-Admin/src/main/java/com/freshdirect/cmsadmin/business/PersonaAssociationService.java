package com.freshdirect.cmsadmin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.repository.jpa.UserPersonaRepository;

/**
 * Service for persona association with user.
 */
@Service
public class PersonaAssociationService {

    @Autowired
    private UserPersonaRepository userPersonaRepository;

    @Autowired
    private Sort sortByNameIgnoreCaseAsc;

    /**
     * Returns with all user persona associations.
     *
     * @return userpersonas
     */
    public List<UserPersona> loadAllUserPersonaAssociations() {
        return userPersonaRepository.findAll(sortByNameIgnoreCaseAsc);
    }

    /**
     * Returns with all userpersonas associated with given persona.
     *
     * @param persona
     *            Persona persona
     * @return userpersonas
     */
    public List<UserPersona> loadAllUserPersonaAssociations(Persona persona) {
        Assert.notNull(persona, "Persona can't be null!");
        return userPersonaRepository.findAllByPersona(persona, sortByNameIgnoreCaseAsc);
    }

    /**
     * Gives user persona association by given user id.
     *
     * @param userId
     *            user id
     * @return user persona association
     */
    public UserPersona loadUserPersonaAssociation(String userId) {
        Assert.notNull(userId, "User id can't be null!");
        return userPersonaRepository.findOne(userId);
    }

    /**
     * Add association between user and persona.
     *
     * @param userPersona
     *            UserPersona userPersona
     */
    @Transactional
    public void addUserPersonaAssociation(UserPersona userPersona) {
        Assert.notNull(userPersona, "Userpersona can't be null!");
        userPersonaRepository.save(userPersona);
    }

    /**
     * Remove association between user and persona.
     *
     * @param userPersona
     *            UserPersona userPersona
     */
    @Transactional
    public void removeUserPersonaAssociation(UserPersona userPersona) {
        Assert.notNull(userPersona, "Userpersona can't be null!");
        userPersonaRepository.delete(userPersona);
    }
}
