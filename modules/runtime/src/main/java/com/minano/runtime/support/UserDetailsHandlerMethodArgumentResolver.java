package com.minano.runtime.support;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
public  class UserDetailsHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return UserDetails.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(final MethodParameter parameter,
			final ModelAndViewContainer modelAndViewContainer,
			final NativeWebRequest webRequest,
			final WebDataBinderFactory binderFactory) {
		final Authentication auth = (Authentication) webRequest.getUserPrincipal();
		return auth != null && auth.getPrincipal() instanceof UserDetails ? auth.getPrincipal() : null;
	}
}