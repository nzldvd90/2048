package com.example.a2048.a2048.model;

import java.util.Scanner;

public class TestGriglia {
    public static void main(String[] args) {
        Griglia griglia = new Griglia();

        while (true) {
            griglia.stampaGriglia();
            System.out.print("\nu = UP, d = DOWN, l = LEFT, r = RIGHT: ");
            Scanner in = new Scanner(System.in);
            char comando = '.';
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
}
