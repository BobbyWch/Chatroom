package cr.util.user;

import cr.LocalEnum;
import cr.ui.comp.UserList;

import java.io.Serial;
import java.util.ArrayList;

/**
 * @author Bobbywang
 * @date 2021-10-14 21:32
 */
public final class UserManager implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 995534364L;

    private final ArrayList<User> users;
    private UserInfo owner;
    private final ArrayList<User> admins=new ArrayList<>();
    private transient UserList list=null;

    public UserManager(){
        users=new ArrayList<>();
    }
    public void addUser(User user){
        if (owner!=null){
            if (user.getInfo().equals(owner))
                user.setPermission(LocalEnum.Permission_OWNER);
        }else if (!admins.isEmpty()){
            if(admins.contains(user))
                user.setPermission(LocalEnum.Permission_ADMIN);
        }else {
            user.setPermission(LocalEnum.Permission_DEFAULT);
        }
        users.add(user);
        if (list!=null){
            list.addUser(user);
        }
    }
    public void removeUser(User user){
        users.remove(user);
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

    public ArrayList<User> getUsers() {
        return users;
    }
    public void setOwner(UserInfo info){
        User user=getByInfo(info);
        owner=info;
        if (user!=null){
            user.setPermission(LocalEnum.Permission_OWNER);
        }
    }
    public void addAdmin(UserInfo info){
        User user=getByInfo(info);
        user.setPermission(LocalEnum.Permission_ADMIN);
        admins.add(getByInfo(info));
    }
    public void setDefault(UserInfo info){
        User user=getByInfo(info);
        admins.remove(getByInfo(info));
        user.setPermission(LocalEnum.Permission_DEFAULT);
    }
    public boolean isOwner(User user){
        return owner.equals(user.getInfo());
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
