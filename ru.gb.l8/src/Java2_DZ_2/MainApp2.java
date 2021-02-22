package Java2_DZ_2;
/**
 * 1. Напишите метод, на вход которого подаётся двумерный строковый массив размером 4х4,
 * при подаче массива другого размера необходимо бросить исключение MyArraySizeException.
 * 2. Далее метод должен пройтись по всем элементам массива, преобразовать в int,
 * и просуммировать. Если в каком-то элементе массива преобразование не удалось (например, в ячейке лежит символ или текст вместо числа), должно быть брошено исключение MyArrayDataException, с детализацией в какой именно ячейке лежат неверные данные.
 * 3. В методе main() вызвать полученный метод, обработать возможные исключения MySizeArrayException
 * и MyArrayDataException, и вывести результат расчета.
 * */

class StartApp {
    public static void main(String[] args){

        String[][] str = {{"1", "1", "*", "1"},{"1","1","1","1"},{"1","1","1","1"},{"1","1","1","1"}};

        try {
            System.out.println(MainApp2.finalMethodMyArray(str));
        } catch (MyArrayDataException e) {
            e.printStackTrace();
        } catch (MyArraySizeException e) {
            e.printStackTrace();
        }


    }
}




public class MainApp2 {


    public static int finalMethodMyArray(String[][] str) throws MyArrayDataException, MyArraySizeException{
        myArrayLength(str);
        return myArrayData(str);
    }



    public static void myArrayLength(String[][] str) throws MyArraySizeException{

        if(str.length != 4){
            throw new MyArraySizeException();
        }

        for (int s = 0; s < str.length; s++) {

                if (str[s].length != 4) {
                    throw new MyArraySizeException();
                }
        }
    }

    public static int  myArrayData(String[][] str) throws MyArrayDataException{
        int summ =0;
        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j <str.length; j++) {

                try {
                    summ += Integer.parseInt(str[i][j]);
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException("Невозможно элемент массива преобразовать к числу!!!\n"+"Строка массива "+i+"\n"+ "Столбец массива "+j);
                }

            }
        }
        return summ;
    }

}

class MyArraySizeException extends Exception{
    public MyArraySizeException() {
        super("Массив не соответствует размеру [4][4]");
    }
}

class MyArrayDataException extends NumberFormatException {
    public MyArrayDataException(String s) {
        super(s);
    }
}