
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.*;


public class Bill {
    //number of vertices
    private String title;
    private int V = 0;

    //adjacency matrix keeping track of debts
    private Map<Member, Double> debtList = new HashMap<>();

    private List<Member> members = new ArrayList<>();
    private Map<Member, Double> negatives;
    private Map<Member, Double> positives;
    private List<Transaction> transactionHistory = new ArrayList<>();

    public Bill(String title) {
        this.title = title;
    }

    public void addMember(Member a){
        V += 1;
        members.add(a);
        debtList.put(a, (double) 0);
    }


    // add directed edge v->w
    public void addDebt(double amount, Member debtor, Member creditor) {
        double debtorDebt = debtList.get(debtor);
        double creditorDebt = debtList.get(creditor);
        debtList.replace(debtor, debtorDebt - amount);
        debtList.replace(creditor, creditorDebt + amount);
        transactionHistory.add(new Transaction(amount, debtor, creditor, LocalDateTime.now()));
    }
    public void addDebtForTime(double amount, Member debtor, Member creditor, LocalDateTime time) {
        double debtorDebt = debtList.get(debtor);
        double creditorDebt = debtList.get(creditor);
        debtList.replace(debtor, debtorDebt - amount);
        debtList.replace(creditor, creditorDebt + amount);
        transactionHistory.add(new Transaction(amount, debtor, creditor, time));
    }




//creates 2 maps:
// members in debt with absolute value of debt (negatives)
// members that are creditors with amount they lent
    public void createPosNegLists(){
        negatives = new HashMap<>();
        positives = new HashMap<>();
        for (Member member: debtList.keySet().stream().toList()) {

            if (debtList.get(member)<0) {
                negatives.put(member, -debtList.get(member));
            }
            else if(debtList.get(member)>0){
                positives.put(member, debtList.get(member));
            }
        }
    }


// generates hashmap:
// key: Member[] - [debtor, creditor]
// val: double - amount
    public Map<Member[], Double> minTransfers() {
        createPosNegLists();
        ReturnTypeForDfs result = dfs(negatives, positives);
        return result.transactions;
    }

    private class ReturnTypeForDfs{
        public int count;
        public Map<Member[], Double> transactions;

        public ReturnTypeForDfs(int count, Map<Member[], Double> transactions) {
            this.count = count;
            this.transactions = transactions;
        }

    }

   // main algorithm, returns ReturnTypeForDfs with:
   //count - minimal number of transactions to settle debt
   //transactions - HashMap of transactions needed to settle debt in count transactions
    private ReturnTypeForDfs dfs(Map<Member, Double> negatives, Map<Member, Double> positives) {
        if (negatives.size() + positives.size() == 0) return new ReturnTypeForDfs(0, new HashMap<>());
        Member neg = (Member) negatives.keySet().stream().toArray()[0];
        int count = Integer.MAX_VALUE;
        Map<Member[], Double> transactions = new HashMap<>();
        List<Member> positivesKeys = positives.keySet().stream().toList();
        for (Member pos : positivesKeys) {
            Double posDouble = positives.get(pos);
            Double negDouble = negatives.get(neg);
            Map<Member, Double> newPositives = new HashMap<>();
            Map<Member, Double> newNegatives = new HashMap<>();
            newNegatives.putAll(negatives);
            newPositives.putAll(positives);
            newNegatives.remove(neg);
            newPositives.remove(pos);

            Member[] fromTo = new Member[2];
            fromTo[0] = neg;
            fromTo[1] = pos;
            double difference = negDouble - posDouble;
            double transferred = min(negDouble, posDouble);

            if (difference > 0) {
                newNegatives.put(neg, difference);
            } else if (difference < 0) {
                newPositives.put(pos, -difference);
            }
            ReturnTypeForDfs ret = dfs(newNegatives, newPositives);
            if(ret.count < count){
                count = ret.count;
                ret.transactions.put(fromTo, abs(transferred));
                transactions = ret.transactions;
            }
            else if(transactions.size()<count+1){
                transactions.put(fromTo, abs(transferred));
            }

        }

        return new ReturnTypeForDfs(count + 1, transactions);
    }


    @Override
    public String toString() {
        return "Bill{" +
                "V=" + V +
                ", members=" + members +
                ", negatives=" + negatives +
                ", positives=" + positives +
                '}';
    }

    public void addGroupDebt(Double amount, Member creditor, Member... debtors){
        double perMember = amount/debtors.length;
        perMember = floor(perMember*100)/100;
        for (Member debtor: debtors) {
            addDebt(perMember, debtor, creditor);
        }
        double roundingError = amount - perMember*debtors.length;

        if (roundingError != 0){
            for (int i = 0; i < roundingError*100; i++) {
                addDebt(0.01, debtors[i%debtors.length], creditor);
            }
        }
    }

    public void mergeBills(Bill otherBill){
        for (Member member : otherBill.getMembers()){
            if (!members.contains(member)){
                addMember(member);
            }
        }
        for (Transaction trans : otherBill.getTransactionHistory()){
            addDebtForTime(trans.getAmount(), trans.getDebtor(), trans.getCreditor(), trans.getTime());
        }
        title = title + " + " + otherBill.getTitle();

    }

    public int getV() {
        return V;
    }

    public String getTitle() {
        return title;
    }

    public Map<Member, Double> getDebtList() {
        return debtList;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}