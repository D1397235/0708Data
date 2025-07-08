import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

//寫一個java程式, 可以讓使用者指定一個n*n的矩陣大小, 把矩陣顯示在畫面上,附GUI. 產生的數據不重覆
public class MainSec extends JFrame {
    private JTextField sizeField;
    private JButton generateButton;
    private JPanel matrixPanel;

    public MainSec() {
        setTitle("n*n 矩陣產生器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("請輸入 n："));
        sizeField = new JTextField(5);
        inputPanel.add(sizeField);
        generateButton = new JButton("產生矩陣");
        inputPanel.add(generateButton);
        add(inputPanel, BorderLayout.NORTH);

        matrixPanel = new JPanel();
        add(matrixPanel, BorderLayout.CENTER);

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateMatrix();
            }
        });

        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generateMatrix() {
        String text = sizeField.getText();
        int n;
        try {
            n = Integer.parseInt(text);
            if (n <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請輸入正整數！");
            return;
        }
        int total = n * n;
        java.util.List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= total; i++) numbers.add(i);
        Collections.shuffle(numbers);

        matrixPanel.removeAll();
        matrixPanel.setLayout(new GridLayout(n, n, 5, 5));
        Iterator<Integer> it = numbers.iterator();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixPanel.add(new JLabel(String.valueOf(it.next()), SwingConstants.CENTER));
            }
        }
        matrixPanel.revalidate();
        matrixPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainSec());
    }
}
