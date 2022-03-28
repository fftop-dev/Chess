import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class Game extends JFrame {
    String[] board = new String[]{
            "-r","-h","-b","-k","-q","-b","-h","-r",
            "-p","-p","-p","-p","-p","-p","-p","-p",
            " "," "," "," "," "," "," "," ",
            " "," "," "," "," "," "," "," ",
            " "," "," "," "," "," "," "," ",
            " "," "," "," "," "," "," "," ",
            "_p","_p","_p","_p","_p","_p","_p","_p",
            "_r","_h","_b","_q","_k","_b","_h","_r"
    };
    private int[] borderRight = new int[]{0,8,16,24,32,40,48,56};
    private int[] borderLeft = new int[]{7,15,23,31,39,47,55, 63};
    int[] temp = new int[]{1};
    private int buttonCount = 0;
    private int cntr = 1;
    private int player = 0;
    private int index1;
    private int index2;
    private ArrayList<JButton> buttons = new ArrayList<>(64);
    JPanel mainPanel = new JPanel(new GridLayout(8,8));
    public Game(){
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        for (int i = 0; i<8; i++){
            if (i%2==0){
                createBoard(1);
            }
            else{
                createBoard(2);
            }
        }
        setTitle("Chess v1.01");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        setResizable(false);
    }
    public void createBoard(int set){
        for (int i = 0; i < 8; i++){
            JButton button = new JButton();
            button.setName(Integer.toString(buttonCount++));
            button.addActionListener(new Listener());
            if (i%2 == 0){
                if (set==1){
                    button.setBackground(Color.decode("#eddacb"));
                }
                else{
                    button.setBackground(Color.decode("#d8aa76"));
                }
            }
            else{
                if (set==1){
                    button.setBackground(Color.decode("#d8aa76"));
                }
                else{
                    button.setBackground(Color.decode("#eddacb"));
                }
            }
            mainPanel.add(button);
            buttons.add(button);
        }
    }
    public void updateBoard(){
        for (int i = 0; i < 64; i++){
            buttons.get(i).setIcon(new ImageIcon("files/" + board[i] + ".png"));
            buttons.get(i).setBorder(null);
            buttons.get(i).setEnabled(false);
            if (player%2==0){
                if (board[i].contains("_")){
                    buttons.get(i).setEnabled(true);
                }
            }
            else{
                if (board[i].contains("-")){
                    buttons.get(i).setEnabled(true);
                }
            }
        }
    }
    class Listener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (cntr%2 == 0){
                index2 = Integer.parseInt(button.getName());
                if (index1 != index2){
                    performPlay(index1,index2);
                    cntr++;
                    buttons.get(index2).setEnabled(false);
                }
                else{
                    cntr--;
                    player--;
                    updateBoard();
                }
            }
            else{
                index1 = Integer.parseInt(button.getName());
                if (board[index1].contains("_")){
                    markFields(validation(index1,-1, "_"), index1);
                }
                else{
                    markFields(validation(index1,1, "-"), index1);
                }
                cntr++;
                player++;
            }
        }
    }
    public void performPlay(int index1, int index2){
        board[index2] = board[index1];
        board[index1] = " ";
        updateBoard();
    }
    public Vector<Integer> validation(int index1, int operator, String color){
        Vector<Integer> validMoves = new Vector<>();
        if (board[index1].equals("_p") || board[index1].equals("-p")){
            if (operator == -1){
                if (index1 >= 48 && index1 <= 55 && board[index1-16].equals(" ") && board[index1-16] != null  && board[index1-8].equals(" ")){
                    validMoves.add(-16);
                }
            }
            else{
                if (index1 >= 8 && index1 <= 15 && board[index1+16].equals(" ") && board[index1+16] != null && board[index1+8].equals(" ")){
                    validMoves.add(16);
                }
            }
            pawn(validMoves,operator, color);
        }
        else if (board[index1].equals("_r") || board[index1].equals("-r")){
            prepareRook(validMoves, operator, color, false);
        }
        else if (board[index1].equals("_b") || board[index1].equals("-b")){
            prepareBishop(validMoves, operator, color, false);
        }
        else if (board[index1].equals("_q") || board[index1].equals("-q")){
            prepareRook(validMoves, operator, color, false);
            prepareBishop(validMoves, operator, color, false);
        }
        else if (board[index1].equals("_k") || board[index1].equals("-k")){
            prepareBishop(validMoves, operator, color, true);
            prepareRook(validMoves, operator, color, true);
        }
        return validMoves;
    }
    public Vector<Integer> prepareRook(Vector<Integer> validMoves, int operator, String color, boolean king){
        if (operator == 1){
            validMoves = bishop(validMoves, operator, color, 1, borderRight, king);
            validMoves = bishop(validMoves, -1, color, 1, borderLeft, king);
        }
        else{
            validMoves = bishop(validMoves, operator, color, 1, borderLeft, king);
            validMoves = bishop(validMoves, 1, color, 1, borderRight, king);
        }
        validMoves = bishop(validMoves, operator, color, 8, temp, king);
        validMoves = bishop(validMoves, operator*(-1), color, 8, temp, king);
        return validMoves;
    }
    public Vector<Integer> prepareBishop(Vector<Integer> validMoves, int operator, String color, boolean king){
        if (operator == 1){
            validMoves = bishop(validMoves, operator, color, 9, borderRight, king);
            validMoves = bishop(validMoves, operator, color, 7, borderLeft, king);
            validMoves = bishop(validMoves, operator*(-1), color, 9, borderLeft, king);
            validMoves = bishop(validMoves, operator*(-1), color, 7, borderRight, king);
        }
        else{
            validMoves = bishop(validMoves, operator, color, 9, borderLeft, king);
            validMoves = bishop(validMoves, operator, color, 7, borderRight, king);
            validMoves = bishop(validMoves, operator*(-1), color, 9, borderRight, king);
            validMoves = bishop(validMoves, operator*(-1), color, 7, borderLeft, king);
        }
        return validMoves;
    }
    public void markFields(Vector<Integer> validMoves, int index1){
        for (int i = 0; i < 64; i++){
            buttons.get(i).setEnabled(false);
        }
        buttons.get(index1).setEnabled(true);
        for (int i = 0; i < validMoves.size(); i++){
            if (board[index1+validMoves.get(i)] != null){
                buttons.get(index1+validMoves.get(i)).setEnabled(true);
                if (board[index1+validMoves.get(i)].equals(" ")){
                    buttons.get(index1+validMoves.get(i)).setIcon(new ImageIcon("files/valid.png"));
                }
                else{
                    buttons.get(index1+validMoves.get(i)).setBorder(BorderFactory.createLineBorder(Color.RED));
                }
            }
        }
    }
    public Vector<Integer> pawn(Vector<Integer> validMoves, int operator, String color){
        if (board[index1+(8*operator)].equals(" ") && board[index1+(8*operator)] != null){
            validMoves.add(8*operator);
        }
        if ((index1+(9*operator)%8 != 0 && (index1+(6*operator))%8 != 0)){
            if (!board[index1+(7*operator)].equals(" ") && !board[index1+(7*operator)].contains(color) && board[index1+(7*operator)] != null){
                validMoves.add(7*operator);
            }
            if(!board[index1+(9*operator)].equals(" ") && !board[index1+(9*operator)].contains(color) && board[index1+(9*operator)] != null){
                validMoves.add(9*operator);
            }
        }
        return validMoves;
    }
    public Vector<Integer> bishop(Vector<Integer> validMoves, int operator, String color, int number, int[] array, boolean king){
        int validIndex = index1;
        int check = 0;
        int i = 1;
        if (((array[0] == 0 && operator == -1 && checkBorder(validIndex, array))||(array[0] == 7 && operator == 1 && checkBorder(validIndex, array))) && number != 8 && number != 7){
            return validMoves;
        }
        while(validIndex+(number*operator) >= 0 && validIndex+(number*operator) <= 63 && !board[validIndex+(number*operator)].contains(color) && !checkBorder(validIndex + (number * operator), array)){
            if (board[validIndex+(number*operator)].contains(" ")){
                validMoves.add((i*number)*operator);
            }
            if (!board[validIndex+(number*operator)].contains(" ") && !board[validIndex+(number*operator)].contains(color)){
                validMoves.add((i*number)*operator);
                break;
            }
            i++;
            validIndex = validIndex + (number*operator);
            if (king){
                break;
            }
        }
        return validMoves;
    }
    public boolean checkBorder(int validIndex, int[] array){
        for (int i = 0; i < array.length; i++){
            if (validIndex == array[i]){
                return true;
            }
        }
        return false;
    }
}