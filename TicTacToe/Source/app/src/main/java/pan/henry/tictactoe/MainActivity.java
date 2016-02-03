package pan.henry.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    char[][] board = new char[3][3];
    boolean victory = false;
    int turn = 0;
    int row;
    int col;
    TextView announce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        announce = (TextView) findViewById (R.id.Announcer);
    }

    /*
    clickBoard is called when a button is pressed.
    Assigns CROSS or CIRCLE to the button depending on the turn.
     */
    public void clickBoard(View v) {
        String t = (String) v.getTag();
        if (!(t.equals("x")) && !(t.equals("o"))){//see if touched button is empty
            row = (int) t.charAt(0) - 48; //ASCII to Int
            col = (int) t.charAt(1) - 48; //ASCII to Int

            if(victory == false) {//only allow moves when game is not over
                if (turn % 2 == 0) {//cross's turn
                    ImageButton vv = (ImageButton) v;
                    vv.setImageResource(R.drawable.cross);
                    board[row][col] = 'x';
                    v.setTag("x");
                    turn++;
                    announce.setText("Circle's Turn");
                    checkVictory();
                } else {//circle's turn
                    ImageButton vv = (ImageButton) v;
                    vv.setImageResource(R.drawable.circle);
                    board[row][col] = 'o';
                    v.setTag("o");
                    turn++;
                    announce.setText("Cross's Turn");
                    checkVictory();
                }
            }
        }
    }

    /*
    checkVictory checks to see if a game is won by either player.
    Nothing happens if a game is not won, but if the board is filled, the game is over with a tie.
     */
    public void checkVictory(){
        int x_counter = 0;
        int o_counter = 0;

        //Check victory by rows
        for(int i = 0; i < 3; i++ ){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == 'x'){
                    x_counter++;
                    if(x_counter == 3){
                        processVictory(0,0,i);
                        return;
                    }
                }else if(board[i][j] == 'o') {
                    o_counter++;
                    if(o_counter == 3){
                        processVictory(1,0,i);
                        return;
                    }
                }
            }
            x_counter = 0;
            o_counter = 0;
        }

        //check victory by columns
        for(int i = 0; i < 3; i++ ){
            for(int j = 0; j < 3; j++){
                if(board[j][i] == 'x'){
                    x_counter++;
                    if(x_counter == 3){
                        processVictory(0,1,i);
                        return;
                    }
                }else if(board[j][i] == 'o') {
                    o_counter++;
                    if(o_counter == 3){
                        processVictory(1,1,i);
                        return;
                    }
                }
            }
            x_counter = 0;
            o_counter = 0;
        }

        //check diagonal, left to right
        for(int i = 0; i < 3; i++) {
            if (board[i][i] == 'x') {
                x_counter++;
                if(x_counter == 3){
                    processVictory(0,2,0);
                    return;
                }
            }else if(board[i][i] == 'o') {
                o_counter++;
                if(o_counter == 3){
                    processVictory(1,2,0);
                    return;
                }
            }
        }
        x_counter = 0;//reset counters
        o_counter = 0;//reset counters
        //check diagonal, right to left
        for(int i = 0; i < 3; i++) {
            if (board[i][2-i] == 'x') {
                x_counter++;
                if(x_counter == 3){
                    processVictory(0,2,1);
                    return;
                }
            }else if(board[i][2-i] == 'o') {
                o_counter++;
                if(o_counter == 3){
                    processVictory(1,2,1);
                    return;
                }
            }
        }

        //tie game
        if(victory == false && turn == 9){
            announce.setText("TIE GAME");
        }

    }

    /*
    processVictory will highlight the winning combination and set the victory text.
    It takes in three arguments:
        player: 0 - cross, 1 - circle
        type: 0 - row, 1 - column, 2 - diagonal
        id: indicates where victory was achieved
     */
    public void processVictory(int player, int type, int id){
        ImageButton a;
        victory = true;

        if(player == 0){//cross victory
            if(type == 0){//victory by row
                switch(id){//figure out which row
                    case 0: a = (ImageButton) findViewById(R.id.imageButton00);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton01);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton02);
                            a.setImageResource(R.drawable.cross_victory);
                            break;
                    case 1: a = (ImageButton) findViewById(R.id.imageButton10);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton11);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton12);
                            a.setImageResource(R.drawable.cross_victory);
                            break;
                    case 2: a = (ImageButton) findViewById(R.id.imageButton20);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton21);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton22);
                            a.setImageResource(R.drawable.cross_victory);
                            break;
                }
            } else if(type == 1){//victory by column
                switch(id){//figure out which column
                    case 0: a = (ImageButton) findViewById(R.id.imageButton00);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton10);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton20);
                            a.setImageResource(R.drawable.cross_victory);
                            break;
                    case 1: a = (ImageButton) findViewById(R.id.imageButton01);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton11);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton21);
                            a.setImageResource(R.drawable.cross_victory);
                            break;
                    case 2: a = (ImageButton) findViewById(R.id.imageButton02);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton12);
                            a.setImageResource(R.drawable.cross_victory);
                            a = (ImageButton) findViewById(R.id.imageButton22);
                            a.setImageResource(R.drawable.cross_victory);
                            break;
                }
            } else {//victory by diagonal
                if(id == 0){//left to right
                    a = (ImageButton) findViewById(R.id.imageButton00);
                    a.setImageResource(R.drawable.cross_victory);
                    a = (ImageButton) findViewById(R.id.imageButton11);
                    a.setImageResource(R.drawable.cross_victory);
                    a = (ImageButton) findViewById(R.id.imageButton22);
                    a.setImageResource(R.drawable.cross_victory);
                }else{//right to left
                    a = (ImageButton) findViewById(R.id.imageButton02);
                    a.setImageResource(R.drawable.cross_victory);
                    a = (ImageButton) findViewById(R.id.imageButton11);
                    a.setImageResource(R.drawable.cross_victory);
                    a = (ImageButton) findViewById(R.id.imageButton20);
                    a.setImageResource(R.drawable.cross_victory);
                }
            }
            announce.setText("CROSS WINS");
        } else {//circle victory
            if(type == 0){//victory by row
                switch(id){//figure out which row
                    case 0: a = (ImageButton) findViewById(R.id.imageButton00);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton01);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton02);
                            a.setImageResource(R.drawable.circle_victory);
                            break;
                    case 1: a = (ImageButton) findViewById(R.id.imageButton10);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton11);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton12);
                            a.setImageResource(R.drawable.circle_victory);
                            break;
                    case 2: a = (ImageButton) findViewById(R.id.imageButton20);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton21);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton22);
                            a.setImageResource(R.drawable.circle_victory);
                            break;
                }
            } else if(type == 1){//victory by column
                switch(id){//figure out which column
                    case 0: a = (ImageButton) findViewById(R.id.imageButton00);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton10);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton20);
                            a.setImageResource(R.drawable.circle_victory);
                            break;
                    case 1: a = (ImageButton) findViewById(R.id.imageButton01);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton11);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton21);
                            a.setImageResource(R.drawable.circle_victory);
                            break;
                    case 2: a = (ImageButton) findViewById(R.id.imageButton02);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton12);
                            a.setImageResource(R.drawable.circle_victory);
                            a = (ImageButton) findViewById(R.id.imageButton22);
                            a.setImageResource(R.drawable.circle_victory);
                            break;
                }
            } else {//victory by diagonal
                if(id == 0){//left to right
                    a = (ImageButton) findViewById(R.id.imageButton00);
                    a.setImageResource(R.drawable.circle_victory);
                    a = (ImageButton) findViewById(R.id.imageButton11);
                    a.setImageResource(R.drawable.circle_victory);
                    a = (ImageButton) findViewById(R.id.imageButton22);
                    a.setImageResource(R.drawable.circle_victory);
                }else{//right to left
                    a = (ImageButton) findViewById(R.id.imageButton02);
                    a.setImageResource(R.drawable.circle_victory);
                    a = (ImageButton) findViewById(R.id.imageButton11);
                    a.setImageResource(R.drawable.circle_victory);
                    a = (ImageButton) findViewById(R.id.imageButton20);
                    a.setImageResource(R.drawable.circle_victory);
                }
            }
            announce.setText("CIRCLE WINS");
        }
    }


    /*
    reset will clear the board and reset the game.
     */
    public void reset(View v) {
        turn = 0;
        victory = false;

        //clear internal board
        for(int i = 0; i < 3; i++ ) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 'b';
            }
        }

        //return all displays to normal
        announce.setText("Cross's Turn");
        ImageButton a = (ImageButton) findViewById(R.id.imageButton00);
        a.setImageResource(R.drawable.blank);
        a.setTag("00");
        a = (ImageButton) findViewById(R.id.imageButton01);
        a.setImageResource(R.drawable.blank);
        a.setTag("01");
        a = (ImageButton) findViewById(R.id.imageButton02);
        a.setImageResource(R.drawable.blank);
        a.setTag("02");
        a = (ImageButton) findViewById(R.id.imageButton10);
        a.setImageResource(R.drawable.blank);
        a.setTag("10");
        a = (ImageButton) findViewById(R.id.imageButton11);
        a.setImageResource(R.drawable.blank);
        a.setTag("11");
        a = (ImageButton) findViewById(R.id.imageButton12);
        a.setImageResource(R.drawable.blank);
        a.setTag("12");
        a = (ImageButton) findViewById(R.id.imageButton20);
        a.setImageResource(R.drawable.blank);
        a.setTag("20");
        a = (ImageButton) findViewById(R.id.imageButton21);
        a.setImageResource(R.drawable.blank);
        a.setTag("21");
        a = (ImageButton) findViewById(R.id.imageButton22);
        a.setImageResource(R.drawable.blank);
        a.setTag("22");
    }
}