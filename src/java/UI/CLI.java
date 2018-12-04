package java.UI;

import java.util.Scanner;

public class CLI {

    public UserInput getInput() {
        // TODO ALLES MIT SCANNER HOLEN
        Scanner sc = new Scanner(System.in);

        System.out.println("Width eingeben: ");
        int tmp0 = sc.nextInt();

        System.out.println("Height eingeben: ");
        int tmp1 = sc.nextInt();

        System.out.println("HunterCount eingeben: ");
        int tmp2 = sc.nextInt();

        System.out.println("PreyCount eingeben: ");
        int tmp3 = sc.nextInt();

        System.out.println("ObstacleCount eingeben: ");
        int tmp4 = sc.nextInt();

        sc.close();

        UserInput input = new UserInput(tmp0, tmp1, tmp2, tmp3, tmp4);
        return input;
    }


}
