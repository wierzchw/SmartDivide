package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serialize implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    List<Bill> bills;
    List<Member> members;

    public Serialize(ArrayList<Bill> bills, ArrayList<Member> members){
        this.bills = bills;
        this.members = members;
    }

    public Serialize (String file) throws Exception{
        Serialize d = readState(file);
        this.bills = d.bills;
        this.members = d.members;
    }

    private Serialize readState(String file) throws Exception{
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (Serialize) ois.readObject();
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
