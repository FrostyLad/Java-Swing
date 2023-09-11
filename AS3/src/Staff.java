// BRADEN JOHNSTON 20005898 - 159234 AS3 2022
public class Staff {

    private final String user, pass;  // I found an ID to be unused
    private final boolean manager;  // whether this staff member is a manager

    Staff(boolean manager, String user, String pass) {
        this.manager = manager;
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public boolean isManager() {
        return manager;
    }
}