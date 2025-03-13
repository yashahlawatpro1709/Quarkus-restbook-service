package org.agoncal.quarkus.starting;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class UserService {
    private final Map<String,String> users=new HashMap<>();
    public UserService(){
        users.put("user1","password1");
        users.put("user2","password2");
        users.put("user3","password3");
    }
    public boolean validateUser(String user,String password){
        return users.containsKey(user) && users.get(user).equals(password);
    }
}
