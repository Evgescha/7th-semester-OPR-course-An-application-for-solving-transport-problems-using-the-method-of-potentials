import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;

public class Answer {

    private JFrame frame;
    private JTable table;
    JLabel lblFx ;
int[][] plan;
DefaultTableModel model;

    /**
     * Create the application.
     */
    public Answer(int[][] plan) {
       this.plan = plan;
        initialize();
        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 602, 481);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setBounds(10, 11, 566, 347);
        frame.getContentPane().add(table);
        
        JButton button = new JButton("Сохранить");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        button.setBounds(207, 408, 145, 23);
        frame.getContentPane().add(button);
         model = new DefaultTableModel();
        table.setModel(model);
        model.setRowCount(plan.length);
        model.setColumnCount(plan[0].length);
        for(int i=0; i<plan.length;i++) {
            for (int j=0; j<plan[0].length;j++){
                model.setValueAt(plan[i][j], i, j);
            }
        }
       
        table.setRowHeight(560/plan[0].length);
        table.setRowHeight(340/plan.length);
        
        JButton button_1 = new JButton("Показать опорный план");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getOporn();
            }
        });
        button_1.setBounds(20, 408, 177, 23);
        frame.getContentPane().add(button_1);
        
        JButton button_2 = new JButton("Показать оптимальный план");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getOptim();
            }
        });
        button_2.setBounds(362, 408, 214, 23);
        frame.getContentPane().add(button_2);
        
         lblFx = new JLabel("F(x) = ");
        lblFx.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblFx.setBounds(10, 370, 160, 27);
        frame.getContentPane().add(lblFx);
        getOptim();
    }
    void save() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          FileWriter fr = null;
//          BufferedWriter br = null;
          String dataWithNewLine=getPlanToWrite();;
          try{
              fr = new FileWriter(file);
              fr.write(dataWithNewLine);
              fr.flush();
//              br = new BufferedWriter(fr);
//              for(int i = noOfLines; i>0; i--){
//                  br.write(dataWithNewLine);
//                  
//              }
          } catch (IOException e) {
              e.printStackTrace();
          }
          // save to file
        }
    }
    String getPlanToWrite() {
        String temp = "";
        for(int[][] asd:TestSimply2.STEP) {
            int fx = 0;
            for(int i=0; i<asd.length;i++) {
                for (int j=0; j<asd[0].length;j++){
                    temp += asd[i][j]+" ";
                    fx+=asd[i][j]*Main.arr[i][j];
                }
                temp+="\n";
            }
            temp+="F(x)="+fx+"\n";
        }
        
        return temp;
    }
    void getOporn() {
        int fx = 0;
        
        for(int i=0; i<TestSimply2.opornPlanTemp.length;i++) {
            for (int j=0; j<TestSimply2.opornPlanTemp[0].length;j++){
                model.setValueAt(TestSimply2.opornPlanTemp[i][j], i, j);
                fx+=TestSimply2.opornPlanTemp[i][j]*Main.arr[i][j];
            }
        }
        lblFx.setText("F(x)="+fx);        
    }
    void getOptim() {
        int fx = 0;
        for(int i=0; i<plan.length;i++) {
            for (int j=0; j<plan[0].length;j++){
                model.setValueAt(plan[i][j], i, j);
                fx+=plan[i][j]*Main.arr[i][j];
            }
        }
        lblFx.setText("F(x)="+fx);
    }
}
