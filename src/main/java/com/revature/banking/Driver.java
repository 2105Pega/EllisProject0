package com.revature.banking;

import com.revature.banking.services.Input;
import com.revature.banking.services.Persistence;

import java.io.File;

public class Driver {
    public static void main(String args[]) {
        String persistFilename = "src/main/resources/bankdata.txt";
        Persistence p;
        if (new File(persistFilename).exists()) {
            p = new Persistence(persistFilename);
        } else {
            p = new Persistence();
        }
        Input i = new Input();
        UserInterface ui = new UserInterface(p, i);
        ui.run();
        i.close();
        p.writeToFile(persistFilename);
    }
}
