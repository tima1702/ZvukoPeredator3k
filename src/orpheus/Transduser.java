package orpheus;



import java.io.File;
import static java.lang.Math.abs;
import static java.lang.Math.sin;
import static jdk.nashorn.internal.objects.ArrayBufferView.buffer;
//import orpheus.WaveFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tima
 */
public class Transduser {
    
    final static int amplitude = 2147483647;
    final static int samplingRate = 48000;
    final static int freq_0 = 19000;
    final static int freq_1 = 20000;
    final static double PI_2 = Math.PI*2;
    
    public Transduser(byte [] message, int time) throws Exception {// time - время передачи бита
        int freq;
        int buffer[] = new int[samplingRate*message.length*time*8/1000];
        System.out.println(buffer.length + " ");
        double angle = 0;
        for(int i = 0; i < message.length*8; i++){
            if(getBit(message,i) == 0) freq = freq_0;
            else freq = freq_1;
            System.out.print(getBit(message,i)+ " ");
                for (int j = 0; j < time*samplingRate/1000; j++)
                {
                    double angularFrequency = PI_2 * freq /samplingRate;
                    buffer[i*time*samplingRate/1000 + j] =  (int)Math.round(amplitude*
                            sin(angle));
                    angle+= angularFrequency;
                }
        }
        WaveFile wf;
        wf = new WaveFile(4, 48000, 1, buffer);
        wf.saveFile(new File("message.wav"));
        System.out.println("Продолжительность моно-файла: "+wf.getDurationTime()+ " сек.");
    }
    
    static int getBit(byte[] message, int index) {
        byte a;
        a = message[(int)index/8];
        a >>= abs(7 - index%8);
        a = (byte) (a & 0b00000001);
	return a;
}
}
