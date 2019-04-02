import javax.swing.*;

public class MainFormUI extends JFrame{

    private JPanel mainPanel;

    private JLabel helloLabel;
    private JTextArea clientMessages;

    //Method to initialise this form from outer .java file:
    public MainFormUI(){
        add(mainPanel);
        setTitle("The First Form");
        setSize(500, 500);
    }

    public void appendTextToForm (String text) {
        clientMessages.append("\n" + text);
    }


}
