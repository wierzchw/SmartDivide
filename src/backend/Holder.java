package backend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Holder {

    private Holder holder;

    private ArrayList<Bill> bills;
    private ArrayList<Member> members;

    public Holder() {
        bills = new ArrayList<Bill>();
        members = new ArrayList<Member>();

        Member member1 = new Member("Adaś");
        Member member2 = new Member("Krzyś");
        bills.add(new Bill("bill1"));
        bills.add(new Bill("bill2"));
        bills.get(0).addMember(member1);
        bills.get(0).addMember(member2);
        bills.get(0).addDebtForTime(20, member1, member2, LocalDateTime.now());
    }

    public boolean checkBillExistence(String newBillTitle) {
        boolean sameNameBillExists = false;

        for (Bill bill : bills) {
            if (Objects.equals(bill.getTitle(), newBillTitle)) {
                sameNameBillExists = true;
                break;
            }
        }
        return sameNameBillExists;
    }

    public boolean checkMemberExistence(String newMemberName) {
        boolean sameNameMemberExists = false;

        for(Bill bill: bills) {
            for (Member member : bill.getMembers()) {
                if (Objects.equals(member.getName(), newMemberName)) {
                    sameNameMemberExists = true;
                    break;
                }
            }
        }

        for(Member member : members) {
            if (Objects.equals(member.getName(), newMemberName)) {
                sameNameMemberExists = true;
                break;
            }
        }

        return sameNameMemberExists;
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
