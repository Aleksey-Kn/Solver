import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class Main {
    private static Complex[][] remove(Complex mat[][]){
        int counter = 0;
        boolean[] need = new boolean[mat.length];
        Arrays.fill(need, false);
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[i].length; j++){
                if(mat[i][j].isNotNull()){
                    counter++;
                    need[i] = true;
                    break;
                }
            }
        }
        Complex[][] now = new Complex[counter][mat[0].length];
        for(int i = 0, count = 0; i < mat.length; i++){
            if(need[i]){
                now[count++] = mat[i];
            }
        }
        return now;
    }

    private static boolean forNotNul(Complex[][] mat, int[] index, int n){
        if(!mat[n][index[n]].isNotNull()){
            for(int i = n + 1; i < mat.length; i++){// меняем строки, если это возможно
                if(mat[i][index[n]].isNotNull()){
                    Complex[] temp = mat[n];
                    mat[n] = mat[i];
                    mat[i] = temp;
                    System.out.println((n + 1) + " <-> " + (i + 1));
                    return true;
                }
            }

            for(int i = n + 1; i < mat[n].length - 1; i++){// меняем столбцы, если это возможно
                if(mat[n][index[i]].isNotNull()){
                    int temp = index[n];
                    index[n] = index[i];
                    index[i] = temp;
                    return true;
                }
            }

            for (int i = n + 1; i < mat.length; i++){ //меняем строки и столбцы, если это возмножно
                for(int j = 0; j < n; j++){
                    if(mat[i][index[j]].isNotNull()){
                        Complex[] temp = mat[n];
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

    private static boolean nearlyNotNul(Complex[][] mat, int[] index, int n){
        if(!mat[n][index[n]].isNearlyNotNull()){
            for(int i = n + 1; i < mat.length; i++){// меняем строки, если это возможно
                if(mat[i][index[n]].isNearlyNotNull()){
                    Complex[] temp = mat[n];
                    mat[n] = mat[i];
                    mat[i] = temp;
                    System.out.println((n + 1) + " <-> " + (i + 1));
                    return true;
                }
            }

            for(int i = n + 1; i < mat[n].length - 1; i++){// меняем столбцы, если это возможно
                if(mat[n][index[i]].isNearlyNotNull()){
                    int temp = index[n];
                    index[n] = index[i];
                    index[i] = temp;
                    return true;
                }
            }

            for (int i = n + 1; i < mat.length; i++){ //меняем строки и столбцы, если это возмножно
                for(int j = 0; j < n; j++){
                    if(mat[i][index[j]].isNearlyNotNull()){
                        Complex[] temp = mat[n];
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

    private static boolean isNoSolutions(Complex[][] mat){
        boolean findSolut;
        for (int i = mat.length - 1; i >= 0; i--){
            findSolut = false;
            for(int j = 0; j < mat[i].length - 1; j++){
                if(mat[i][j].isNotNull()){
                    findSolut = true;
                    break;
                }
            }
            if(mat[i][mat[i].length - 1].isNotNull() && !findSolut){
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

    private static boolean equalsLine(Complex[][] mat){
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
            Complex[][] mat = new Complex[kolStr][kolStolb + 1];
            int[] index = new int[kolStolb + 1];
            for(int i = 0; i <= kolStolb; i++){
                index[i] = i;
            }
            Complex temp;
            final Complex complr1m1 = new Complex("1+1i");
            for(int i = 0; i < kolStr; i++){
                for(int j = 0; j <= kolStolb; j++){
                    mat[i][j] = new Complex(scan.next());
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
                if (forNotNul(mat, index, k)){
                    if(nearlyNotNul(mat, index, k)){
                        temp = new Complex(mat[k][index[k]]);
                        mat[k][index[k]].dev(mat[k][index[k]]);
                    }
                    else {
                        mat[k][index[k]].dev(complr1m1);
                        temp = new Complex(mat[k][index[k]]);
                        mat[k][index[k]] = new Complex("1+1i");
                    }
                }
                else break;
                for(int j = k + 1; j <= kolStolb; j++){//чтобы на главной диагонали была 1
                    mat[k][index[j]].dev(temp);
                }
                for (int i = k + 1; i < kolStr; i++) { // зануляем всё, что ниже первой цифры текущего столбца
                    if(mat[i][index[k]].isNotNull()) {
                        System.out.println("-(" + ((1 / temp.getReal()) * mat[i][index[k]].getReal()) + (((1 / temp.getImaginary()) * mat[i][index[k]].getImaginary()) > 0? "+": "") + ((1 / temp.getImaginary()) * mat[i][index[k]].getImaginary()) + "i" + " * R" + (k + 1) + ") + R" + (i + 1) + " -> R" + (i + 1));
                        temp = new Complex(mat[i][index[k]]); // смторим значение элемента под главной диагональю
                        for (int j = 0; j <= kolStolb; j++) {// вычитание строк
                            mat[i][index[j]].minus(Complex.virtualMulti(mat[k][index[j]], temp));
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


            Complex[] result = new Complex[mat.length];// вычисляем корни
            result[kolStolb - 1] = new Complex(mat[kolStr - 1][kolStolb]);
            for(int i = 0; i < kolStolb - 1; i++){
                result[i] = new Complex("0");
            }
            for(int i = kolStr - 2; i >= 0; i--){
                for(int j = 0; j < kolStolb; j++){
                    mat[i][kolStolb].minus(Complex.virtualMulti(mat[i][j], result[j])); // избавляемся от уже известных корней
                }
                result[i] = mat[i][kolStolb];
            }

            for (int i = 0; i < kolStolb; i++){
                writer.write(result[i].toString());
                writer.write("\r\n");
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

class Complex{
    private float real;
    private float imaginary;

    public Complex(Complex complex){
        real = complex.getReal();
        imaginary = complex.getImaginary();
    }

    public Complex(String s){
        if(s.contains("i")) {
            if (s.substring(1).contains("+")) { // substring нужен, чтобы игнорировать знак перед числом
                String[] forSplit = s.split("\\+");
                if(forSplit.length == 2){ // если встретился один такой символ
                    real = Float.parseFloat(forSplit[0]);
                    imaginary = (forSplit[1].equals("i")? 1: Float.parseFloat(forSplit[1].replace("i", "")));
                }
                else{ // если встретилось два "+"
                    real = Float.parseFloat(forSplit[1]);
                    imaginary =  (forSplit[2].equals("i")? 1: Float.parseFloat(forSplit[2].replace("i", "")));
                }
            }
            else if (s.substring(1).contains("-")) {
                String[] forSplit = s.split("-");
                if (forSplit.length == 2) {// если встретился один такой символ
                    real = Float.parseFloat(forSplit[0]);
                    imaginary = (forSplit[1].equals("i") ? 1 : Float.parseFloat(forSplit[1].replace("i", ""))) * -1;
                } else { // если встретилось 2 "-"
                    real = Float.parseFloat(forSplit[1]) * -1;
                    imaginary = (forSplit[2].equals("i") ? 1 : Float.parseFloat(forSplit[2].replace("i", ""))) * -1;
                }
            }
            else{ // если разделителей нет
                real = 0;
                imaginary = (s.equals("-i")? -1: (s.equals("i")? 1: Float.parseFloat(s.replace("i", ""))));
            }
        }
        else { // если нет мнимой части
            real = Float.parseFloat(s);
            imaginary = 0;
        }
    }

    public boolean isNotNull(){
        if(real == 0 && imaginary == 0){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isNearlyNotNull(){
        if(real != 0 || imaginary != 0){
            return true;
        }
        else {
            return false;
        }
    }

    public String toString(){
        return Float.toString(real) + (imaginary != 0 ?(imaginary < 0? imaginary: "+" + imaginary) + "i": "");
    }

    public float getReal(){
        return real;
    }

    public float getImaginary(){
        return imaginary;
    }

    public void minus(Complex second){
        real -= second.getReal();
        imaginary -= second.getImaginary();
    }

    public void dev(Complex second){
        float tempReal;
        tempReal = (float)((real * second.getReal() + imaginary * second.getImaginary()) / (Math.pow(second.getReal(), 2) + Math.pow(second.getImaginary(), 2)));
        imaginary = (float)((imaginary * second.getReal() - real * second.getImaginary()) / (Math.pow(second.getReal(), 2) + Math.pow(second.getImaginary(), 2)));
        real = tempReal;
    }

    public void multi(Complex second){
        real = real * second.getReal() - imaginary * second.getImaginary();
        imaginary = real * second.getImaginary() + imaginary * second.getReal();
    }

    public static Complex virtualMulti(Complex first, Complex second) {// умножение без изменения сторон, только для получения результата
        float resultReal, resultImaginary;
        resultReal = first.getReal() * second.getReal() - first.getImaginary() * second.getImaginary();
        resultImaginary = first.getReal() * second.getImaginary() + first.getImaginary() * second.getReal();
        return new Complex(resultReal + (resultImaginary >= 0? "+": "") + resultImaginary + "i");
    }

}

