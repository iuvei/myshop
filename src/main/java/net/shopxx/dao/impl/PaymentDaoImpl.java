/*
 * Copyright 2005-2015 jshop.com. All rights reserved.
 * File Head

 */
package net.shopxx.dao.impl;

import javax.persistence.NoResultException;

import net.shopxx.dao.PaymentDao;
import net.shopxx.entity.Payment;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Dao - 收款单
 * 
 * @author JSHOP Team
 \* @version 3.X
 */
@Repository("paymentDaoImpl")
public class PaymentDaoImpl extends BaseDaoImpl<Payment, Long> implements PaymentDao {

	public Payment findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String jpql = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Payment.class).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}