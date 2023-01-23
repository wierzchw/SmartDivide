package backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.*;


public class Bill implements Serializable {
    //number of vertices
    private String title;
    private int V = 0;

    //map keeping track of debts
    private Map<Member, BigDecimal> debtList = new HashMap<>();

    private List<Member> members = new ArrayList<>();
    private Map<Member, BigDecimal> negatives;
    private Map<Member, BigDecimal> positives;
    private List<Transaction> transactionHistory = new ArrayList<>();
    private Map<Member[], BigDecimal> solution;

    private static int refoundCounter = 1;

    public Bill(String title) {
        this.title = title;
    }

    public void addMember(Member a){
        V += 1;
        members.add(a);
        debtList.put(a, BigDecimal.valueOf(0));
    }


    // add directed edge v->w
    public void addDebt(String debtTitle, BigDecimal amount, Member debtor, Member creditor) {
        BigDecimal debtorDebt = debtList.get(debtor);
        BigDecimal creditorDebt = debtList.get(creditor);
        debtList.replace(debtor, debtorDebt.subtract(amount));
        debtList.replace(creditor, creditorDebt.add(amount));
        transactionHistory.add(new Transaction(amount, debtor, creditor, LocalDateTime.now(), debtTitle));
    }
    private void addDebtForTime(String debtTitle, BigDecimal amount, Member debtor, Member creditor, LocalDateTime time) {
        BigDecimal debtorDebt = debtList.get(debtor);
        BigDecimal creditorDebt = debtList.get(creditor);
        debtList.replace(debtor, debtorDebt. subtract(amount));
        debtList.replace(creditor, creditorDebt.add(amount));
        transactionHistory.add(new Transaction(amount, debtor, creditor, time, debtTitle));
    }




    //creates 2 maps:
// members in debt with absolute value of debt (negatives)
// members that are creditors with amount they lent
    private void createPosNegMaps(){
        negatives = new HashMap<>();
        positives = new HashMap<>();
        for (Member member: debtList.keySet().stream().toList()) {

            if (debtList.get(member).compareTo(BigDecimal.valueOf(0))<0) {
                negatives.put(member, debtList.get(member).negate());
            }
            else if(debtList.get(member).compareTo(BigDecimal.valueOf(0))>0){
                positives.put(member, debtList.get(member));
            }
        }
    }


    // generates hashmap:
// key: Member[] - [debtor, creditor]
// val: double - amount
    public void minTransfers() {
        createPosNegMaps();
        ReturnTypeForSolveBill result = solveBill(negatives, positives);
        solution = result.transactions;
    }

    public void removeMember(Member selectedMember) {
        members.remove(selectedMember);
        debtList.remove(selectedMember);
    }

    private class ReturnTypeForSolveBill{
        public int count;
        public Map<Member[], BigDecimal> transactions;

        public ReturnTypeForSolveBill(int count, Map<Member[], BigDecimal> transactions) {
            this.count = count;
            this.transactions = transactions;
        }

    }

    // main algorithm, returns ReturnTypeForDfs with:
    //count - minimal number of transactions to settle debt
    //transactions - HashMap of transactions needed to settle debt in count transactions
    private ReturnTypeForSolveBill solveBill(Map<Member, BigDecimal> negatives, Map<Member, BigDecimal> positives) {
        if (negatives.size() + positives.size() == 0) return new ReturnTypeForSolveBill(0, new HashMap<>());
        Member neg = (Member) negatives.keySet().stream().toArray()[0];
        int count = Integer.MAX_VALUE;
        Map<Member[], BigDecimal> transactions = new HashMap<>();
        List<Member> positivesKeys = positives.keySet().stream().toList();
        for (Member pos : positivesKeys) {
            BigDecimal posNumber = positives.get(pos);
            BigDecimal negNumber = negatives.get(neg);
            Map<Member, BigDecimal> newPositives = new HashMap<>();
            Map<Member, BigDecimal> newNegatives = new HashMap<>();
            newNegatives.putAll(negatives);
            newPositives.putAll(positives);
            newNegatives.remove(neg);
            newPositives.remove(pos);

            Member[] fromTo = new Member[2];
            fromTo[0] = neg;
            fromTo[1] = pos;
            BigDecimal difference = negNumber.subtract(posNumber);
            BigDecimal transferred = negNumber.min(posNumber);

            if (difference.compareTo(BigDecimal.valueOf(0)) > 0) {
                newNegatives.put(neg, difference);
            } else if (difference.compareTo(BigDecimal.valueOf(0)) < 0) {
                newPositives.put(pos, difference.negate());
            }
            ReturnTypeForSolveBill ret = solveBill(newNegatives, newPositives);
            if(ret.count < count){
                count = ret.count;
                ret.transactions.put(fromTo, transferred.abs());
                transactions = ret.transactions;
            }
            else if(transactions.size()<count+1){
                transactions.put(fromTo, transferred.abs());
            }

        }

        return new ReturnTypeForSolveBill(count + 1, transactions);
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

    public void addGroupDebt(String debtTitle, BigDecimal amount, Member creditor, Member... debtors){
        BigDecimal perMember = amount.divide(BigDecimal.valueOf(debtors.length + 1), 2, RoundingMode.FLOOR);
        BigDecimal roundingError = amount.subtract(perMember.multiply(BigDecimal.valueOf(debtors.length+1)));

        for (Member debtor: debtors) {
            if (roundingError.compareTo(BigDecimal.valueOf(0)) != 0){
                addDebt(debtTitle, perMember.add(BigDecimal.valueOf(0.01)), debtor, creditor);
                roundingError = roundingError.subtract(BigDecimal.valueOf(0.01));
            }
            else{
                addDebt(debtTitle, perMember, debtor, creditor);
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
            addDebtForTime(trans.getTitle(), trans.getAmount(), trans.getDebtor(), trans.getCreditor(), trans.getTime());
        }

    }
    public List<Transaction> transactionHistoryForMember (Member a){
        List<Transaction> result = new ArrayList<>();
        for (Transaction trans: transactionHistory){
            if (trans.getDebtor() == a || trans.getCreditor() == a){
                result.add(trans);
            }
        }
        return result;
    }
    public void settleDebtFromSolution(Member debtor, Member creditor){
        Member[] input = new Member[2];
        input[0] = debtor;
        input[1] = creditor;
        BigDecimal amount;
        for (Member[] dc: solution.keySet().stream().toList()) {
            if (dc[0]==input[0] && dc[1] == input[1]){
                amount = solution.remove(dc);
                addDebt("zwrot" + refoundCounter, amount, creditor, debtor);
                refoundCounter+=1;
                return;
            }
        }
        return;
    }

    public int getV() {
        return V;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public Map<Member, BigDecimal> getDebtList() {
        return debtList;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public Map<Member[], BigDecimal> getSolution() {
        return solution;
    }
}