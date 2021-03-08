public class AppMainDZ5 {

    public static void main(String[] args) throws InterruptedException {
        ThredArray thredArray = new ThredArray();

        thredArray.defaultArrayMethod();
        thredArray.thredArrayMethod();
    }

}

class ThredArray{
    static final int size = 10000000;
    static final int h = size / 2;


    public  void  thredArrayMethod() throws InterruptedException {
        float[] arr = new float[size];
        for (int i = 0; i <size ; i++) {
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        float[] a1 = new float[h];
        System.arraycopy(arr, 0, a1, 0, h);
        float[] a2 = new float[h];
        System.arraycopy(arr, h, a1, 0, h);

        Thread ta1 = new Thread(() -> {
            for (int i = 0; i <a1.length ; i++) {
                a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2)); }
        });

        Thread ta2 = new Thread(() -> {
            for (int i = h; i <a2.length ; i++) {
                a2[i] = (float)(a2[i+h] * Math.sin(0.2f + (i+h)/ 5) * Math.cos(0.2f + (i+h) / 5) * Math.cos(0.4f + (i+h) / 2)); }
        });

        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, 0, h);

        ta1.start();
        ta2.start();
        ta1.join();
        ta2.join();

        System.out.println(System.currentTimeMillis() - a);
    }

    public  void  defaultArrayMethod(){
        float[] arr = new float[size];
        for (int i = 0; i <size ; i++) {
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        for (int i = 0; i <size ; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(System.currentTimeMillis() - a);
    }
}