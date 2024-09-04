package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.util.Util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Playme {



        public static void main(String[] argv) {
        	try {
    			FileInputStream fileInputStream = new FileInputStream("C:/Users/Pierre/Desktop/sound/planetiscalling/start.mp3");
    			Player player = new Player(fileInputStream);
    			System.out.println("Song is playing...");
    			player.play();
    			Util.pause(10000);
    			player.close();
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (JavaLayerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

        }
}