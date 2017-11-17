package com.freshdirect.cms.changecontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;

/**
 * Purpose of {@code ContentChangeEntitySetRepository} to provide save functionality for changes. Queries implemented in {@code DatabaseChangeControlService} based on performance
 * issues.
 */
public interface ContentChangeEntitySetRepository extends JpaRepository<ContentChangeSetEntity, Integer> {

}
