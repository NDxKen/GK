/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gkltm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author admin
 */
public class Server {

    public static int PORT = 7988;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(PORT);
        while (true) {
            Socket s = ss.accept();
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            String cipherText = din.readUTF();
            int key = din.read();
            String plainText = decrypt(cipherText, key);
            
            Map<Character, Integer> map = getMap(plainText);
            int seconMax = secondMostAppearTheNumbers(map);
            char secondCharacter = secondMostAppearCharacter(map, seconMax);

            System.out.println(plainText);
//            for(char c : map.keySet()) {
//                System.out.println(c + " " + map.get(c));
//            }
//            System.out.println("second " + seconMax);
//            System.out.println("Char " + secondCharacter);
            String res = secondCharacter + " " + seconMax;
            dout.writeUTF(res);
        }
    }

    public static String decrypt(String cipherText, int key) {
        StringBuilder plainText = new StringBuilder(cipherText);
        int currPosition = 0;
        for (int row = 0; row < key; row++) {
            int iter = 0;
            for (int i = row; i < cipherText.length(); i += Client.getJump(iter++, row, key)) {
                plainText.setCharAt(i, cipherText.charAt(currPosition++));
            }
        }
        return plainText.toString();
    }

    public static int secondMostAppearTheNumbers(Map<Character, Integer> map) {
        int max = 0;
        int secondMax = 0;
        char ch = '-';
        for (char c : map.keySet()) { 
            if (map.get(c) > max) {
                secondMax = max;
                max = map.get(c);
            } else if (map.get(c) > secondMax && map.get(c) != max) {
                secondMax = map.get(c);
            }
        }
        return secondMax;
    }

    public static char secondMostAppearCharacter(Map<Character, Integer> map, int seconMax) {
        char secondCharacter = '-';
        for (char c : map.keySet()) {
            if (map.get(c).compareTo(seconMax) == 0) {
                secondCharacter = c;
                return secondCharacter;
            }
        }
        return secondCharacter;
    }

    public static Map<Character, Integer> getMap(String plainText) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < plainText.length(); i++) {
            if (map.containsKey(plainText.charAt(i))) {
                map.put(plainText.charAt(i), map.get(plainText.charAt(i)) + 1);
            } else {
                map.put(plainText.charAt(i), 1);
            }
        }
        return map;
    }

}
