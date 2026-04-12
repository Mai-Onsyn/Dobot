import cius.mai_onsyn.dobot.api.DobotE6V4;
import cius.mai_onsyn.dobot.api.RobotCalGetApi;
import cius.mai_onsyn.dobot.api.RobotControlApi;
import cius.mai_onsyn.dobot.api.socket.ParamValue;
import cius.mai_onsyn.dobot.api.socket.RobotResponse;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class CoordinateFrame extends JFrame {
    private JPanel mainPanel = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private double j1=0;
    private double j2=0;
    private double j3=0;
    private double j4=0;
    private double j5=0;
    private double j6=0;

    public DobotE6V4 dot = new DobotE6V4("192.168.5.1");
    public RobotControlApi control;

    public CoordinateFrame() {
        init();
    }

    private void update(){
        this.getCoordinate();
        jLabel1.setText(j1+"");
        jLabel2.setText(j2+"");
        jLabel3.setText(j3+"");
        jLabel4.setText(j4+"");
        jLabel5.setText(j5+"");
        jLabel6.setText(j6+"");
    }

    public void init() {
        dot.connect();
        control = dot.getControl();

        control.requestControl();
        control.stop();
        control.clearError();

        mainPanel.add(jLabel1);
        mainPanel.add(jLabel2);
        mainPanel.add(jLabel3);
        mainPanel.add(jLabel4);
        mainPanel.add(jLabel5);
        mainPanel.add(jLabel6);
        update();
        Thread.ofVirtual().start(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    this.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dot.disconnect();
            }
        });
        this.add(mainPanel);
    }

    private void getCoordinate(){
        RobotCalGetApi calGet = dot.getCalGet();

        RobotResponse angle = calGet.getAngle();
        List<ParamValue> refValues = angle.getRefValues();
        j1 = (double) refValues.get(0).getValue();
        j2 = (double) refValues.get(1).getValue();
        j3 = (double) refValues.get(2).getValue();
        j4 = (double) refValues.get(3).getValue();
        j5 = (double) refValues.get(4).getValue();
        j6 = (double) refValues.get(5).getValue();

    }

    public void disconnect() {
        dot.disconnect();
    }
}
