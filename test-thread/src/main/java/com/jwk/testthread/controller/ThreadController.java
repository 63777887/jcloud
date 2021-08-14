package com.jwk.testthread.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;

/**
 * @program: parent
 * @author: jwk
 * @create: {MONTH}-{HOUR}:2021/4/2--下午9:55
 **/
@RestController
public class ThreadController {


  @GetMapping("/setRedis")
  public String setRedis(@RequestParam("value") String value, @RequestParam("key") String key) {
//        new ThreadPoolExecutor();
//        new Thread();
//        new ReentrantReadWriteLock();
//        new Hashtable<>()
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

    return null;
  }

  public static void main(String[] args) {
//        for (int i = 0; i < 35; i++) {
//            System.out.println(31 & i);
//        }
    ArrayList<Object> objects = new ArrayList<>();
//        new HashSet<>()
    int n = 33 - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    System.out.println(((n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1));
  }
}
