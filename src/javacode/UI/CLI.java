package javacode.UI;

import java.util.Scanner;

public class CLI {

    public UserInput getInput() {
        // TODO ALLES MIT SCANNER HOLEN
        Scanner sc = new Scanner(System.in);

        System.out.print("Width eingeben: ");
        int tmp0 = sc.nextInt();

        System.out.print("Height eingeben: ");
        int tmp1 = sc.nextInt();

        System.out.print("HunterCount eingeben: ");
        int tmp2 = sc.nextInt();

        System.out.print("PreyCount eingeben: ");
        int tmp3 = sc.nextInt();

        System.out.print("ObstacleCount eingeben: ");
        int tmp4 = sc.nextInt();

        UserInput input = null;

        try {
            input = new UserInput(tmp0, tmp1, tmp2, tmp3, tmp4);
            sc.close();
        } catch (WrongUserInputException e) {
            // TODO Was soll passieren, wenn der Input falsch ist?
            System.out.println(e.toString());
            input = getInput();
        }

        return input;
    }


}
