package cr.util.user;

import cr.ui.comp.UserList;

import java.io.Serial;
import java.util.HashSet;

/**
 * @author Bobbywang
 * @date 2021-10-14 21:32
 */
public final class UserManager implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 995534364L;

    private final HashSet<User> users;
    private UserInfo owner;
    private final HashSet<User> admins=new HashSet<>();
    private transient UserList list=null;

    public UserManager(){
        users=new HashSet<>();
    }
    public void addUser(User user){
        user.group=this;
        users.add(user);
        if (list!=null){
            list.addUser(user);
        }
    }
    public void removeUser(User user){
        users.remove(getByInfo(user.getInfo()));
        if (list!=null){
            list.removeUser(user);
        }
    }
    public User getByInfo(UserInfo info){
        for (User user:users){
            if (user.getInfo().equals(info))
                return user;
        }
        return null;
    }
    public int size(){
        return users.size();
    }
    public void clear(){
        users.clear();
        owner=null;
        admins.clear();
    }

    public HashSet<User> getUsers() {
        return users;
    }
    public void setOwner(UserInfo info){
        owner=info;
    }
    public void addAdmin(UserInfo info){
        admins.add(getByInfo(info));
    }
    public void setDefault(UserInfo info){
        admins.remove(getByInfo(info));
    }
    public boolean isOwner(UserInfo user){
        return owner.equals(user);
    }
    public boolean isAdmin(UserInfo info){
        return admins.contains(getByInfo(info)) || info.equals(owner);
    }
    public boolean contains(UserInfo info){
        return users.contains(getByInfo(info));
    }
    public boolean containsName(String name){
        return getByName(name)!=null;
    }

    public void setUserList(UserList list) {
        this.list = list;
        list.addAll(users);
    }
    public User getByName(String name){
        for (User user:users){
            if (user.getName().equals(name))
                return user;
        }
        return null;
    }
}
