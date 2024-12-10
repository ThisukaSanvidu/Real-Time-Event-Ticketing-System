package com.thisuka.rtets.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to store system logs from vendors and customers
 * All logs stored in-memory for demonstration.
 */
@Service
public class LogService{

    private final List<String> logs = new ArrayList<>();


    public synchronized void addLog(String message){

        logs.add(message);
    }

    public synchronized List<String> getLogs(){

        return new ArrayList<>(logs);
    }

    public synchronized void clearLogs(){

        logs.clear();
    }
}
