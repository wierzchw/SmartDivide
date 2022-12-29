import java.time.LocalDateTime;

public class Transaction{
    private double amount;
    private Member debtor;
    private Member creditor;
    private LocalDateTime time;

    public Transaction(double amount, Member debtor, Member creditor, LocalDateTime time) {
        this.amount = amount;
        this.debtor = debtor;
        this.creditor = creditor;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", debtor=" + debtor +
                ", creditor=" + creditor +
                ", time=" + time +
                '}';
    }

    public double getAmount() {
        return amount;
    }

    public Member getDebtor() {
        return debtor;
    }

    public Member getCreditor() {
        return creditor;
    }

    public LocalDateTime getTime() {
        return time;
    }
}

