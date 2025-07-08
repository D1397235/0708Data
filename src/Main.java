import java.util.*;

//可以自動產生一個N*N的二維數字矩陣, 數字不重覆, 矩陣大小可由使用者指定
//可以輸入一個數字，然後在這個咀任中找到數字，若找到顯示數字所在的位置, 找不到則輸出找不到
//搜尋方法分別有：循序搜尋（sequential search），二元搜尋（binary search），以及雜湊搜尋（hash search）

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("請輸入矩陣大小 N：");
        int n = scanner.nextInt();
        int size = n * n;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        int[][] matrix = new int[n][n];
        Iterator<Integer> it = numbers.iterator();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = it.next();
            }
        }
        System.out.println("產生的矩陣：");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }

        System.out.print("請輸入要搜尋的數字：");
        int target = scanner.nextInt();
        System.out.println("請選擇搜尋方法：1.循序搜尋 2.二元搜尋 3.雜湊搜尋");
        int method = scanner.nextInt();
        boolean found = false;
        int row = -1, col = -1;
        switch (method) {
            case 1:
                // 循序搜尋
                outer:
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (matrix[i][j] == target) {
                            found = true;
                            row = i;
                            col = j;
                            break outer;
                        }
                    }
                }
                break;
            case 2:
                // 二元搜尋（先將矩陣攤平成一維陣列並排序）
                int[] arr = new int[n * n];
                int idx = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        arr[idx++] = matrix[i][j];
                    }
                }
                Arrays.sort(arr);
                int pos = Arrays.binarySearch(arr, target);
                if (pos >= 0) {
                    // 找到後再回原矩陣找位置
                    outer:
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (matrix[i][j] == target) {
                                found = true;
                                row = i;
                                col = j;
                                break outer;
                            }
                        }
                    }
                }
                break;
            case 3:
                // 雜湊搜尋（用HashMap記錄數字位置）
                Map<Integer, int[]> map = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        map.put(matrix[i][j], new int[]{i, j});
                    }
                }
                if (map.containsKey(target)) {
                    found = true;
                    row = map.get(target)[0];
                    col = map.get(target)[1];
                }
                break;
            default:
                System.out.println("搜尋方法選擇錯誤");
                return;
        }
        if (found) {
            System.out.printf("找到 %d 於第 %d 列, 第 %d 行\n", target, row + 1, col + 1);
        } else {
            System.out.println("找不到");
        }
    }
}