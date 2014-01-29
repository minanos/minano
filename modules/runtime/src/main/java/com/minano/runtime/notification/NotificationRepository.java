package com.minano.runtime.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface NotificationRepository extends
		JpaRepository<Notification, Long>,
		JpaSpecificationExecutor<Notification>,
		QueryDslPredicateExecutor<Notification> {

	Notification findByName(String nameToSet);

}
