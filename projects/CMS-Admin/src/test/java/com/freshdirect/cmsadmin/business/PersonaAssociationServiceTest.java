package com.freshdirect.cmsadmin.business;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.repository.jpa.UserPersonaRepository;
import com.freshdirect.cmsadmin.utils.EntityFactory;

/**
 * Unit test cases for test PersonaAssociationService methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PersonaAssociationServiceTest {

    @Mock
    private UserPersonaRepository userPersonaRepository;

    @Mock
    private Sort sortByNameIgnoreCaseAsc;

    @InjectMocks
    private PersonaAssociationService underTest;

    @Test
    public void testLoadAllUserPersonaAssociateds() {
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona());
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<UserPersona> userPersonasAssociated = underTest.loadAllUserPersonaAssociations();
        Assert.assertEquals(userPersonasAssociated, userPersonas);
    }

    @Test
    public void testLoadAllUserPersonaAssociatedWithPersona() {
        Persona persona = EntityFactory.createPersona();
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona());
        Mockito.when(userPersonaRepository.findAllByPersona(persona, sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<UserPersona> userPersonasAssociated = underTest.loadAllUserPersonaAssociations(persona);
        Assert.assertEquals(userPersonasAssociated, userPersonas);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadAllUserPersonaAssociatedWithNullPersona() {
        underTest.loadAllUserPersonaAssociations(null);
        Assert.fail("Service accepts null persona!!!");
    }

    @Test
    public void testRemoveUserPersonaAssociation() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        underTest.removeUserPersonaAssociation(userPersona);
        Mockito.verify(userPersonaRepository).delete(userPersona);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveUserPersonaAssociatedWithNullUserPersona() {
        underTest.removeUserPersonaAssociation(null);
        Assert.fail("Service accepts null userpersona!!!");
    }

    @Test
    public void testAddUserPersonaAssociation() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        underTest.addUserPersonaAssociation(userPersona);
        Mockito.verify(userPersonaRepository).save(userPersona);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddUserPersonaAssociationWithNullUserPersona() {
        underTest.addUserPersonaAssociation(null);
        Assert.fail("Service accepts null userpersona!!!");
    }
}
