/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maliciousWebsiteChecker.service;

import com.maliciousWebsiteChecker.api.TrustIndicatorsWOTService;
import org.springframework.stereotype.Service;

/**
 *
 * @author santosh
 */
@Service
public class MaliciousCheckerService {
    
   public boolean checkWebsiteByWOT(String website){
       
       boolean malicious = false;
       TrustIndicatorsWOTService trustIndicatorsWOTService = new TrustIndicatorsWOTService();
       int[] scores = trustIndicatorsWOTService.trustScores(website);
       if(scores[0] ==0 || scores[1] == 0){
           malicious = true;
       }
       return malicious;
   }
    
}
