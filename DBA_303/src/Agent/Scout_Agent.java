/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import components.*;

/**
 *
 * @author vipeba
 */
public class Scout_Agent extends Agent{
    
    Map explored_area;
    
    Position p;
    
    @Override
    public void setup(){
        System.out.println("Hello! I'm Agent");
        
        this.addBehaviour(new average());
        
        //doDelete();
    }
    
    
    @Override
    protected void takeDown(){
        System.out.println("Terminating Agent...");
    }
    
    /**
     * Pedir cuantos numeros
     * Bucle que guarde los numeros
     * Mostrar la media
     */
    class average extends Behaviour{
        
        @Override
        public void action() {

            System.out.print("Program that calculates average.\n");
            System.out.print("Indicate how many numbers you want to average:\n");
            
        }
        
        @Override
        public boolean done(){
           return true; 
        }
        
    }
    
    
    
}
