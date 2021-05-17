package com.revature.banking.services;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Input {
    private static Scanner sc;
    private Logger logger = LogManager.getLogger(this.getClass());

    public Input() {
        if (sc == null) {
            sc = new Scanner(System.in);
        }
    }

    public String getLine() {
        return sc.nextLine();
    }

    public double getDouble() {
        try {
            return sc.nextDouble();
        } catch(Exception e) {
            logger.debug("invalid input for double");
        }
        return -1;
    }

    public void close() {
        sc.close();
    }
}
