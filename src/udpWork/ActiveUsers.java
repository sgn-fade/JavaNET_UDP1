package udpWork;

import java.io.Serializable;
import java.util.ArrayList;

public class ActiveUsers implements Serializable {
    private ArrayList<User> users;

    public ActiveUsers() {
        users = new ArrayList<>();
    }
    public void add(User user){
        users.add(user);
    }
    public boolean isEmpty(){
        return users.isEmpty();
    }
    public int size(){
        return users.size();
    }
    public boolean contains(User user){
        return users.contains(user);
    }
    public User get(int index){
        return users.get(index);
    }

    @Override
    public String toString() {
        return "ActiveUsers{" +
                "users=" + users +
                '}';
    }
    public void remove(int index){
        users.remove(index);
    }
    public void remove(User user){
        users.remove(user);
    }
    public void clear(){
        users.clear();
    }

}
