package Java2_DZ_3;
import java.util.*;

/**
 * 1 . Создать массив с набором слов (10-20 слов, должны встречаться повторяющиеся).
 * Найти и вывести список уникальных слов, из которых состоит массив (дубликаты не считаем).
 * Посчитать сколько раз встречается каждое слово.
 * Консоль желательно не использовать (в том числе Scanner), тестировать просто из метода main() прописывая add() и get().
 * */

public class StartApp_2 {

    public static void main(String[] args) {
        String[] arg = new String[]{"molotok","file","file","nail","nail","nail","pliers","screwdriver","screwdriver"};
        System.out.println(Arrays.toString(arg)); // Выводим весь массив.
        MyArrayString myArrayString = new MyArrayString(arg);
        System.out.println(myArrayString.UniqueWords()); // Метод считает колличество уникальных слов.
          myArrayString.howManyToimesDoesTheWordOccur(); // Сколько раз встречается каждое слово.
    }
}

class MyArrayString{

    private static String[] array;

    public MyArrayString(String[] array){
        MyArrayString.array = array;
    }

    public HashSet<String> UniqueWords() {
        HashSet<String> stringHashSet = new HashSet<>();
        for (int i = 0; i <MyArrayString.array.length ; i++) {
            stringHashSet.add(MyArrayString.array[i]);
        }
        return stringHashSet;
    }

    public void howManyToimesDoesTheWordOccur() {
        HashSet<String> hashSetBasic = this.UniqueWords();
        String[] strings = hashSetBasic.toArray(new String[hashSetBasic.size()]);
        HashMap<String, Integer> arrayMapCount = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            int countString = 0;
            for (int j = 0; j < array.length; j++) {
                if(strings[i].equals(array[j])){
                    countString++;
                    arrayMapCount.put(strings[i],countString);
                }
            }
        }
        System.out.println(arrayMapCount);
    }
}