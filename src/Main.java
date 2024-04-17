import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Main {

    private JFrame frame;
    private JTable table;
    private JTable table_1;
    private JTable table_2;
    private JTextField textField;
    private JTextField textField_1;
    private JButton button;
    private JLabel label_2;
    private JLabel label_3;
    private JLabel label_4;
    static DefaultTableModel model = new DefaultTableModel(),
                      model_1 = new DefaultTableModel(),
                      model_2 = new DefaultTableModel(); 
    private JButton btnGetmin;
    private Scanner sc;
    static public int arr[][];

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 936, 517);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        table = new JTable();
        table.setBounds(29, 37, 515, 308);
        frame.getContentPane().add(table);
        
        table_1 = new JTable();
        table_1.setBounds(29, 372, 518, 35);
        frame.getContentPane().add(table_1);
        
        table_2 = new JTable();
        table_2.setBounds(554, 37, 51, 308);
        frame.getContentPane().add(table_2);
        
        JLabel label = new JLabel("Количество потребностей");
        label.setBounds(615, 11, 216, 14);
        frame.getContentPane().add(label);
        
        textField = new JTextField();
        textField.setBounds(615, 34, 161, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
        JButton btnNewButton = new JButton("Создать таблицу");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable(-1,-1);
            }
        });
        btnNewButton.setBounds(615, 133, 161, 23);
        frame.getContentPane().add(btnNewButton);
        
        JLabel label_1 = new JLabel("Количество запасов");
        label_1.setBounds(615, 65, 216, 14);
        frame.getContentPane().add(label_1);
        
        textField_1 = new JTextField();
        textField_1.setBounds(615, 90, 161, 20);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);
        
        button = new JButton("Рассчитать");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decide();
            }
        });
        button.setBounds(615, 167, 161, 23);
        frame.getContentPane().add(button);
        
        label_2 = new JLabel("Потребности");
        label_2.setBounds(29, 356, 147, 14);
        frame.getContentPane().add(label_2);
        
        label_3 = new JLabel("Запасы");
        label_3.setBounds(549, 11, 77, 14);
        frame.getContentPane().add(label_3);
        
        label_4 = new JLabel("Тарифы");
        label_4.setBounds(29, 11, 129, 14);
        frame.getContentPane().add(label_4);
        
        btnGetmin = new JButton("Открыть файл");
        btnGetmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getMin();
            }
        });
        btnGetmin.setBounds(615, 201, 161, 23);
        frame.getContentPane().add(btnGetmin);
    }
    void createTable(int rows, int collumns) {
        table.removeAll();
        table_1.removeAll();
        table_2.removeAll();
        if(rows<0 && collumns<0) {
            rows = Integer.parseInt(textField.getText());
            collumns = Integer.parseInt(textField_1.getText());
        }
//        DefaultTableModel model = new DefaultTableModel(); 
        table.setModel(model);
        model.setColumnCount(rows);
        model.setRowCount(collumns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(308/collumns);
        
//        DefaultTableModel model_1 = new DefaultTableModel(); 
        table_1.setModel(model_1);
        model_1.setColumnCount(rows);
        model_1.setRowCount(1);
        table_1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table_1.setRowHeight(35);
        
//        DefaultTableModel model_2 = new DefaultTableModel(); 
        table_2.setModel(model_2);
        model_2.setRowCount(collumns);
        model_2.setColumnCount(1);
        table_2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table_2.setRowHeight(308/collumns);
    }
    
    
    void decide() {
      
        
        try{
            int row = model_2.getRowCount();
            int col=model_1.getColumnCount();
            arr = new int[row+1][col+1];
            for(int i=0; i<row;i++) {
                arr[i][col]=Integer.parseInt(model_2.getValueAt(i, 0).toString());
                for(int j=0; j<col;j++) {
                    arr[i][j]=Integer.parseInt(model.getValueAt(i, j).toString());
                }
            }
            for(int i=0; i<col;i++) {
                arr[row][i]=Integer.parseInt(model_1.getValueAt(0, i).toString());
            }
            TestSimply2.setArr(arr);
            TestSimply2.initial();
            TestSimply2.makeSbalansirowSadach();
            
           TestSimply2.main(null);
           new Answer(TestSimply2.opornPlan);
           
           
        }catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(frame, "Не все поля заполнены или неверные данные");
        }
    }
    //open file
    void getMin(){
        try {
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");                
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
             sc= new Scanner(file);
             int row = sc.nextInt();
             int col = sc.nextInt();
             createTable(col-1, row-1);
             arr = new int[row][col];
             for(int i=0; i<row;i++) {
                 for(int j=0; j<col;j++) {
                     arr[i][j]=sc.nextInt();
                 }
             }
             TestSimply2.setArr(arr);
             TestSimply2.initial();
             TestSimply2.makeSbalansirowSadach();
             
             for(int i=0; i<row-1;i++) {
                 model_2.setValueAt(TestSimply2.sapasi[i], i, 0);
                 for(int j=0; j<col-1;j++) {
                     model.setValueAt(arr[i][j], i, j);
                     
                 }
             }
             for(int i=0; i<col-1;i++) {
                 model_1.setValueAt(TestSimply2.potrebit[i], 0, i);
             }
        }
        }catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(frame, "Ошибка при работе с файлом");
        }
    }
        
        
    
}
