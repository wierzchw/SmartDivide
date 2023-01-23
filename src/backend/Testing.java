package backend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Testing {
    public static void main(String[] args) {
        Bill mat = new Bill("mat");
        Bill mat2 = new Bill("mat2");
        Member a = new Member("A");
        Member b = new Member("B");
        Member c = new Member("C");
        Member d = new Member("D");
        Member e = new Member("E");
        Member f = new Member("F");
        Member g = new Member("G");
        Member h = new Member("H");
        Member i = new Member("I");
        Member j = new Member("J");
        Member a1 = new Member("A1");
        Member a2 = new Member("A2");
        Member a3 = new Member("A3");
        List<Member> list = new ArrayList<Member>();
        List<Member> list2 = new ArrayList<Member>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);
        list.add(g);
        list.add(h);
        list.add(i);
        list.add(j);
        list2.add(a1);
        list2.add(a2);
        list2.add(a3);
        list2.add(a);

        for(Member member : list){
            mat.addMember(member);
        }
        for(Member member : list2){
            mat2.addMember(member);
        }

        mat.addDebt("1", BigDecimal.valueOf(40), a,b);
        mat.addDebt("2", BigDecimal.valueOf(40), b,c);
        mat.addDebt("3", BigDecimal.valueOf(20), c,d);
        mat2.addDebt("4", BigDecimal.valueOf(70), a, a1);
//        mat.addDebt(20, d, a);
//        mat.addDebt(50, f,d);
//        mat.addDebt(20.25, a, e);
//        mat.addDebt(20, i, b);
//        mat.addDebt(44, j, c);
//        mat.addDebt(60, h, g);

        //members - OK
        System.out.println(mat);

        //neg/pos lists - OK
        System.out.println(mat);

//        list of operations - OK
        mat.minTransfers();
        Map<Member[], BigDecimal> result = mat.getSolution();
        for (Member[] members : result.keySet().stream().toList()){
            System.out.println(members[0] + " wisi " + members[1] + " " + result.get(members)) ;
        }
        // transaction history - ok
        for (Transaction transaction: mat.getTransactionHistory()) {
            System.out.println(transaction);
        }
        System.out.println(mat.getDebtList());

        //merging
        System.out.println("-------------------");
        System.out.println("mergeBills()");
        mat.mergeBills(mat2);
        mat.minTransfers();
        System.out.println(mat);

        System.out.println("minTransfers.keySet().stream().toList()");
        mat.minTransfers();
        Map<Member[], BigDecimal> result2 = mat.getSolution();
        for (Member[] members : result2.keySet().stream().toList()) {
            System.out.println(members[0] + " wisi " + members[1] + " " + result2.get(members));
        }

        System.out.println("getTransactionHistory");
        for (Transaction transaction: mat.getTransactionHistory()) {
            System.out.println(transaction);
        }

        System.out.println("getDebtList");
        System.out.println(mat.getDebtList());

        System.out.println("getTitle");
        System.out.println(mat.getTitle());



    }
}
