package com.minano.runtime.support.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AjaxUtils {

	public final static String AJAX_ATTRIBUTE = AjaxUtils.class.getName()
			+ ".AJAX_REQUEST";

	public static Boolean isAjaxRequest() {
		final RequestAttributes requestAttributes = RequestContextHolder
				.getRequestAttributes();
		final HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
				.getRequest();
		final Boolean ajax = (Boolean) request.getAttribute(AJAX_ATTRIBUTE);

		return ajax;
	}



}
