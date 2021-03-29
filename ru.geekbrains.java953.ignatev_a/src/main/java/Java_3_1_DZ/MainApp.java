package Java_3_1_DZ;

import java.util.ArrayList;
import java.util.Arrays;

public class MainApp {
    public static void main(String[] args) {
// Задание №1 (Запуск) Начало
        Integer[] a = {1, null, null, null, 3};
        String[] b = {null, null, "A", null, "B"};

        new MainApp().permutationArray(a);

        System.out.println(Arrays.toString(a));

        new MainApp().permutationArray(b);

        System.out.println(Arrays.toString(b));
// Конец

// Задание №2 (Начало)

        String[] s = {"cola", "fanta", "sprait", "baycal"};

        ArrayList<String> arrayList = new MainApp().arrayToArrayList(s);

        for (String str : arrayList) {
            System.out.println(str);
        }


    }

/*Задание №1:Написать метод, который меняет два элемента
массива местами (массив может быть любого ссылочного типа);*/
    // Комментарий: Мой метод принимает массив любого ссылочного типа и меняет первый
    // не null со вторы не тгдд местами.


    public void permutationArray(Object[] obj) {
        if (obj.length > 1) {
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    for (int j = i + 1; j < obj.length; j++) {
                        if (obj[j] != null) {
                            Object value = obj[j];
                            obj[j] = obj[i];
                            obj[i] = value;
                            return;
                        }

                    }
                }
            }


        } else {
            System.out.println("В масиве меньше двух элементов метод не сработает! ");
        }
    }

    /* Задача №2: Написать метод, который преобразует массив в ArrayList;*/

    public <T> ArrayList<T> arrayToArrayList(T[] ts) {
        return new ArrayList<>(Arrays.asList(ts));
    }

}

/*Задача 3:
Даны классы Fruit, Apple extends Fruit, Orange extends Fruit;
Класс Box, в который можно складывать фрукты. Коробки условно сортируются по типу фрукта, поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
Для хранения фруктов внутри коробки можно использовать ArrayList;
Сделать метод getWeight(), который высчитывает вес коробки, зная вес одного фрукта и их количество: вес яблока – 1.0f, апельсина – 1.5f (единицы измерения не важны);
Внутри класса Box сделать метод compare(), который позволяет сравнить текущую коробку с той, которую подадут в compare() в качестве параметра. true – если их массы равны, false в противоположном случае. Можно сравнивать коробки с яблоками и апельсинами;
Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую. Помним про сортировку фруктов: нельзя яблоки высыпать в коробку с апельсинами. Соответственно, в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в первой;
Не забываем про метод добавления фрукта в коробку.
*/
class MainAppFrutis {
    public static void main(String[] args) {

        Box<Apple> appleBox1 = new Box<>();
        appleBox1.addFrutis(3, new Apple());

        Box<Apple> appleBox2 = new Box<>();
        appleBox2.addFrutis(15, new Apple());

        Box<Orange> orangeBox1 = new Box<>();
        orangeBox1.addFrutis(2, new Orange());

        Box<Orange> orangeBox2 = new Box<>();
        orangeBox2.addFrutis(15, new Orange());

        System.out.println(appleBox1.compare(orangeBox1));
        System.out.println(orangeBox2.getWeight());
        System.out.println(appleBox2.getWeight());

        System.out.println(orangeBox1.overfillFrutis(orangeBox2).getWeight());


    }
}

abstract class Fruit {
}

class Apple extends Fruit {
    public static float WEIGHT = 1.0f;
}

class Orange extends Fruit {
    public static float WEIGHT = 1.5f;
}

class Box<T extends Fruit> {

    public ArrayList<T> boxList = new ArrayList<>();


    // Метод вычисляющий вес фруктов в коробке.
    public float getWeight() {
        float weight = 0.0f;
        if (boxList.size() == 0) {
            return weight;
        }
        if (boxList.get(0) instanceof Apple) {
            return Apple.WEIGHT * boxList.size();
        } else {
            return Orange.WEIGHT * boxList.size();
        }
    }

    // Метод добавления фруктов в коробку
    public ArrayList<T> addFrutis(int counter, T t) {
        for (int i = 0; i < counter; i++) {
            this.boxList.add(i, t);
        }
        return this.boxList;
    }

    // Сравнение коробок.
    public boolean compare(Box<?> box2) {
        return Math.abs(this.getWeight() - box2.getWeight()) < 0.0001;
    }

    // Пересыпка из коробки в коробку

    public Box<T> overfillFrutis(Box<T> box2) {
        // Проверка на пустую коробку.
        if (this.boxList.size() == 0) {
            System.out.println("Фруктов в текущей коробки нет!");
            return null;
        } else {
            box2.addFrutis(this.boxList.size(), this.boxList.get(0));

            for (int i = 0; i < this.boxList.size(); i++) {
                this.boxList.remove(i);
            }
            return box2;
        }

    }
}

