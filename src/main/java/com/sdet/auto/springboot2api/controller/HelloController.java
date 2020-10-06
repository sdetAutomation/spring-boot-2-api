package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
public class HelloController {

    @Autowired
    private ResourceBundleMessageSource messageSource;

//    @RequestMapping(method = RequestMethod.GET, path = "/helloworld")
    @GetMapping("/helloworld")
    public String helloWorld() {
        return "Hello, welcome to my springboot app!";
    }

    @GetMapping("/helloworld-bean")
    public UserDetails helloWorldBean() {
        return new UserDetails("sdet", "automation", "partsunknown");
    }

    @GetMapping("/hello-int")
    public String getMessagesInI18NFormat(@RequestHeader(name = "Accept-Language", required = false) String locale) {
        return messageSource.getMessage("label.hello", null, new Locale(locale));
    }

    @GetMapping("/hello-int2")
    public String getMessagesInI18NFormat() {
        return messageSource.getMessage("label.hello", null, LocaleContextHolder.getLocale());
    }
}