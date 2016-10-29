/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orpheus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Простой класс для работы с wave-файлами. 
 *  
 * @author eqlbin
 *
 */
public class WaveFile {

    private int INT_SIZE = 4;
    public final int NOT_SPECIFIED = -1;
    private int sampleSize = NOT_SPECIFIED;
    private long framesCount = NOT_SPECIFIED;
    private byte[] data = null;  // массив байт представляющий аудио-данные 
    private AudioInputStream ais = null;
    private AudioFormat af = null;
    
    /**
     * Создает объект из указанного wave-файла
     * 
     * @param file - wave-файл
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    WaveFile(File file) throws UnsupportedAudioFileException, IOException {
        
        if(!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        
        // получаем поток с аудио-данными
        ais = AudioSystem.getAudioInputStream(file);  
        
        // получаем информацию о формате
        af = ais.getFormat();
        
        // количество кадров в файле
        framesCount = ais.getFrameLength();
        
        // размер сэмпла в байтах
        sampleSize = af.getSampleSizeInBits()/8;
        
        // размер данных в байтах
        long dataLength = framesCount*af.getSampleSizeInBits()*af.getChannels()/8;
        
        // читаем в память все данные из файла разом
        data = new byte[(int) dataLength]; 
        ais.read(data);
    }

    /**
     * Создает объект из массива целых чисел
     * 
     * @param sampleSize - количество байт занимаемых сэмплом
     * @param sampleRate - частота
     * @param channels - количество каналов
     * @param samples - массив значений (данные)
     * @throws Exception если размер сэмпла меньше, чем необходимо 
     * для хранения переменной типа int 
     */
    WaveFile(int sampleSize, float sampleRate, int channels, int[] samples) throws Exception {

        if(sampleSize < INT_SIZE){
            throw new Exception("sample size < int size");
        }
        
        this.sampleSize = sampleSize;
        this.af = new AudioFormat(sampleRate, sampleSize*8, channels, true, false);
        this.data = new byte[samples.length*sampleSize];
        
        // заполнение данных
        for(int i=0; i < samples.length; i++){
            setSampleInt(i, samples[i]);
        }
        
        framesCount = data.length / (sampleSize*af.getChannels());
        ais = new AudioInputStream(new ByteArrayInputStream(data), af, framesCount);
    }

    /**
     * Возвращает формат аудио-данных
     * 
     * @return формат
     */
    public AudioFormat getAudioFormat(){
        return af;
    }

    /**
     * Возвращает копию массива байт представляющих
     * данные wave-файла
     * 
     * 
     * @return массив байт
     */
    public byte[] getData() {  
        return Arrays.copyOf(data, data.length);
    }
    
    /**
     * Возвращает количество байт которое занимает
     * один сэмпл
     * 
     * @return размер сэмпла
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * Возвращает продолжительность сигнала в секундах
     * 
     * @return продолжительность сигнала
     */
    public double getDurationTime() {
        return getFramesCount() / getAudioFormat().getFrameRate();
    }
    
    /**
     * Возвращает количество фреймов (кадров) в файле 
     * 
     * @return количество фреймов
     */
    public long getFramesCount(){
        return framesCount;
    }
    
    /**
     * Сохраняет объект WaveFile в стандартный файл формата WAVE
     * 
     * @param file
     * @throws IOException
     */
    public void saveFile(File file) throws IOException{
        AudioSystem.write( new AudioInputStream(new ByteArrayInputStream(data), 
                af, framesCount), AudioFileFormat.Type.WAVE, file); 
    }
    
    /**
     * Возвращает значение сэмпла по порядковому номеру. Если данные
     * записаны в 2 канала, то необходимо учитывать, что сэмплы левого и 
     * правого канала чередуются. Например, сэмпл под номером один это
     * первый сэмпл левого канала, сэмпл номер два это первый сэмпл правого 
     * канала, сэмпл номер три это второй сэмпл левого канала и т.д..
     * 
     * @param sampleNumber - номер сэмпла, начиная с 0
     * @return значение сэмпла
     */
    public int getSampleInt(int sampleNumber) {
    
        if(sampleNumber < 0 || sampleNumber >= data.length/sampleSize){
            throw new IllegalArgumentException(
                    "sample number is can't be < 0 or >= data.length/"
                     + sampleSize);
        }
        
        // массив байт для представления сэмпла 
        // (в данном случае целого числа)
        byte[] sampleBytes = new byte[sampleSize];
        
        // читаем из данных байты которые соответствуют
        // указанному номеру сэмпла
        for(int i=0; i < sampleSize; i++){
            sampleBytes[i] = data[sampleNumber * sampleSize + i];
        }
        
        // преобразуем байты в целое число
        int sample = ByteBuffer.wrap(sampleBytes)
                .order(ByteOrder.LITTLE_ENDIAN).getInt();
    
        return sample;
    }
    
    /**
     * Устанавливает значение сэмпла 
     * 
     * @param sampleNumber - номер сэмпла
     * @param sampleValue - значение сэмпла
     */
    public void setSampleInt(int sampleNumber, int sampleValue){
    
        // представляем целое число в виде массива байт
        byte[] sampleBytes = ByteBuffer.allocate(sampleSize).
            order(ByteOrder.LITTLE_ENDIAN).putInt(sampleValue).array();

        // последовательно записываем полученные байты
        // в место, которое соответствует указанному
        // номеру сэмпла
            for(int i=0; i < sampleSize; i++){
                data[sampleNumber * sampleSize + i] = sampleBytes[i];
            }
        }
    }