package labb6;

public class User implements UserMethods {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String userDonate() {
        return "Wow! " + name + " donated! Thank you!";
    }

    public String userCelebrate() {
        return name + " wants to celebrate good times, come on!";
    }

}
