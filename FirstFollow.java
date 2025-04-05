import java.util.*;
public class FirstFollow {
    static Map<String, List<String>> prods = new HashMap<>();
    static Map<String, Set<String>> first = new HashMap<>();
    static Map<String, Set<String>> follow = new HashMap<>();
    static Set<String> nonT = new HashSet<>();
    static String start = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of Production rules: ");
        int n = Integer.parseInt(sc.nextLine());
        System.out.println("Enter rules: ");
        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("->");
            String lhs = parts[0].trim();
            if (i == 0) start = lhs;
            nonT.add(lhs);
            List<String> alts = new ArrayList<>();
            for (String alt : parts[1].split("/"))
                alts.add(alt.trim());
            prods.put(lhs, alts);
        }
        for (String nt : nonT) {
            first.put(nt, new HashSet<>());
            follow.put(nt, new HashSet<>());
        }
        for (String nt : nonT) computeFirst(nt);
        follow.get(start).add("$");
        boolean changed;
        do {
            changed = false;
            for (String A : prods.keySet()) {
                for (String rhs : prods.get(A)) {
                    List<String> symbols = new ArrayList<>();
                    for (int i = 0; i < rhs.length(); i++)
                        symbols.add(String.valueOf(rhs.charAt(i)));
                    for (int i = 0; i < symbols.size(); i++) {
                        String B = symbols.get(i);
                        if (nonT.contains(B)) {
                            int sizeBefore = follow.get(B).size();
                            Set<String> firstBeta = new HashSet<>();
                            boolean betaEps = true;
                            for (int j = i + 1; j < symbols.size(); j++) {
                                String sym = symbols.get(j);
                                Set<String> fs = nonT.contains(sym) ? first.get(sym) : new HashSet<>(Arrays.asList(sym));
                                firstBeta.addAll(withoutEps(fs));
                                if (!fs.contains("∈")) { betaEps = false; break; }
                            }
                            if (i == symbols.size() - 1 || betaEps)
                                firstBeta.addAll(follow.get(A));
                            follow.get(B).addAll(firstBeta);
                            if (follow.get(B).size() > sizeBefore)
                                changed = true;
                        }
                    }
                }
            }
        } while (changed);
        
        System.out.println("\nFirst and Follow-");
        System.out.println("First Sets:");
        for (String nt : nonT)
            System.out.println("First(" + nt + ") = " + first.get(nt));
        System.out.println("\nFollow Sets:");
        for (String nt : nonT)
            System.out.println("Follow(" + nt + ") = " + follow.get(nt));
        sc.close();
    }

    static Set<String> withoutEps(Set<String> s) {
        Set<String> res = new HashSet<>(s);
        res.remove("∈");
        return res;
    }

    static Set<String> computeFirst(String X) {
        Set<String> res = first.get(X);
        if (!res.isEmpty()) return res;
        for (String rhs : prods.get(X)) {
            Set<String> alt = new HashSet<>();
            if (rhs.equals("∈"))
                alt.add("∈");
            else {
                boolean allEps = true;
                for (int i = 0; i < rhs.length(); i++) {
                    String sym = String.valueOf(rhs.charAt(i));
                    Set<String> fs = nonT.contains(sym) ? computeFirst(sym) : new HashSet<>(Arrays.asList(sym));
                    alt.addAll(withoutEps(fs));
                    if (!fs.contains("∈")) { allEps = false; break; }
                }
                if (allEps) alt.add("∈");
            }
            res.addAll(alt);
        }
        return res;
    }
}
