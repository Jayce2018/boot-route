import com.jayce.boot.route.common.util.DesignThreadPool;
import com.jayce.boot.route.entity.LibraryBook;

import java.util.Date;
import java.util.concurrent.RejectedExecutionException;

public class MultithreadedControl {
    public static void main(String[] args) {
        DesignThreadPool pool = new DesignThreadPool();
        LibraryBook book=new LibraryBook();
        for (int i = 0; i <5 ; i++) {
            int finalI = i;
            try {
                pool.execute(() -> {
                    new Design().calculate(finalI,book);
                });
            }catch (RejectedExecutionException e){
                System.out.println("参数为"+i+"的任务阻塞，删除任务");
            }

        }
        while (true){
            if(null!=book.getBookId()){
                System.out.println("bookId->"+book.getBookId());
                break;
            }
        }
        /*while (true){
            if(null!=book.getBookName()){
                System.out.println("bookName->"+book.getBookName());
                break;
            }
        }
        while (true){
            if(null!=book.getStatus()){
                System.out.println("status->"+book.getStatus());
                break;
            }
        }
        while (true){
            if(null!=book.getType()){
                System.out.println("type->"+book.getType());
                break;
            }
        }
        while (true){
            if(null!=book.getCreateTime()){
                System.out.println("createTime->"+book.getCreateTime());
                break;
            }
        }*/

        System.out.println(book);
    }

}

class Design {
    public  String calculate(Integer type,LibraryBook book) {
        switch (type){
            case 0:{
                book.setBookId(10086L);
                break;
            }
            case 1:{
                book.setBookName("BookName");
                break;
            }
            case 2:{
                book.setStatus(10086);
                break;
            }
            case 3:{
                book.setType(10086);
                break;
            }
            case 4:{
                book.setCreateTime(new Date());
                break;
            }
        }
        return "";
    }
}
