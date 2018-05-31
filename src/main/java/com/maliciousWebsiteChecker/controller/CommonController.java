/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maliciousWebsiteChecker.controller;

import com.maliciousWebsiteChecker.model.Website;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author santosh
 */
@Controller
public class CommonController {
    
    @RequestMapping(value = "/index.html",method = RequestMethod.GET)
    public String homePage(Model model){
        model.addAttribute("website", new Website());
        return "homePage";
    }
}
