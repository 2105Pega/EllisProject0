package com.revature.banking;

import com.revature.banking.services.Input;
import com.revature.banking.services.Persistence;
import com.revature.banking.services.PropertiesLoader;

public class Driver {
    public static void main(String args[]) {
        String propertiesFileName = "src/main/resources/jdbcbank.properties";
        PropertiesLoader.load(propertiesFileName);
        Persistence p = new Persistence();
        Input i = new Input();
        UserInterface ui = new UserInterface(p, i);
        ui.run();
        i.close();
    }
}
