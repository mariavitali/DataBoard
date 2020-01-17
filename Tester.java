import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Tester {
    public static void main(String[] args) {
        final String  pass = "testpass";
        final String nullPass = null;
        MyBoard1<Book> dataBoard = new MyBoard1<>(pass);
        //MyBoard2<Book> dataBoard = new MyBoard2<>(pass);

        String cat1 = "fantasy";
        String cat2 = "crime and detective";
        String cat3 = "sci-fi";
        String cat4 = "classics";
        String nullCat = null;
        String wrongPassw = "wrong";

        String friend1 = "Ted";
        String friend2 = "Marshall";
        String friend3 = "Lily";
        String friend4 = "Barney";
        String friend5 = "Robin";
        String nullFriend = null;

        Book book1 = new Book(1, "Harry Potter And The Philosopher’s Stone", "J.K. Rowling", 1997);
        Book book2 = new Book(2, "Foundation", "Isaac Asimov", 1951);
        Book book3 = new Book(3, "Sherlock Holmes", "Arthur Conan Doyle", 1887);
        Book book4 = new Book(4, "1984", "George Orwell", 1949);
        Book book5 = new Book(5, "Romeo and Juliet", "William Shakespeare", 1595);
        Book book6 = new Book(6, "I promessi sposi", "Alessandro Manzoni", 1827);
        Book testBook = new Book(11, "test", "test", 1000);
        Book nullBook = null;


        try {
            System.out.println("\nTEST: addCategory");
            System.out.println(">> Trying to create new categories: " + cat1 + ", " + cat2 + ", " + cat3 + ", " + cat4 + "...");
            dataBoard.createCategory(cat1, pass);
            dataBoard.createCategory(cat2, pass);
            dataBoard.createCategory(cat3, pass);
            dataBoard.createCategory(cat4, pass);
            System.out.println();

            /*
            //ECCEZIONI DEL METODO addCategory
            try {
                //se si vogliono testare tutte le eccezioni distintamente bisogna separarle in blocchi try-catch diversi
                System.out.println(">> Trying to add a null category");
                dataBoard.createCategory(nullCat, pass);
                System.out.println(">> Trying to add a category with a null password");
                dataBoard.createCategory(cat1, nullPass);
            }catch(NullPointerException e){
                System.out.println(e.toString());
                //gestione errore
                //si può uscire dal programma
            }
            try {
                System.out.println(">> Trying to add a category with the wrong password");
                dataBoard.createCategory(cat1, wrongPassw);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }
            try {
                System.out.println(">> Trying to create category: " + cat4 + " (again)");
                dataBoard.createCategory(cat4, pass);
            }catch(IllegalArgumentException e){
                System.out.println(e.toString());
            }*/

        }catch (Exception e){
            System.out.println(e.toString());

        }
        System.out.println();
        dataBoard.displayCategories();

        System.out.println();
        System.out.println();


        //ECCEZIONE METODO get
        //ECCEZIONE METODO remove (funziona allo stesso modo)
        try{
            System.out.println(">> Trying to get a data from an empty DataBoard");
            dataBoard.get(pass, book1);
            //dataBoard.remove(pass, book1);
        }catch (EmptyDataBoardException e){
            System.out.println(e.toString());
        }catch(WrongPasswordException e){
            System.out.println(e.toString());
        }


        /*aggiungere amici con cui vengono condivisi i dati delle categorie*/
        try{
            System.out.println("\nTEST: addFriend");
            System.out.println(">> Trying to add new friends " + friend1 + ", " + friend2 + ", " + friend5 + " to category " + cat1 + "...");
            dataBoard.addFriend(cat1, pass, friend1);
            dataBoard.addFriend(cat1, pass, friend2);
            dataBoard.addFriend(cat1, pass, friend5);

            System.out.println(">> Trying to add new friends " + friend1 + ", " + friend4 + " to category " + cat3 + "...");
            dataBoard.addFriend(cat3, pass, friend1);
            dataBoard.addFriend(cat3, pass, friend4);

            System.out.println(">> Trying to add new friend " + friend3 + " to category " + cat2 + "...");
            dataBoard.addFriend(cat2, pass, friend3);
            System.out.println(">> Trying to add new friend " + friend3 + " to category " + cat2 + " (again)...");
            dataBoard.addFriend(cat2, pass, friend3);
            System.out.println(">> Trying to add new friends " + friend4 + ", " + friend5 + " to category " + cat4 + "...");
            dataBoard.addFriend(cat4, pass, friend4);
            dataBoard.addFriend(cat4, pass, friend5);

            /*
            //ECCEZIONI DEL METODO addFriend
            try {
                //se si vogliono testare tutte e tre le eccezioni distintamente bisogna separarle in blocchi try-catch diversi
                System.out.println(">> Trying to add a friend to a null category");
                dataBoard.addFriend(nullCat, pass, friend1);
                System.out.println(">> Trying to add a friend with a null password");
                dataBoard.addFriend(cat1, nullPass, friend1);
                System.out.println(">> Trying to add a null friend");
                dataBoard.addFriend(cat1, pass, nullFriend);
            }catch(NullPointerException e){
                System.out.println(e.toString());
                //gestione errore
                //si può uscire dal programma
            }

            try {
                System.out.println(">> Trying to add a friend with the wrong password");
                dataBoard.addFriend(cat1, wrongPassw, friend1);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> Trying to add a friend to a non-existing category");
                dataBoard.addFriend("cat", pass, friend1);
            }catch(IllegalArgumentException e){
                System.out.println(e.toString());
            }*/


        }catch(Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        dataBoard.displayFriendsForCategories();

        System.out.println();
        System.out.println();

        try{
            System.out.println("\nTEST: put");
            System.out.println(">> Trying to add 6 books...");
            dataBoard.put(pass, book1, cat1);
            dataBoard.put(pass, book2, cat3);
            dataBoard.put(pass, book3, cat2);
            dataBoard.put(pass, book4, cat4);
            dataBoard.put(pass, book5, cat4);
            dataBoard.put(pass, book6, cat4);

            /*
            //ECCEZIONI DEL METODO put
            try{
                //se si vogliono testare tutte le eccezioni distintamente bisogna separarle in blocchi try-catch diversi
                System.out.println(">> Trying to insert a null data");
                dataBoard.put(pass, nullBook, cat1);
                System.out.println(">> Trying to insert a new data into a null category");
                dataBoard.put(pass, book1, nullCat);
                System.out.println(">> Trying to insert a new data with a null password");
                dataBoard.put(nullPass, book1, cat1);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }

            try {
                System.out.println(">> Trying to insert a data that's already in the DataBoard");
                dataBoard.put(pass, book5, cat2);
                System.out.println(">> Trying to insert a data to a category that doesn't exist");
                dataBoard.put(pass, testBook, "cat");
            }catch(IllegalArgumentException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> Trying to insert a new data using the wrong password");
                dataBoard.put(wrongPassw, testBook, "cat");
            }catch (WrongPasswordException e){
                System.out.println(e.toString());
            }*/



        }catch (Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        dataBoard.displayCategorizedData();


        System.out.println();
        System.out.println();

        try{
            System.out.println("\nTEST: get");
            System.out.println(">> Trying to get "+ book3.getTitle() + "...");
            Book b = (Book)dataBoard.get(pass, book3);
            b.display();

            System.out.println(">> Trying to get a new book that doesn't exist in the DataBoard...");
            Book b1 = (Book)dataBoard.get(pass, testBook);
            if(b1 == null){
                System.out.println("null");
            }else b1.display();

            /*
            //ECCEZIONI DEL METODO get
            try{
                System.out.println(">> Trying to get a data using a null password");
                dataBoard.get(nullPass, testBook);
                System.out.println(">> Trying to get a null data");
                dataBoard.get(pass, nullBook);
            } catch (NullPointerException e){
                System.out.println(e.toString());
                //eventuale gestione errore
            }

            try{
                System.out.println(">> Trying to get a data using the wrong password");
                dataBoard.get(wrongPassw, book1);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }*/


        }catch (Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        System.out.println();

        try{
            System.out.println("TEST: insertLike");
            System.out.println(">> " + friend1 + " likes " + book2.getTitle() + "...");
            dataBoard.insertLike(friend1, book2);
            System.out.println(">> " + friend2 + " likes " + book1.getTitle() + "...");
            dataBoard.insertLike(friend2, book1);
            System.out.println(">> " + friend5 + " likes " + book1.getTitle() + "...");
            dataBoard.insertLike(friend5, book1);

            /*
            //ECCEZIONI METODO insertLike
            try{
                System.out.println(">> Null friend trying to like a data");
                dataBoard.insertLike(nullFriend, book1);
                System.out.println(">> Trying to insert a new like to a null data");
                dataBoard.insertLike(friend1, nullBook);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> Trying to insert a like to a data that is not in the DataBoard");
                dataBoard.insertLike(friend1, testBook);
            }catch(NoSuchElementException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> " + friend1 + " likes " + book3.getTitle());
                dataBoard.insertLike(friend1, book3);
            }catch(IllegalArgumentException e){
                System.out.println(e.toString());
            }*/

        }catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println();
        System.out.println(book1.getTitle());
        System.out.print("\t");
        dataBoard.displayLikesForData(book1);
        System.out.println(book2.getTitle());
        System.out.print("\t");
        dataBoard.displayLikesForData(book2);
        System.out.println(book3.getTitle());
        System.out.print("\t");
        dataBoard.displayLikesForData(book3);


        System.out.println();
        System.out.println();

        System.out.println("TEST: getIterator (dati ordinati per numero di likes decrescente)");
        try{
            Iterator<Book> it = dataBoard.getIterator(pass);
            if(it != null) {
                System.out.println("Data ordered by number of likes:");
                while (it.hasNext()) {
                    Book b = it.next();
                    System.out.println(" - " + b.getTitle() + " (" + dataBoard.getLikes(b) + ")");
                }
            }
            else{
                System.out.println("No data in the DataBoard");
            }

            /*
            //ECCEZIONI DEL METODO getIterator
            try{
                System.out.println(">> Trying to get a new iterator using a null password");
                Iterator<Book> iter = dataBoard.getIterator(nullPass);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }
            try{
                System.out.println(">> Trying to get a new iterator using a wrong password");
                Iterator<Book> iter2 = dataBoard.getIterator(wrongPassw);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }*/


        }catch(Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        System.out.println();

        System.out.println("TEST: getFriendIterator");
        try{
            Iterator<Book> it = dataBoard.getFriendIterator(friend5);
            if(it!= null) {
                System.out.println(friend5 + " has access to:");
                while (it.hasNext()) {
                    Book b = it.next();
                    System.out.println(" - " + b.getTitle());
                }
            }
            else{
                System.out.println(friend5 + " has access to zero data");
            }

            /*
            //ECCEZIONI METODO getFriendIterator
            try{
                System.out.println(">> Trying to get a new friendIterator for a null friend");
                dataBoard.getFriendIterator(nullFriend);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }*/

        }catch(Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        System.out.println();

        try{
            System.out.println("TEST: getDataCategory");
            System.out.println("Get list of books in category: " + cat4);
            List<Book> info = dataBoard.getDataCategory(pass, cat4);
            for (Book b: info) {
                b.display();
            }

            /*
            //ECCEZIONI DEL METODO getDataCategory
            try{
                System.out.println(">> Trying to get the list of books using a null password");
                dataBoard.getDataCategory(nullPass, cat1);
                System.out.println(">> Trying to get the list of books in a null category");
                dataBoard.getDataCategory(pass, nullCat);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> Trying to get the list of books in category " + cat1 + " using a wrong password");
                dataBoard.getDataCategory(wrongPassw, cat1);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }*/

        }catch(Exception e){
            System.out.println(e.toString());
        }


        System.out.println();
        System.out.println();

        try{
            System.out.println("TEST: remove (di un dato)");
            System.out.println(">> Trying to remove " + book5.getTitle());
            dataBoard.remove(pass, book5);

            /*
            //ECCEZIONI DEL METODO remove
            try{
                System.out.println(">> Trying to remove a data using a null password");
                dataBoard.remove(nullPass, book1);
                System.out.println(">> Trying to remove a null data");
                dataBoard.remove(pass, nullBook);
            } catch (NullPointerException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> Trying to remove a book using a wrong password");
                dataBoard.remove(wrongPassw, book1);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }*/

        }catch(Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        System.out.println();

        try{
            System.out.println("TEST: removeFriend");
            System.out.println(">> Trying to remove friend " + friend2);
            dataBoard.removeFriend(cat1, pass, friend2);
            System.out.println(">> Trying to remove friend " + friend2 + " (again)");
            dataBoard.removeFriend(cat1, pass, friend2);

            /*
            //ECCEZIONI DEL METODO removeFriend
            try{
                System.out.println(">> Trying to remove a friend from a null category");
                dataBoard.removeFriend(nullCat, pass, friend1);
                System.out.println(">> Trying to remove a friend using a null password");
                dataBoard.removeFriend(cat1, nullPass, friend1);
                System.out.println(">> Trying to remove a null friend");
                dataBoard.removeFriend(cat1, pass, nullFriend);
                System.out.println(">> Trying to remove a friend using a wrong password");
                dataBoard.removeFriend(cat1, wrongPassw, friend1);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }

            try{
                System.out.println(">> Trying to remove a friend from a category that doesn't exist");
                dataBoard.removeFriend("cat", pass, friend1);
            }catch (IllegalArgumentException e){
                System.out.println(e.toString());
            }*/


        }catch(Exception e){
            System.out.println(e.toString());
        }

        System.out.println();
        dataBoard.displayFriendsForCategories();

        System.out.println();
        System.out.println();

        try{
            System.out.println("TEST: removeCategory");
            System.out.println(">> Trying to remove the category " + cat3);
            dataBoard.removeCategory(cat3, pass);
            System.out.println(">> Trying to remove the category " + cat3 + " (again)");
            dataBoard.removeCategory(cat3, pass);

            /*
            //ECCEZIONI DEL METODO removeCategory
            try{
                System.out.println(">> Trying to remove a null category ");
                dataBoard.removeCategory(nullCat, pass);
                System.out.println(">> Trying to remove a category using a null password");
                dataBoard.removeCategory(cat1, nullPass);
            }catch(NullPointerException e){
                System.out.println(e.toString());
            }
            try{
                System.out.println(">> Trying to remove a category using a wrong password");
                dataBoard.removeCategory(cat2, wrongPassw);
            }catch(WrongPasswordException e){
                System.out.println(e.toString());
            }*/

        }catch(Exception e){
            System.out.println(e.toString());
        }
        System.out.println();
        dataBoard.displayCategories();

        System.out.println();
        System.out.println();

    }
}
