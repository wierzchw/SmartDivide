package backend;

import java.io.Serializable;

public class Member implements Serializable {
    String name;

    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "backend.Member{" +
                "name='" + name + '\'' +
                '}';
    }
}
