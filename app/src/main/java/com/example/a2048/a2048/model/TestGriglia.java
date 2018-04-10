package com.example.a2048.a2048.model;

import java.util.Scanner;

public class TestGriglia {
    public static void main(String[] args) {
        Griglia griglia = new Griglia();
        Scanner in = new Scanner(System.in);

        while (true) {
            TestGriglia.stampaGriglia(griglia);
            System.out.print("\nu = UP, d = DOWN, l = LEFT, r = RIGHT: ");
            char comando;
            comando = in.next().charAt(0);
            switch (comando) {
                case 'u':
                    griglia.muoviSu();
                    break;
                case 'd':
                    griglia.muoviGiu();
                    break;
                case 'l':
                    griglia.muoviSinistra();
                    break;
                case 'r':
                    griglia.muoviDestra();
                    break;
            }
        }
    }

    private static void stampaGriglia(Griglia griglia) {
        for (int r = 0; r < 4; r++){
            for (int c = 0; c < 4; c++){
                Numero cella = griglia.griglia[r][c];
                if(cella != null) {
                    System.out.print(cella.numero + "\t");
                } else {
                    System.out.print("*\t");
                }
            }
            System.out.println("");
        }
    }
}
