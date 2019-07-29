import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args){
        File filein = null;
        File fileout = null;
        for(int i = 0; i < args.length - 1; i++) {
            if(args[i].equals("-in")) {
                filein = new File(args[++i]);
            }
            else if(args[i].equals("-out")) {
                fileout = new File(args[++i]);
            }
        }
        try {
            Scanner scan = new Scanner(filein); // открытие файлов на запись и чтение
            FileWriter writer = new FileWriter(fileout, false);

            int kolKor = scan.nextInt();
            float[][] mat = new float[kolKor][kolKor + 1];
            float temp;
            float[] result = new float[kolKor];
            for(int i = 0; i < kolKor; i++){
                for(int j = 0; j <= kolKor; j++){
                    mat[i][j] = scan.nextInt();
                }
            }
            scan.close();

            for(int k = 0; k < kolKor - 1; k++) { // обнуление чисел под главной диагоалью
                temp = mat[k][k];
                for(int j = k; j <= kolKor; j++){//чтобы на главной диагонали была 1
                    mat[k][j] /= temp;
                }
                for (int i = k + 1; i < kolKor; i++) {
                    temp = mat[i][k];
                    for(int j = 0; j <= kolKor; j++){
                        mat[i][j] -= mat[k][j] * temp;
                    }
                }
            }
            temp = mat[kolKor - 1][kolKor - 1];// зануление главной диагонали в последней строке
            for(int j = kolKor - 1; j <= kolKor; j++){
                mat[kolKor - 1][j] /= temp;
            }

            result[kolKor - 1] = mat[kolKor - 1][kolKor] / mat[kolKor - 1][kolKor - 1];
            for(int i = kolKor - 2; i >= 0; i--){
                for(int j = i + 1; j < kolKor; j++){
                    mat[i][kolKor] -= mat[i][j] * result[j];
                }
                result[i] = mat[i][kolKor];
            }

            for (int i = 0; i < kolKor; i++){
                writer.write(result[i] + "\t");
            }
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File or input key not found!");
        }
        catch (IOException e){
            System.out.println("File or output key not found!");
        }
    }
}
