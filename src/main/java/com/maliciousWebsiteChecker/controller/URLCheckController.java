/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maliciousWebsiteChecker.controller;

import com.maliciousChecker.common.Common;
import com.maliciousWebsiteChecker.model.Website;
import com.maliciousWebsiteChecker.service.MaliciousCheckerService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author santosh
 */
@Controller
public class URLCheckController {

    @Autowired
    private MaliciousCheckerService maliciousCheckerService;

    @RequestMapping(value = "/check-url.html", method = RequestMethod.POST)
    public String checkWebsite(Website website, Model model) {

        List<List<String>> cookieContainer = new ArrayList<>();
        StringBuilder redirectedUrl = new StringBuilder();
        int statusCode = Common.getStatusCode(website.getDomainName(), redirectedUrl, cookieContainer);

        if (statusCode == 200) {
            boolean malicious = maliciousCheckerService.checkWebsiteByWOT(redirectedUrl.toString());
            website.setMalicious(malicious);
            model.addAttribute("website", website);
        }else{
            model.addAttribute("validity", "URL is not valid");
        }

        return "homePage";
    }

}
