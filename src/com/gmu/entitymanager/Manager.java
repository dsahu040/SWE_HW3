//This is EntityManager class, used to create instance for EntityManager
package com.gmu.entitymanager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Manager {

	private static final EntityManagerFactory emFactoryObj;
	private static final String PERSISTENCE_UNIT_NAME = "default";

	static {
		emFactoryObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	public static javax.persistence.EntityManager getEntityManager() {
		return emFactoryObj.createEntityManager();
	}

}