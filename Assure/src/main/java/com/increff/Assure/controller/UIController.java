package com.increff.Assure.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "/ui")
    public ModelAndView index()
    {
        return mav("index.html");
    }

    public ModelAndView mav(String page)
    {
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("baseUrl",baseUrl);
        return mav;
    }
}
