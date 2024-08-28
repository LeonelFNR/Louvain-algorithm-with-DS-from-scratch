package cat.urv.deim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cat.urv.deim.exceptions.ElementNoTrobat;

public class Main {

    public Main() {}

    public static void main(String[] args) throws IOException{
        try {
            System.out.println("Introduce filename to read\n");
            BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
            String filename = reader.readLine();
            PajekReader testReader = new PajekReader(200, filename);
            DSComunity testing = new DSComunity(testReader.obtainGraph());
            testing.creatComunityFile(filename);

        } catch (IOException e) {
            throw new IOException();
        }
    }
}
