
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;


public class AdjMatrixDigraph {
    private int V = 0;
    private int E = 0;
    private double[][] adj = new double[0][0];
    private List<Member> members = new ArrayList<>();
    private Map<Member, Double> negatives;
    private Map<Member, Double> positives;

    // number of vertices and edges
    public int V() {
        return V;
    }

    public int E() {
        return E;
    }
    public void addMember(Member a){
        V += 1;
        members.add(a);
        double[][] newAdj = new double[V][V];
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                newAdj[i][j] = adj[i][j];
            }
        }
        adj = newAdj;
    }


    // add directed edge v->w
    public void addEdge(Member from, Member to, double amount) {
        int v = members.indexOf(from);
        int w = members.indexOf(to);
        adj[v][w] += amount;
    }

    public void createSinkSourceLists(){
        negatives = new HashMap<>();
        positives = new HashMap<>();
        for (int i = 0; i < adj.length; i++) {
            double sum = 0;
            for (int j = 0; j < adj.length; j++) {
                sum += adj[j][i] - adj[i][j];
            }
            if (sum<0) {
                negatives.put(members.get(i), abs(sum));
            }
            else if(sum>0){
                positives.put(members.get(i), sum);
            }
        }
    }

    public Map<Double, Member[]> minTransfers() {


//        Double [] debt = new Double[owes.values().size()];
//        int i=0;
//        for (Double v : owes.values()){
//            debt[i++] = v;
//        }
        createSinkSourceLists();
        ReturnTypeForDfs result = dfs(negatives, positives);
        return result.transactions;
    }

    int minCount = Integer.MAX_VALUE;

    private class ReturnTypeForDfs{
        public int count;
        public Map<Double, Member[]> transactions;

        public ReturnTypeForDfs(int count, Map<Double, Member[]> transactions) {
            this.count = count;
            this.transactions = transactions;
        }

    }

    private ReturnTypeForDfs dfs(Map<Member, Double> negatives, Map<Member, Double> positives) {
//        while (start < debt.length && debt[start] == 0) ++start;

        if (negatives.size() + positives.size() == 0) return new ReturnTypeForDfs(0, new HashMap<>());
//        int res = Integer.MAX_VALUE;
        Member neg = (Member) negatives.keySet().stream().toArray()[0];
        int count = Integer.MAX_VALUE;
        Map<Double, Member[]> transactions = new HashMap<>();
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
                ret.transactions.put(abs(transferred), fromTo);
                transactions = ret.transactions;
            }
            else if(transactions.size()<count+1){
                transactions.put(abs(transferred), fromTo);
            }

        }

        return new ReturnTypeForDfs(count + 1, transactions);
    }


    @Override
    public String toString() {
        return "AdjMatrixDigraph{" +
                "V=" + V +
                ", members=" + members +
                ", negatives=" + negatives +
                ", positives=" + positives +
                '}';
    }
}
