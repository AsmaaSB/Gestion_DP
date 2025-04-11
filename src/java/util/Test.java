/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author PC
 */
public class Test {
    
     {
        System.out.println("non static 1");
    }
    
    static {
        System.out.println("static 1");
    }
    
    static {
        System.out.println("static 2");
    }
    
    
    {
        System.out.println("non static 2");
    }

    public Test() {
        System.out.println("construct");
    }
    
    
    public static void main(String[] args) {
        new Test();
    }
    
    
}
