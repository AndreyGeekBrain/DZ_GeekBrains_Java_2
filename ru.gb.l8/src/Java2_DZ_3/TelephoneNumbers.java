package Java2_DZ_3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 2. Написать простой класс ТелефонныйСправочник, который хранит в себе список фамилий и телефонных номеров.
         * В этот телефонный справочник с помощью метода add() можно добавлять записи.
         * С помощью метода get() искать номер телефона по фамилии.
         * Следует учесть, что под одной фамилией может быть несколько телефонов (в случае однофамильцев),
         * тогда при запросе такой фамилии должны выводиться все телефоны.
         * Желательно как можно меньше добавлять своего, чего нет в задании (т.е. не надо в телефонную запись добавлять еще дополнительные поля
         * (имя, отчество, адрес), делать взаимодействие с пользователем через консоль и т.д.).
*/

class AppTelephoneNumbersStart{

    public static void main(String[] args) {
        TelephoneNumbers.setTelephoneNumbers ("Ivanov", 123_333_444);
        TelephoneNumbers.setTelephoneNumbers ("Petrov", 333_333_444);
        TelephoneNumbers.setTelephoneNumbers ("Smirnov", 555_333_444);
        TelephoneNumbers.setTelephoneNumbers ("Frolov", 666_333_444);
        TelephoneNumbers.setTelephoneNumbers ("Ivanov", 123_999_444);
        TelephoneNumbers.setTelephoneNumbers ("Ivanov", 123_100_888);
        TelephoneNumbers.getTelephoneNumbers("Ivanov");
        TelephoneNumbers.getTelephoneNumbers("Smuzin");
    }

}

public class TelephoneNumbers {
    public static HashMap<Integer, String> hashMap = new HashMap<>();
    public static void setTelephoneNumbers (String surname, Integer phone){
        TelephoneNumbers.hashMap.put(phone,surname);
    }
    public static void getTelephoneNumbers (String surname){
        if (!(TelephoneNumbers.hashMap.isEmpty())){
            Iterator<Map.Entry<Integer, String>> iterator = TelephoneNumbers.hashMap.entrySet().iterator();
            int countSurname=0;
            while (iterator.hasNext())
            {
                //получение «пары» элементов
                Map.Entry<Integer, String> pair = iterator.next();
                Integer key = pair.getKey();            //ключ
                String value = pair.getValue();        //значение
                if (value.equals(surname)) {
                    System.out.println( value + ":" + key);
                    countSurname++;
                }
            }
                if (countSurname ==0){
                    System.out.println( surname +" cовпадений не найдено !!!"); }
        } else {
            System.out.println("Поиск не возможен, так как справочник пуст"); }

    }
}
