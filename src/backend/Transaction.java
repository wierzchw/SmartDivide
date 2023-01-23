package backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private BigDecimal amount;
    private Member debtor;
    private Member creditor;
    private LocalDateTime time;
    private String title;

    public Transaction(BigDecimal amount, Member debtor, Member creditor, LocalDateTime time, String title) {
        this.amount = amount;
        this.debtor = debtor;
        this.creditor = creditor;
        this.time = time;
        this.title = title;
    }

    @Override
    public String toString() {
        return "backend.Transaction{" +
                "title=" + title +
                "amount=" + amount +
                ", debtor=" + debtor +
                ", creditor=" + creditor +
                ", time=" + time +
                '}';
    }

    public BigDecimal getAmount() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

