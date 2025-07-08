import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainThr extends JFrame {

    private JTextField sizeInputField;
    private JPanel matrixPanel;
    private JButton generateButton;

    private JTextField searchInputField;
    private JButton linearSearchButton;
    private JButton hashSetSearchButton;
    private JButton mapSearchButton;
    private JButton allSearchButton;
    private JButton randomSearchButton;
    private JLabel searchResultLabel;
    private int[][] currentMatrix; // 保存目前的矩陣
    private Map<Integer, int[]> valuePositionMap; // Map搜尋用
    private StringBuilder infoHistory = new StringBuilder(); // 保留所有搜尋結果

    public MainThr() {
        setTitle("Matrix Generator");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 上方：矩陣大小與搜尋數字輸入
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sizeLabel = new JLabel("矩陣大小 n x n: ");
        sizeInputField = new JTextField(5);
        generateButton = new JButton("產生矩陣");
        topPanel.add(sizeLabel);
        topPanel.add(sizeInputField);
        topPanel.add(generateButton);
        topPanel.add(new JLabel("搜尋數字: "));
        searchInputField = new JTextField(5);
        topPanel.add(searchInputField);

        // 中間：矩陣顯示
        matrixPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(matrixPanel);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        // 右側：顯示資訊
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        searchResultLabel = new JLabel("");
        searchResultLabel.setVerticalAlignment(SwingConstants.TOP);
        infoPanel.add(searchResultLabel, BorderLayout.NORTH);
        infoPanel.setPreferredSize(new Dimension(250, 350));

        // 下方：搜尋按鈕
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linearSearchButton = new JButton("循序搜尋");
        hashSetSearchButton = new JButton("HashSet搜尋");
        mapSearchButton = new JButton("Map搜尋");
        allSearchButton = new JButton("全部比較");
        JButton clearButton = new JButton("清除結果");
        randomSearchButton = new JButton("隨機搜尋");
        bottomPanel.add(linearSearchButton);
        bottomPanel.add(hashSetSearchButton);
        bottomPanel.add(mapSearchButton);
        bottomPanel.add(allSearchButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(randomSearchButton);

        // 主畫面佈局
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件綁定
        generateButton.addActionListener(e -> generateMatrix());
        linearSearchButton.addActionListener(e -> searchNumberByMethod("linear"));
        hashSetSearchButton.addActionListener(e -> searchNumberByMethod("hashset"));
        mapSearchButton.addActionListener(e -> searchNumberByMethod("map"));
        allSearchButton.addActionListener(e -> searchNumberByMethod("all"));
        clearButton.addActionListener(e -> {
            infoHistory.setLength(0);
            searchResultLabel.setText("");
        });
        randomSearchButton.addActionListener(e -> {
            if (currentMatrix == null) {
                searchResultLabel.setText("請先產生矩陣");
                return;
            }
            int n = currentMatrix.length;
            if (n == 0) return;
            // 隨機產生一個矩陣內的值
            int i = (int)(Math.random() * n);
            int j = (int)(Math.random() * n);
            int value = currentMatrix[i][j];
            searchInputField.setText(String.valueOf(value));
            searchNumberByMethod("all");
        });
    }

    private void generateMatrix() {
        // Get the size from the text field
        String input = sizeInputField.getText();
        int n = 0;

        try {
            n = Integer.parseInt(input);
            if (n <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive integer for matrix size.");
            return;
        }

        // Clear any previous matrix display
        matrixPanel.removeAll();

        // Generate a random n x n matrix with unique values
        Set<Integer> usedNumbers = new HashSet<>();
        Random random = new Random();
        int[][] matrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int num;
                do {
                    num = random.nextInt(n * n) + 1; // Generate numbers between 1 and n*n
                } while (usedNumbers.contains(num)); // Ensure no duplicates
                matrix[i][j] = num;
                usedNumbers.add(num);
            }
        }

        // Display the matrix in the grid
        matrixPanel.setLayout(new GridLayout(n, n)); // Adjust grid layout size

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JLabel label = new JLabel(String.valueOf(matrix[i][j]), SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                matrixPanel.add(label);
            }
        }

        // 產生完矩陣後保存
        currentMatrix = matrix;
        valuePositionMap = buildValuePositionMap(matrix);

        // Refresh the panel to show the updated matrix
        matrixPanel.revalidate();
        matrixPanel.repaint();
    }

    // 方法1：循序搜尋
    private boolean linearSearch(int[][] matrix, int target) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    // 方法2：HashSet 快速查找
    private boolean hashSetSearch(int[][] matrix, int target) {
        Set<Integer> set = new HashSet<>();
        for (int[] row : matrix) {
            for (int num : row) {
                set.add(num);
            }
        }
        return set.contains(target);
    }

    // 方法3：Map 儲存數字與座標
    private Map<Integer, int[]> buildValuePositionMap(int[][] matrix) {
        Map<Integer, int[]> map = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                map.put(matrix[i][j], new int[]{i, j});
            }
        }
        return map;
    }
    // 查找數字座標
    private int[] mapSearch(Map<Integer, int[]> map, int target) {
        return map.getOrDefault(target, null);
    }

    // 新增搜尋方法選擇
    private void searchNumberByMethod(String method) {
        if (currentMatrix == null) {
            searchResultLabel.setText("請先產生矩陣");
            return;
        }
        String input = searchInputField.getText();
        int target;
        try {
            target = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            searchResultLabel.setText("請輸入有效數字");
            return;
        }
        String html = "";
        if ("linear".equals(method)) {
            long start = System.nanoTime();
            int[] pos = null;
            boolean found = false;
            outer: for (int i = 0; i < currentMatrix.length; i++) {
                for (int j = 0; j < currentMatrix[i].length; j++) {
                    if (currentMatrix[i][j] == target) {
                        pos = new int[]{i, j};
                        found = true;
                        break outer;
                    }
                }
            }
            long end = System.nanoTime();
            double ms = (end - start) / 1_000_000.0;
            if (found) {
                html = String.format("循序搜尋: 找到 (%d, %d) (%.3f ms)", pos[0], pos[1], ms);
            } else {
                html = String.format("循序搜尋: 找不到 (%.3f ms)", ms);
            }
        } else if ("hashset".equals(method)) {
            long start = System.nanoTime();
            int[] pos = null;
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < currentMatrix.length; i++) {
                for (int j = 0; j < currentMatrix[i].length; j++) {
                    set.add(currentMatrix[i][j]);
                }
            }
            boolean found = set.contains(target);
            if (found) {
                outer: for (int i = 0; i < currentMatrix.length; i++) {
                    for (int j = 0; j < currentMatrix[i].length; j++) {
                        if (currentMatrix[i][j] == target) {
                            pos = new int[]{i, j};
                            break outer;
                        }
                    }
                }
            }
            long end = System.nanoTime();
            double ms = (end - start) / 1_000_000.0;
            if (found && pos != null) {
                html = String.format("HashSet搜尋: 找到 (%d, %d) (%.3f ms)", pos[0], pos[1], ms);
            } else {
                html = String.format("HashSet搜尋: 找不到 (%.3f ms)", ms);
            }
        } else if ("map".equals(method)) {
            long start = System.nanoTime();
            int[] pos = mapSearch(valuePositionMap, target);
            long end = System.nanoTime();
            double ms = (end - start) / 1_000_000.0;
            if (pos != null) {
                html = String.format("Map搜尋: 找到 (%d, %d) (%.3f ms)", pos[0], pos[1], ms);
            } else {
                html = String.format("Map搜尋: 找不到 (%.3f ms)", ms);
            }
        } else if ("all".equals(method)) {
            StringBuilder sb = new StringBuilder();
            // 循序搜尋
            long start1 = System.nanoTime();
            int[] pos1 = null;
            boolean found1 = false;
            outer1: for (int i = 0; i < currentMatrix.length; i++) {
                for (int j = 0; j < currentMatrix[i].length; j++) {
                    if (currentMatrix[i][j] == target) {
                        pos1 = new int[]{i, j};
                        found1 = true;
                        break outer1;
                    }
                }
            }
            long end1 = System.nanoTime();
            double ms1 = (end1 - start1) / 1_000_000.0;
            if (found1 && pos1 != null) {
                sb.append(String.format("循序搜尋:找到(%d,%d)(%.3f ms)<br>", pos1[0], pos1[1], ms1));
            } else {
                sb.append(String.format("循序搜尋:找不到(%.3f ms)<br>", ms1));
            }
            // HashSet搜尋
            long start2 = System.nanoTime();
            int[] pos2 = null;
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < currentMatrix.length; i++) {
                for (int j = 0; j < currentMatrix[i].length; j++) {
                    set.add(currentMatrix[i][j]);
                }
            }
            boolean found2 = set.contains(target);
            if (found2) {
                outer2: for (int i = 0; i < currentMatrix.length; i++) {
                    for (int j = 0; j < currentMatrix[i].length; j++) {
                        if (currentMatrix[i][j] == target) {
                            pos2 = new int[]{i, j};
                            break outer2;
                        }
                    }
                }
            }
            long end2 = System.nanoTime();
            double ms2 = (end2 - start2) / 1_000_000.0;
            if (found2 && pos2 != null) {
                sb.append(String.format("HashSet搜尋:找到(%d,%d)(%.3f ms)<br>", pos2[0], pos2[1], ms2));
            } else {
                sb.append(String.format("HashSet搜尋:找不到(%.3f ms)<br>", ms2));
            }
            // Map搜尋
            long start3 = System.nanoTime();
            int[] pos3 = mapSearch(valuePositionMap, target);
            long end3 = System.nanoTime();
            double ms3 = (end3 - start3) / 1_000_000.0;
            if (pos3 != null) {
                sb.append(String.format("Map搜尋:找到(%d,%d)(%.3f ms)<br>", pos3[0], pos3[1], ms3));
            } else {
                sb.append(String.format("Map搜尋:找不到(%.3f ms)<br>", ms3));
            }
            // 比較快慢
            double[] times = {ms1, ms2, ms3};
            String[] names = {"循序搜尋", "HashSet搜尋", "Map搜尋"};
            // 冒泡排序三個
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2 - i; j++) {
                    if (times[j] < times[j+1]) {
                        double t = times[j]; times[j] = times[j+1]; times[j+1] = t;
                        String tmp = names[j]; names[j] = names[j+1]; names[j+1] = tmp;
                    }
                }
            }
            sb.append("<b>搜尋速度由慢到快：</b><br>");
            for (int i = 0; i < 3; i++) {
                sb.append(String.format("%s：%.3f ms<br>", names[i], times[i]));
            }
            html = sb.toString();
        }
        if (!html.isEmpty()) {
            if (infoHistory.length() > 0) infoHistory.append("<br><hr style='border:0;border-top:1px solid #ccc;'>");
            infoHistory.append(html);
            searchResultLabel.setText("<html>" + infoHistory.toString() + "</html>");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HW0708().setVisible(true);
            }
        });
    }
}
