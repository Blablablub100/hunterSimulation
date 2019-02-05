package UI;

import Simulation.SimulationObjects.*;

import java.util.List;
import java.util.Scanner;

public class CLI {

    public UserInput getInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Type in width: ");
        int tmp0 = sc.nextInt();

        System.out.print("Type in height: ");
        int tmp1 = sc.nextInt();

        System.out.print("Type in HunterCount: ");
        int tmp2 = sc.nextInt();

        System.out.print("Type in PreyCount: ");
        int tmp3 = sc.nextInt();

        System.out.print("Type in ObstacleCount: ");
        int tmp4 = sc.nextInt();

        UserInput input;

        try {
            input = new UserInput(tmp0, tmp1, tmp2, tmp3, tmp4);
            sc.close();
        } catch (WrongUserInputException e) {
            System.out.println(e.toString());
            input = getInput();
        }

        return input;
    }


    public void printBoard(Board b) {
        char[][] boardOutputCLI = new char[b.getWidth()][b.getHeight()];
        fill(boardOutputCLI, '#');

        List<Hunter> hunters;
        List<Prey> preys;
        List<BoardObject> obstacles;

        hunters = b.getHunters();
        preys = b.getPreys();
        obstacles = b.getBoardObjects();

        for (Hunter hunter : hunters) setElement(boardOutputCLI, hunter);
        for (Prey prey : preys) setElement(boardOutputCLI, prey);
        for (BoardObject obstacle : obstacles) setElement(boardOutputCLI, obstacle);

        outputBoard(boardOutputCLI);
    }

    private void setElement(char[][] board, BoardObject o) {
        BoardObject.Location loc = o.getLocation();
        if (o instanceof Hunter) {
            board[loc.getX()][loc.getY()] = 'h';
        } else if (o instanceof Prey) {
            board[loc.getX()][loc.getY()] = 'p';
        } else if (o instanceof DeadCorpse) {
            board[loc.getX()][loc.getY()] = 'c';
        } else {
            board[loc.getX()][loc.getY()] = 'o';
        }
    }

    private void fill(char[][] toFill, char c) {
        for(int x = 0; x < toFill.length; x++) {
            for(int y = 0; y < toFill[x].length; y++) {
                toFill[x][y] = c;
            }
        }
    }

    private void outputBoard(char[][] output) {
        for(int y = 0; y < output[0].length; y++) {
            for(int x = 0; x < output.length; x++) {
                System.out.print(output[x][y]);
            }
            System.out.println();
        }
    }
}
