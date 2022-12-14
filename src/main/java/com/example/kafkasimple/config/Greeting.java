package com.example.kafkasimple.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Greeting {

    private String msg;
    private String name;

    // standard getters, setters and constructor

    @Override
    public String toString() {
        return msg + ", " + name + "!";
    }
}
