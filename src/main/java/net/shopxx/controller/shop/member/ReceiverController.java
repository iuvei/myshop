/*
 * Copyright 2005-2015 jshop.com. All rights reserved.
 * File Head

 */
package net.shopxx.controller.shop.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.controller.shop.BaseController;
import net.shopxx.entity.Area;
import net.shopxx.entity.Member;
import net.shopxx.entity.Receiver;
import net.shopxx.service.AreaService;
import net.shopxx.service.MemberService;
import net.shopxx.service.ReceiverService;
import net.shopxx.util.JsonUtils;

/**
 * Controller - 会员中心 - 收货地址
 * 
 * @author JSHOP Team
 \* @version 3.X
 */
@Controller("shopMemberReceiverController")
@RequestMapping("/member/receiver")
public class ReceiverController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", receiverService.findPage(member, pageable));
		model.addAttribute("member",member);
		
		return "/shop/${theme}/member/receiver/list_mdh2";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			addFlashMessage(redirectAttributes, Message.warn("shop.member.receiver.addCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return "redirect:list.jhtml";
		}
		return "/shop/${theme}/member/receiver/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Receiver receiver, Long areaId, RedirectAttributes redirectAttributes) {
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return ERROR_VIEW;
		}
		receiver.setAreaName(null);
		receiver.setMember(member);
		receiverService.save(receiver);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return ERROR_VIEW;
		}
		model.addAttribute("receiver", receiver);
		return "/shop/${theme}/member/receiver/edit";
	}
	
	/**
	 * 编辑收货地址
	 * 
	 * @param id
	 *            收货地址ID
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> info(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Area area = receiver.getArea();
		data.put("message", SUCCESS_MESSAGE);
		data.put("id", receiver.getId());
		data.put("consignee", receiver.getConsignee());
		data.put("cardId", receiver.getCardId());
		data.put("isDefault", receiver.getIsDefault());
		data.put("phone", receiver.getPhone());
		data.put("zipCode", receiver.getZipCode());
		data.put("address", receiver.getAddress());
		data.put("isDefault", receiver.getIsDefault());
		List<Map<String,String>> areaStr = new ArrayList<Map<String,String>>();
		do {
			Map<String,String> tmp=new HashMap<String,String>();
			tmp.put("areaId", area.getId().toString());
			tmp.put("areaName", area.getName());
			areaStr.add(tmp);
			area = area.getParent();
		} while (area != null);
		data.put("area", JsonUtils.toJson(areaStr));
		return data;
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Receiver receiver, Long id, Long areaId, RedirectAttributes redirectAttributes) {
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			return ERROR_VIEW;
		}
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(pReceiver.getMember())) {
			return ERROR_VIEW;
		}
		receiverService.update(receiver, "areaName", "member");
		receiver.getIsDefault();
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	/**
	 * 设置默认地址
	 * 
	 * @param id
	 *            收货地址ID
	 * @return
	 */
	@RequestMapping(value = "/updateDefault", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateDefault(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(pReceiver.getMember())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		pReceiver.setIsDefault(true);
		receiverService.update(pReceiver);
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}
	

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return ERROR_MESSAGE;
		}
		receiverService.delete(id);
		return SUCCESS_MESSAGE;
	}

}