package backend;

import java.util.ArrayList;

public class Holder {

    private Holder holder;

    private ArrayList<Bill> bills;
    private ArrayList<Member> members;

    public Holder() {
        bills = new ArrayList<Bill>();
        members = new ArrayList<Member>();
        members.add(new Member("Adaś"));
        members.add(new Member("Krzyś"));
        bills.add(new Bill("bill1"));
        bills.add(new Bill("bill2"));
    }

    public void addBill(Bill bill){
        bills.add(bill);
    }

    public void addMember(Member member) { members.add(member); }

    public ArrayList<Bill> getBills() {
        return bills;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }
}
