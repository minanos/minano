package com.minano.runtime.support.web;

import java.net.URI;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class BaseURLInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(BaseURLInterceptor.class);

	public BaseURLInterceptor(final String cdnURLToSet) {
		cdnURL = cdnURLToSet;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request,
			final HttpServletResponse response, final Object handler) {
		return true;
	}

	private String cdnURL;

	@Override
	public void postHandle(final HttpServletRequest request,
			final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) {
		final URI baseURL = ServletUriComponentsBuilder
				.fromCurrentContextPath().build().toUri();
		if (logger.isDebugEnabled()) {
			logger.debug(baseURL.toString());
			if (modelAndView != null) {
				for (final Entry<String, Object> x : modelAndView.getModelMap()
						.entrySet()) {
					logger.debug(x.getKey() + " = " + x.getValue());
				}
			}
		}
		if (modelAndView != null
				&& !modelAndView.getViewName().startsWith("redirect:")) {
			final Boolean bl = AjaxUtils.isAjaxRequest();
			if (!bl) {
				modelAndView.getModel().put("LAYOUT", "/layout/layout");
			} else {
				modelAndView.getModel().put("LAYOUT", "/layout/noLayout");
			}

			modelAndView.getModel().put("BASEURL", baseURL);
			modelAndView.getModel().put("baseURL", baseURL);
			modelAndView.getModel().put("baseurl", baseURL);
			modelAndView.getModel().put("cdnURL", cdnURL);

		}

	}
}
