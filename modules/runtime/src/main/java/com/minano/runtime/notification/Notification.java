package com.minano.runtime.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.rest.common.persistence.model.INameableEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@Entity
@XmlRootElement
@XStreamAlias("notification")
public class Notification implements INameableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "NOTIFICATION_ID")
	@XStreamAsAttribute
	private Long id;
	@Column(unique = true, nullable = false)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long idToSet) {
		id = idToSet;
	}

	public String getName() {
		return name;
	}

	public void setName(String nameToSet) {
		name = nameToSet;
	}

}
