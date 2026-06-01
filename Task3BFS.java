// Запуск: javac Task3BFS.java && java -Dfile.encoding=UTF-8 Task3BFS
import java.util.*;

/**
 * ЛАБОРАТОРНА РОБОТА №3
 * ЗАВДАННЯ 3: Реалізація та трасування BFS
 */
public class Task3BFS {

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
    // 3.1  BFS базовий
    // ──────────────────────────────────────────────
    static List<Integer> bfs(Graph g, int start) {
        boolean[] visited = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);
            for (int u : g.adj.get(v)) {
                if (!visited[u]) {
                    visited[u] = true;
                    queue.add(u);
                }
            }
        }
        return order;
    }

    // ──────────────────────────────────────────────
    // 3.2  BFS з покроковою таблицею
    // ──────────────────────────────────────────────
    static List<Integer> bfsWithTrace(Graph g, int start) {
        System.out.println("\n┌──────┬────────────┬────────────────────────────────┐");
        System.out.println("│ Крок │  Вершина   │  Стан черги (після кроку)      │");
        System.out.println("├──────┼────────────┼────────────────────────────────┤");

        boolean[] visited = new boolean[g.n];
        List<Integer> order = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        int step = 0;

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);
            step++;

            for (int u : g.adj.get(v)) {
                if (!visited[u]) {
                    visited[u] = true;
                    queue.add(u);
                }
            }
            System.out.printf("│  %2d  │     %d      │  черга: %-24s│%n",
                    step, v, queue.toString());
        }
        System.out.println("└──────┴────────────┴────────────────────────────────┘");
        return order;
    }

    // ──────────────────────────────────────────────
    // 3.4  Найкоротший шлях (BFS)
    // ──────────────────────────────────────────────
    static int[] bfsShortestPath(Graph g, int src, int dst) {
        /**
         * Повертає масив int[] де [0] = довжина, [1..] = вершини шляху.
         * Якщо шляху немає — повертає {-1}.
         */
        if (src == dst) return new int[]{0, src};

        int[] parent = new int[g.n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[g.n];
        Queue<Integer> queue = new LinkedList<>();

        visited[src] = true;
        queue.add(src);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u : g.adj.get(v)) {
                if (!visited[u]) {
                    visited[u] = true;
                    parent[u] = v;
                    if (u == dst) {
                        // Відновлюємо шлях
                        List<Integer> path = new ArrayList<>();
                        int cur = dst;
                        while (cur != -1) {
                            path.add(0, cur);
                            cur = parent[cur];
                        }
                        int[] result = new int[path.size() + 1];
                        result[0] = path.size() - 1; // довжина в ребрах
                        for (int i = 0; i < path.size(); i++) result[i + 1] = path.get(i);
                        return result;
                    }
                    queue.add(u);
                }
            }
        }
        return new int[]{-1}; // недосяжно
    }

    // ──────────────────────────────────────────────
    // Відстані BFS від вершини до всіх інших
    // ──────────────────────────────────────────────
    static int[] bfsDistances(Graph g, int start) {
        int[] dist = new int[g.n];
        Arrays.fill(dist, -1);
        Queue<Integer> queue = new LinkedList<>();
        dist[start] = 0;
        queue.add(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u : g.adj.get(v)) {
                if (dist[u] == -1) {
                    dist[u] = dist[v] + 1;
                    queue.add(u);
                }
            }
        }
        return dist;
    }

    // ──────────────────────────────────────────────
    // Хелпер: список → рядок зі стрілками
    // ──────────────────────────────────────────────
    static String toArrowString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) sb.append(" → ");
        }
        return sb.toString();
    }

    static String pathToString(int[] result) {
        if (result[0] == -1) return "недосяжно";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < result.length; i++) {
            sb.append(result[i]);
            if (i < result.length - 1) sb.append(" → ");
        }
        return sb.toString();
    }

    // ──────────────────────────────────────────────
    // MAIN
    // ──────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  ЛР №3 | ЗАВДАННЯ 3: BFS");
        System.out.println("==================================================");

        // Граф із Завдання 1
        Graph g = new Graph(7);
        int[][] edges = {{0,1},{0,2},{1,3},{1,4},{2,5},{2,6}};
        for (int[] e : edges) g.addEdge(e[0], e[1]);

        // ── 3.1 базовий BFS ───────────────────────
        List<Integer> order = bfs(g, 0);
        System.out.println("\n▶ 3.1 BFS від вершини 0:");
        System.out.println("  Порядок відвідування: " + toArrowString(order));

        // ── 3.2 трасування ────────────────────────
        System.out.println("\n▶ 3.2 Покрокове трасування BFS (від вершини 0):");
        List<Integer> traced = bfsWithTrace(g, 0);

        // ── 3.3 підсумковий порядок ───────────────
        System.out.println("\n▶ 3.3 Порядок відвідування: " + toArrowString(traced));

        // ── Відстані до всіх вершин ───────────────
        System.out.println("\n▶ Відстані від вершини 0 до всіх інших:");
        int[] dist = bfsDistances(g, 0);
        for (int v = 0; v < g.n; v++) {
            String bar = "●".repeat(dist[v]);
            System.out.printf("    0 → %d: %d ребер  %s%n", v, dist[v], bar);
        }

        // ── 3.4 найкоротший шлях 0 → 6 ───────────
        System.out.println("\n▶ 3.4 Найкоротший шлях від 0 до 6:");
        int[] result = bfsShortestPath(g, 0, 6);
        System.out.printf("  Довжина: %d ребер%n", result[0]);
        System.out.printf("  Шлях:    %s%n", pathToString(result));

        // Додаткові пари
        System.out.println("\n▶ Додаткові шляхи:");
        int[][] pairs = {{0,3},{3,6},{1,5}};
        for (int[] p : pairs) {
            int[] r = bfsShortestPath(g, p[0], p[1]);
            System.out.printf("  %d → %d: довжина=%d, шлях=%s%n",
                    p[0], p[1], r[0], pathToString(r));
        }

        // ── Тест на незв'язному графі ─────────────
        System.out.println("\n" + "─".repeat(50));
        System.out.println("▶ Тест: BFS на незв'язному графі");
        System.out.println("─".repeat(50));

        Graph g2 = new Graph(6);
        for (int[] e : new int[][]{{0,1},{1,2},{3,4}}) g2.addEdge(e[0], e[1]);

        System.out.println("  Граф: 0–1–2  |  3–4  |  5 (ізольована)");

        int[] r1 = bfsShortestPath(g2, 0, 5);
        System.out.printf("  Шлях 0 → 5: %s%n", pathToString(r1));

        int[] r2 = bfsShortestPath(g2, 0, 4);
        System.out.printf("  Шлях 0 → 4: %s%n", pathToString(r2));
    }
}
