/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orpheus;
import java.io.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.sin;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Orpheus {
    static int getBit(byte[] message, int index) {
        byte a;
        a = message[(int)index/8];
        a >>= abs(7 - index%8);
        a = (byte) (a & 0b00000001);
	return a;
}
    
    final static int amplitude = 2147483647;
    final static int samplingRate = 48000;
    final static double PI_2 = Math.PI*2;
    
    public static void main(String[] args) throws Exception {
//        
//        
//        byte[] a = {0b01010101,0b1111001,4};
//        for(int i = 0 ; i <= 15; i++)
//            System.out.println(getBit(a,i));
//        // создание одноканального wave-файла из массива целых чисел
//        System.out.println("Создание моно-файла...");
//        int[] samples = new int[500000];
//        for(int i=0; i < samples.length; i++){
//             samples[i] = (int)Math.round(amplitude*sin((PI_2*20000*i)/samplingRate));
//        }
////        for(int i= samples.length/2; i < samples.length; i++){
////            samples[i] = (int)Math.round((Integer.MAX_VALUE/2)*
////                        (10*Math.sin(Math.PI*440*i/44100)));
////        }
//        WaveFile wf = new WaveFile(4, 48000, 1, samples);
//        wf.saveFile(new File("10кГц.wav"));
//        System.out.println("Продолжительность моно-файла: "+wf.getDurationTime()+ " сек.");
//        
        // Создание стерео-файла
       
        
//        // Чтение данных из файла
//        System.out.println("Чтение данных из моно-файла:");
//        wf = new WaveFile(new File("/home/user/test/testwav1.wav"));
//        for(int i=0; i<10; i++){
//            System.out.println(wf.getSampleInt(i));
//        }
        byte[] a = {4,6,7,1};
        Transduser trans = new Transduser(a,1000);
    }
    
}
