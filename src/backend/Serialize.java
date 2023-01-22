package backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Serialize {private static final long serialVersionUID = 1234567L;
    List<Bill> bills;
    List<Member> members;

    public Serialize(){
        this.bills = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public Serialize (String file) throws Exception{
        Serialize d = readState(file);
        this.bills = d.bills;
        this.members = d.members;
    }

    private Serialize readState(String file) throws Exception{
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            Serialize d  = (Serialize) ois.readObject();
            return d;
        }
    }


    public void saveState(String file) throws Exception{
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(this);
        }
    }

    public List<Bill> getBills() {
        return bills;
    }

    public List<Member> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "Dane{" +
                "rachuneks=" + bills +
                ", osobas=" + members +
                '}';
    }
}
