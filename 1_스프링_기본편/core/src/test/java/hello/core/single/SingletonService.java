package hello.core.single;

public class SingletonService {
    // 싱글턴 객체 하나생성.
    private static final SingletonService instance = new SingletonService();
    // 싱글턴 객체 조회(가져오는것)
    public static SingletonService getInstance() {
        return instance;
    }
    // 2개 이상 생성못하게 막음.
    private SingletonService(){} // new SingletonService() 못해

}
