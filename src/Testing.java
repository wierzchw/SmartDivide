import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Testing {
    public static void main(String[] args) {
        AdjMatrixDigraph mat = new AdjMatrixDigraph();
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
        List<Member> list = new ArrayList<Member>();
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

        for(Member member : list){
            mat.addMember(member);
        }
        mat.addEdge(a,b,40);
        mat.addEdge(b,c, 40);
        mat.addEdge(c,d, 20);
        mat.addEdge(d, a, 20);
        mat.addEdge(f,d, 50);
//        mat.addEdge(a, e, 20.25);
//        mat.addEdge(i, b, 20);
//        mat.addEdge(j, c, 44);
//        mat.addEdge(h, g, 60);

        //members - OK
        System.out.println(mat);

        //neg/pos lists - OK
        mat.createSinkSourceLists();
        System.out.println(mat);

        //number of operations - OK
        mat.minTransfers();
        System.out.println(mat.minTransfers());

        //list of operations
        Map<Double, Member[]> result = mat.minTransfers();
        for (Double amount : result.keySet().stream().toList()){
            System.out.println(result.get(amount)[0] + " wisi " + result.get(amount)[1] + " " + amount) ;
        }


    }
}
