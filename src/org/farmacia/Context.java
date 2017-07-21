package org.farmacia;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final static Context instance = new Context();
    private final Map<String,Object> global = new HashMap<>();
    
    public static Context getInstance(){
        return instance;
    }
    
    public Map<String, Object> getGlobal(){
        return global;
    }
}
