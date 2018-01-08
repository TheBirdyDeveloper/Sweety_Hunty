package testGame;

import static org.junit.Assert.*;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.jgroups.JChannel;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

import gameDisplay.GridDisplay;
import multiplayerLogic.Multiplayer;

public class ParallelComputerExample {

    @Test
    public void runAllTests() {
        Class<?>[] classes = { ParallelPlayer1.class, ParallelPlayer2.class };

        // ParallelComputer(true,true) will run all classes and methods 
        // in parallel.  (First arg for classes, second arg for methods)
        JUnitCore.runClasses(new ParallelComputer(true, true), classes);
    }

    public static class ParallelPlayer1 {
    	Multiplayer game = new Multiplayer();
        @Test
        public void test1a() throws Exception {
        	game.startSessionTest();
            
        }
    }

    public static class ParallelPlayer2 {
    	Multiplayer game = new Multiplayer();
        @Test
        public void test1a() throws Exception {
            Thread.sleep(5000);
            game.startSessionTest();
  
            
        }
    }

  
}
