/*
 * Copyright 2005-2015 jshop.com. All rights reserved.
 * File Head

 */
package net.shopxx.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity - 支付方式
 * 
 * @author JSHOP Team
 \* @version 3.X
 */
@Entity
@Table(name = "xx_payment_method")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_payment_method")
public class PaymentMethod extends OrderEntity<Long> {

	private static final long serialVersionUID = 4079811920107015825L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 款到发货 */
		deliveryAgainstPayment,

		/** 货到付款 */
		cashOnDelivery
	}

	/**
	 * 方式
	 */
	public enum Method {

		/** 在线支付 */
		online,

		/** 线下支付 */
		offline
	}

	/** 名称 */
	private String name;

	/** 类型 */
	private PaymentMethod.Type type;

	/** 方式 */
	private PaymentMethod.Method method;

	/** 超时时间 */
	private Integer timeout;

	/** 图标 */
	private String icon;

	/** 介绍 */
	private String description;

	/** 内容 */
	private String content;

	/** 配送方式 */
	@JsonIgnore
	private Set<ShippingMethod> shippingMethods = new HashSet<ShippingMethod>();

	/** 订单 */
	@JsonIgnore
	private Set<Order> orders = new HashSet<Order>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@NotNull
	@Column(nullable = false)
	public PaymentMethod.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(PaymentMethod.Type type) {
		this.type = type;
	}

	/**
	 * 获取方式
	 * 
	 * @return 方式
	 */
	@NotNull
	@Column(nullable = false)
	public PaymentMethod.Method getMethod() {
		return method;
	}

	/**
	 * 设置方式
	 * 
	 * @param method
	 *            方式
	 */
	public void setMethod(PaymentMethod.Method method) {
		this.method = method;
	}

	/**
	 * 获取超时时间
	 * 
	 * @return 超时时间
	 */
	@Min(1)
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param timeout
	 *            超时时间
	 */
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	/**
	 * 获取图标
	 * 
	 * @return 图标
	 */
	@Length(max = 200)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置图标
	 * 
	 * @param icon
	 *            图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取介绍
	 * 
	 * @return 介绍
	 */
	@Length(max = 200)
	public String getDescription() {
		return description;
	}

	/**
	 * 设置介绍
	 * 
	 * @param description
	 *            介绍
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	@Lob
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取配送方式
	 * 
	 * @return 配送方式
	 */
	@ManyToMany(mappedBy = "paymentMethods", fetch = FetchType.LAZY)
	public Set<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethods
	 *            配送方式
	 */
	public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	@OneToMany(mappedBy = "paymentMethod", fetch = FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<ShippingMethod> shippingMethods = getShippingMethods();
		if (shippingMethods != null) {
			for (ShippingMethod shippingMethod : shippingMethods) {
				shippingMethod.getPaymentMethods().remove(this);
			}
		}
		Set<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.setPaymentMethod(null);
			}
		}
	}

}
