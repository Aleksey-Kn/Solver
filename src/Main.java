import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class Main {
    private static float[][] remove(float mat[][]){
        int counter = 0;
        boolean[] need = new boolean[mat.length];
        Arrays.fill(need, false);
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[i].length; j++){
                if(mat[i][j] != 0){
                    counter++;
                    need[i] = true;
                    break;
                }
            }
        }
        float[][] now = new float[counter][mat[0].length];
        for(int i = 0, count = 0; i < mat.length; i++){
            if(need[i]){
                now[count++] = mat[i];
            }
        }
        return now;
    }

    private static boolean forNotNul(float[][] mat, int[] index, int n){
        if(mat[n][index[n]] == 0){
            for(int i = n + 1; i < mat.length; i++){// меняем строки, если это возможно
                if(mat[i][index[n]] != 0){
                    float[] temp = mat[n];
                    mat[n] = mat[i];
                    mat[i] = temp;
                    System.out.println((n + 1) + " <-> " + (i + 1));
                    return true;
                }
            }

            for(int i = n + 1; i < mat[n].length - 1; i++){// меняем столбцы, если это возможно
                if(mat[n][index[i]] != 0){
                    int temp = index[n];
                    index[n] = index[i];
                    index[i] = temp;
                    return true;
                }
            }

            for (int i = n + 1; i < mat.length; i++){ //меняем строки и столбцы, если это возмножно
                for(int j = 0; j < n; j++){
                    if(mat[i][index[j]] != 0){
                        float[] temp = mat[n];
                        mat[n] = mat[i];
                        mat[i] = temp;
                        System.out.println((n + 1) + " <-> " + (i + 1));
                        int temp1 = index[n];
                        index[n] = index[j];
                        index[j] = temp1;
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private static boolean isNoSolutions(float[][] mat){
        boolean findSolut;
        for (int i = mat.length - 1; i >= 0; i--){
            findSolut = false;
            for(int j = 0; j < mat[i].length - 1; j++){
                if(mat[i][j] != 0){
                    findSolut = true;
                    break;
                }
            }
            if(mat[i][mat[i].length - 1] != 0 && !findSolut){
                return true;
            }
        }
        return false;
    }

    private static boolean isInfinitelyManySolutions(int kolStr, int kolKor){
        if(kolStr < kolKor){
            return true;
        }
        return false;
    }

    private static boolean equalsLine(float[][] mat){
        for (int i = 0; i < mat.length - 1; i++){
            for (int j = i + 1; j < mat.length; j++) {
                if(Arrays.equals(mat[i], mat[j])){
                    return true;
                }
            }

        }
        return false;
    }


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

            int kolStolb = scan.nextInt();
            int kolStr = scan.nextInt();
            float[][] mat = new float[kolStr][kolStolb + 1];
            int[] index = new int[kolStolb + 1];
            for(int i = 0; i <= kolStolb; i++){
                index[i] = i;
            }
            float temp;
            for(int i = 0; i < kolStr; i++){
                for(int j = 0; j <= kolStolb; j++){
                    mat[i][j] = scan.nextFloat();
                }
            }
            scan.close();

            mat = remove(mat);//  убираем изначально нулевые строки
            kolStr = mat.length;
            System.out.println("Start solving the equation:");
            if(equalsLine(mat)){// если строки повторяются, то решений бесконечно много
                System.out.println("Infinitely many solutions");
                writer.write("Infinitely many solutions");
                writer.close();
                return;
            }
            if (isNoSolutions(mat) || kolStolb < kolStr) { // проверка на отсутствие корней
                System.out.println("No solutions");
                writer.write("No solutions");
                writer.close();
                return;
            }
            System.out.println("Rows manipulation: ");

            for(int k = 0; k < kolStr; k++) { // обнуление чисел под главной диагоалью
                if (!forNotNul(mat, index, k)){
                    break;
                }
                temp = mat[k][index[k]];
                for(int j = k; j <= kolStolb; j++){//чтобы на главной диагонали была 1
                    mat[k][index[j]] /= temp;
                }
                for (int i = k + 1; i < kolStr; i++) { // зануляем всё, что ниже первой цифры текущего столбца
                    if(mat[i][index[k]] != 0) {
                        System.out.println("-(" + ((1 / temp) * mat[i][index[k]]) + " * R" + (k + 1) + ") + R" + (i + 1) + " -> R" + (i + 1));
                        temp = mat[i][index[k]]; // смторим значение элемента под главной диагональю
                        for (int j = 0; j <= kolStolb; j++) {// вычитание строк
                            mat[i][index[j]] -= mat[k][index[j]] * temp;
                        }
                    }
                }
            }

            mat = remove(mat);
            kolStr = mat.length;

            if (isNoSolutions(mat)) { // проверка на отсутствие и бесконечное множество корней
                System.out.println("No solutions");
                writer.write("No solutions");
                writer.close();
                return;
            }
            if(isInfinitelyManySolutions(kolStr, kolStolb)){
                System.out.println("Infinitely many solutions");
                writer.write("Infinitely many solutions");
                writer.close();
                return;
            }


            float[] result = new float[mat.length];// вычисляем корни
            for(int i = 0; i < kolStolb; i++) {
                if (mat[kolStr - 1][i] != 0) {
                    result[i] = mat[kolStr - 1][kolStolb];
                }
            }
            for(int i = kolStr - 2; i >= 0; i--){
                for(int j = 0; j < kolStolb; j++){
                    mat[i][kolStolb] -= mat[i][j] * result[j]; // избавляемся от уже известных корней
                }
                result[i] = mat[i][kolStolb];
            }

            for (int i = 0; i < kolStolb; i++){
                writer.write(result[i] + "\n");
            }
            System.out.println("Saved to file " + fileout.getName());
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