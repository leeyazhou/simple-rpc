/**
 * 
 */
package com.github.leeyazhou.example.service;

/**
 * @author leeyazhou
 *
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String echo) {
        return echo;
    }

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

}
