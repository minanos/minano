package com.minano.runtime.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RouterController {
	@RequestMapping(value = { "/", "welcome", "index" }, method = RequestMethod.GET)
	public String root() {
		return "index";
	}
	@RequestMapping(value = { "{path}" }, method = RequestMethod.GET)
	public String rootRouter(@PathVariable("path") final String path) {
		return path;
	}
}
