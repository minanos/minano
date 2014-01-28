package com.minano.runtime.support.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

public class DefaultRequestInterceptor implements WebRequestInterceptor {

	@Override
	public void afterCompletion(final WebRequest requestToSet,
			final Exception exToSet) {
		//

	}

	@Override
	public void postHandle(final WebRequest requestToSet,
			final ModelMap modelToSet) {
		//
	}

	@Override
	public void preHandle(final WebRequest requestToSet) {
		final RequestAttributes requestAttributes = RequestContextHolder
				.getRequestAttributes();
		final HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
				.getRequest();
		if (request.getParameter("isAjax") != null
				|| request.getHeader("X-Requested-With") != null) {
			request.setAttribute(AjaxUtils.AJAX_ATTRIBUTE, true);
		} else {
			request.setAttribute(AjaxUtils.AJAX_ATTRIBUTE, false);
		}

	}

}
