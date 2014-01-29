package com.minano.runtime.notification;


import org.apache.commons.lang3.tuple.Triple;
import org.rest.common.persistence.service.AbstractService;
import org.rest.common.search.ClientOperation;
import org.rest.sec.util.SearchUtilSec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationServiceImpl extends AbstractService<Notification>
		implements NotificationService {

	@Autowired
	NotificationRepository dao;

	public NotificationServiceImpl() {
		super(Notification.class);
	}

	// API

	// get/find

	@Override
	public Notification findByName(final String name) {
		return getDao().findByName(name);
	}

	// create

	@Override
	public Notification create(final Notification entity) {
		/*
		 * final long id = IdUtil.randomPositiveLong(); entity.setId( id );
		 */

		/*
		 * final List< Privilege > associationsTemp = Lists.newArrayList(
		 * entity.getPrivileges() ); entity.getPrivileges().clear(); for( final
		 * Privilege privilege : associationsTemp ){ entity.getPrivileges().add(
		 * associationDao.findByName( privilege.getName() ) ); }
		 */

		return super.create(entity);
	}

	// Spring

	@Override
	public Specification<Notification> resolveConstraint(
			final Triple<String, ClientOperation, String> constraint) {
		return SearchUtilSec.resolveConstraint(constraint, Notification.class);
	}

	@Override
	protected final NotificationRepository getDao() {
		return dao;
	}

	@Override
	protected JpaSpecificationExecutor<Notification> getSpecificationExecutor() {
		return dao;
	}

}
