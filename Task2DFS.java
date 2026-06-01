// Запуск: javac Task2DFS.java && java -Dfile.encoding=UTF-8 Task2DFS
import java.util.*;

/**
 * ЛАБОРАТОРНА РОБОТА №3
 * ЗАВДАННЯ 2: Реалізація та трасування DFS
 */
public class Task2DFS {

    static class Graph {
        int n;
        List<List<Integer>> adj;

        Graph(int numVertices) {
            this.n = numVertices;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }

        void addEdge(int u, int v) {
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
    }

    // ──────────────────────────────────────────────
    // 2.1  DFS рекурсивний
    // ──────────────────────────────────────────────
    static List<Integer> dfsRecursive(Graph g, int start) {
        boolean[] visited = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        dfsHelper(g, start, visited, order);
        return order;
    }

    private static void dfsHelper(Graph g, int v, boolean[] visited, List<Integer> order) {
        visited[v] = true;
        order.add(v);
        for (int u : g.adj.get(v)) {
            if (!visited[u]) {
                dfsHelper(g, u, visited, order);
            }
        }
    }

    // ──────────────────────────────────────────────
    // 2.1  DFS ітеративний (явний стек)
    // ──────────────────────────────────────────────
    static List<Integer> dfsIterative(Graph g, int start) {
        boolean[] visited = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (visited[v]) continue;
            visited[v] = true;
            order.add(v);
            // Додаємо у зворотньому порядку, щоб менший індекс йшов першим
            List<Integer> neighbors = g.adj.get(v);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                int u = neighbors.get(i);
                if (!visited[u]) stack.push(u);
            }
        }
        return order;
    }

    // ──────────────────────────────────────────────
    // 2.2  DFS з покроковою таблицею
    // ──────────────────────────────────────────────
    static List<Integer> dfsWithTrace(Graph g, int start) {
        System.out.println("\n┌──────┬────────────┬────────────────────────────────┐");
        System.out.println("│ Крок │  Вершина   │  Стан стеку (після кроку)      │");
        System.out.println("├──────┼────────────┼────────────────────────────────┤");

        boolean[] visited = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        int step = 0;

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (visited[v]) continue;
            visited[v] = true;
            order.add(v);
            step++;

            List<Integer> neighbors = g.adj.get(v);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                int u = neighbors.get(i);
                if (!visited[u]) stack.push(u);
            }

            // Відображаємо стек як список (від верху до дна)
            List<Integer> stackList = new ArrayList<>(stack);
            System.out.printf("│  %2d  │     %d      │  стек: %-25s│%n",
                    step, v, stackList.toString());
        }
        System.out.println("└──────┴────────────┴────────────────────────────────┘");
        return order;
    }

    // ──────────────────────────────────────────────
    // 2.4  Підрахунок зв'язних компонент (DFS)
    // ──────────────────────────────────────────────
    static int countComponents(Graph g) {
        boolean[] visited = new boolean[g.n];
        int count = 0;
        for (int v = 0; v < g.n; v++) {
            if (!visited[v]) {
                count++;
                dfsHelper(g, v, visited, new ArrayList<>());
            }
        }
        return count;
    }

    static List<List<Integer>> getComponents(Graph g) {
        boolean[] visited = new boolean[g.n];
        List<List<Integer>> components = new ArrayList<>();
        for (int v = 0; v < g.n; v++) {
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                dfsHelper(g, v, visited, comp);
                components.add(comp);
            }
        }
        return components;
    }

    // ──────────────────────────────────────────────
    // Хелпер: порядок → рядок зі стрілками
    // ──────────────────────────────────────────────
    static String toArrowString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) sb.append(" → ");
        }
        return sb.toString();
    }

    // ──────────────────────────────────────────────
    // MAIN
    // ──────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  ЛР №3 | ЗАВДАННЯ 2: DFS");
        System.out.println("==================================================");

        // Граф із Завдання 1
        Graph g = new Graph(7);
        int[][] edges = {{0,1},{0,2},{1,3},{1,4},{2,5},{2,6}};
        for (int[] e : edges) g.addEdge(e[0], e[1]);

        // ── 2.1 рекурсивний DFS ───────────────────
        List<Integer> recOrder = dfsRecursive(g, 0);
        System.out.println("\n▶ 2.1 Рекурсивний DFS від вершини 0:");
        System.out.println("  Порядок відвідування: " + toArrowString(recOrder));

        // ── 2.1 ітеративний DFS ───────────────────
        List<Integer> iterOrder = dfsIterative(g, 0);
        System.out.println("\n▶ 2.1 Ітеративний DFS від вершини 0:");
        System.out.println("  Порядок відвідування: " + toArrowString(iterOrder));

        // ── 2.2 трасування ────────────────────────
        System.out.println("\n▶ 2.2 Покрокове трасування DFS (від вершини 0):");
        List<Integer> traced = dfsWithTrace(g, 0);

        // ── 2.3 підсумковий порядок ───────────────
        System.out.println("\n▶ 2.3 Порядок відвідування: " + toArrowString(traced));

        // ── 2.4 зв'язні компоненти ────────────────
        System.out.println("\n" + "─".repeat(50));
        System.out.println("▶ 2.4 Підрахунок зв'язних компонент");
        System.out.println("─".repeat(50));

        // Граф A — зв'язний (з завдання)
        List<List<Integer>> comps1 = getComponents(g);
        System.out.printf("%n  Граф A (зв'язний, 7 вершин):%n");
        System.out.printf("  Компонент: %d%n", comps1.size());
        for (int i = 0; i < comps1.size(); i++)
            System.out.printf("    Компонента %d: %s%n", i + 1, comps1.get(i));

        // Граф B — незв'язний (2 компоненти)
        Graph g2 = new Graph(6);
        for (int[] e : new int[][]{{0,1},{1,2},{3,4},{4,5}}) g2.addEdge(e[0], e[1]);
        List<List<Integer>> comps2 = getComponents(g2);
        System.out.printf("%n  Граф B (незв'язний, ребра: 0–1,1–2 | 3–4,4–5):%n");
        System.out.printf("  Компонент: %d%n", comps2.size());
        for (int i = 0; i < comps2.size(); i++)
            System.out.printf("    Компонента %d: %s%n", i + 1, comps2.get(i));

        // Граф C — 1 ребро + ізольовані вершини
        Graph g3 = new Graph(5);
        g3.addEdge(0, 1);
        List<List<Integer>> comps3 = getComponents(g3);
        System.out.printf("%n  Граф C (ребро 0–1, вершини 2,3,4 ізольовані):%n");
        System.out.printf("  Компонент: %d%n", comps3.size());
        for (int i = 0; i < comps3.size(); i++)
            System.out.printf("    Компонента %d: %s%n", i + 1, comps3.get(i));
    }
}
