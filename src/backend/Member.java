package backend;

public class Member {
    String name;

    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "backend.Member{" +
                "name='" + name + '\'' +
                '}';
    }
}
