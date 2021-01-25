package com.tdt.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by liu.min on 2018/11/7.
 */
public class SequenceBuilder {

    private final static ConcurrentHashMap<String,String> lastNoMap = new ConcurrentHashMap<>();
    private final static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();
    /**
     * 生成单号
     */
    public static String generateNo(String type){
        StringBuffer no = new StringBuffer();
        Semaphore semaphore = getSemaphore(type);
        try {
            semaphore.acquire();
            no.append(type).append(DateUtil.getAllTime());
            while (lastNoMap.containsValue(no.toString())){
                Thread.sleep(1000L);
                no.setLength(0);
                no.append(type).append(DateUtil.getAllTime());
            }
            lastNoMap.put(type, no.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return no.toString();
    }

    public static Semaphore getSemaphore(String type){
        if(!semaphoreMap.containsKey(type)){
            synchronized (SequenceBuilder.class){
                if(!semaphoreMap.containsKey(type)){
                    semaphoreMap.put(type, new Semaphore(1));
                }
            }
        }
        return semaphoreMap.get(type);
    }

    public static void main(String[] args) {
        for(int i=0;i<5;i++){
            new Thread(()->{
                String no = SequenceBuilder.generateNo("IP");
                String no2 = SequenceBuilder.generateNo("IW");
                System.out.println(no + "=====" + no2);
            }).start();
        }
    }
}
