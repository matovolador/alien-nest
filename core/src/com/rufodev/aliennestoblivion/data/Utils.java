/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rufodev.aliennestoblivion.data;

import java.util.Random;

/**
 *
 * @author mato
 */



public class Utils {
    
    //int from a to b. a<b
    public static int doRandom(int a, int b) {
        Random random = new Random();
        int rand = random.nextInt(b ) + a;
        return rand;
        
        
    }
    
}
