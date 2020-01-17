import java.util.*;

public class MyBoard1<E extends Data> implements DataBoard {
    /*
    * AF -> <this.password, this.categories, {categorizedData.get(i).get(j) | 0 <= i < categories.size() && 0 <= j < categorizedData.get(i).size()},
    *          {friendsForCategories.get(i).get(j) | 0 <= i < categories.size() && 0 <= j < friends.get(i).size()},
    *           categorizedData: String -> List<E>, friendsForCategories: String -> List<String>, likes: E -> List<String>>
    *
    * RI -> password != null && categories != null && categorizedData != null && friendsForCategories != null && likes != null
    *       && ncat = categories.size() = categorizedData.size() = friendsForCategories.size()
    *       && for all i, 0 <= i < ncat, categories.get(i) != null
    *       && for all i, 0 <= i < categorizedData.size(), categorizedData.get(i) != null
    *       && for all i, 0 <= i < categorizedData.size() && for all j, 0 <= j < categorizedData.get(i).size() , categorizedData.get(i).get(j) != null
    *       && for all i, 0 <= i < friendsForCategories.size(), friendsForCategories.get(i) != null
    *       && for all i, 0 <= i < friendsForCategories.size() && for all j, 0 <= j < friendsForCategories.get(i).size() , friendsForCategories.get(i).get(j) != null
    *       && for all i, 0 <= i < likesForData.size() , likesForData.get(i) != null
    *       && for all i, 0 <= i < likesForData.size() && for all j, 0 <= j < likesForData.get(i).size() , likesForData.get(i).get(j) != null
    *       && for all i,j , 0 <= i,j < ncat . i!= j ==> !categories.get(i).equals(categories.get(j))
    *       && for all i, 0 <= i < categorizedData.size() && for all j,k , 0 <= j,k < categorizedData.get(i).size() con j!=k, !categorizedData.get(i).get(j).equals(categorizedData.get(i).getk))
    *       && ogni dato inserito ha una categoria
    *       && for all i, 0 <= i < friendsForCategories.size() && for all j,k , 0 <= j,k < friendsForCategories.get(i).size() con j!=k , !friendsForCategories.get(i).get(j).equals(friendsForCategories.get(i).get(k))
    *       && ogni amico può mettere like a un dato a cui ha accesso una sola volta
    *          cioè for all i , 0 <= i < likes.size() . for all j , 0 <= j < likes.get(i).size() , #(likes.get(i).get(j)) = 1
    *       && l'insieme di amici che hanno messo like a un dato è un sottoinsieme dell'insieme di amici che hanno accesso
    *           alla categoria a cui appartiene quel dato
    *
    * */

    private String password;
    private List<String> categories;
    private HashMap<String, List<E>> categorizedData;
    private HashMap<String, List<String>> friendsForCategories;
    private HashMap<E, List<String>> likes;


    public MyBoard1(String password) {
        if(password == null) throw new NullPointerException();
        this.password = password;
        categories = new ArrayList<>();
        categorizedData = new HashMap<>();
        friendsForCategories = new HashMap<>();
        likes = new HashMap<>();
    }

    @Override
    public void createCategory(String category, String passw) throws WrongPasswordException {
        if(category == null || passw == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controllo se la categoria category è già presente nella lista*/
        if(categories.contains(category)) throw new IllegalArgumentException(category + " is already in the category list.");

        /*aggiungo la categoria category alla lista delle categorie*/
        categories.add(category);

        /*associa una nuova lista di dati alla categoria*/
        categorizedData.put(category, new ArrayList<E>());

        /*associa la categoria alla lista di amici che avranno accesso a questa nuova categoria*/
        friendsForCategories.put(category, new ArrayList<String>());
        System.out.println("Category " + category + " created!");
    }

    @Override
    public void removeCategory(String category, String passw) throws WrongPasswordException {
        if(category == null || passw == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        if(categories.contains(category)) {

            /*elimina i likes associati ai dati di quella categoria*/
            for (Data d: categorizedData.get(category)) {
                likes.get(d).clear();
                likes.remove(d);
            }

            /*elimina tutti i dati associati alla categoria, e la lista di amici che ha accesso a questa categoria*/
            categorizedData.get(category).clear();  //elimina tutti gli elementi della lista
            categorizedData.remove(category);       //rimuove l'associazione nella HashMap
            friendsForCategories.get(category).clear();
            friendsForCategories.remove(category);

            /*rimuove category dalla lista di categorie*/
            categories.remove(category);
            System.out.println("category " + category + " removed");
        }

    }

    @Override
    public void addFriend(String category, String passw, String friend) throws WrongPasswordException {
        if(category == null || passw == null || friend == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");
        if(!categories.contains(category)) throw new IllegalArgumentException(category + " is not a category in this DataBoard");

        if(!friendsForCategories.get(category).contains(friend)){
            friendsForCategories.get(category).add(friend);
            System.out.println("New friend " + friend + " added to " + category);
        }
    }

    @Override
    public void removeFriend(String category, String passw, String friend) throws WrongPasswordException {
        if(category == null || passw == null || friend == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controllo se la categoria category è presente nella lista delle categorie*/
        if(!categories.contains(category)) throw new IllegalArgumentException();

        if(friendsForCategories.get(category).contains(friend)){
            //elimino i likes dell'amico che voglio rimuovere
            for (Data d: likes.keySet()) {
                if(likes.get(d).contains(friend)){
                    likes.get(d).remove(friend);
                }
            }
            /*elimino l'amico*/
            friendsForCategories.get(category).remove(friend);
            System.out.println(friend + " removed from category " + category);
        }

    }

    @Override
    public boolean put(String passw, Data data, String category) throws WrongPasswordException {
        if(passw == null || data == null || category == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");
        
        if(this.getCategory(data) != null) throw new IllegalArgumentException("Data is a duplicate");
        if(!categories.contains(category)) throw new IllegalArgumentException(category + " is not a category in the DataBoard");

        likes.put((E)data, new ArrayList<>());  //lista di amici che hanno messo like a questo dato (per ora vuota)
        return categorizedData.get(category).add((E)data);

    }

    @Override
    public Data get(String passw, Data data) throws WrongPasswordException, EmptyDataBoardException {
        if(passw == null || data == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controlla che sia presente almeno un dato*/
        int empty = 0;
        for (String cat: categorizedData.keySet()) {
            if(categorizedData.get(cat).size() == 0) empty++;
        }
        if(empty == categorizedData.size()) throw new EmptyDataBoardException();

        String cat;
        Data result = null;
        if((cat =getCategory(data)) == null){
            /*il dato non è presente*/
            return null;
        }
        else{
            int index = categorizedData.get(cat).indexOf(data);
            try {
                result = (Data)categorizedData.get(cat).get(index).clone();
            }catch(CloneNotSupportedException e){
                System.out.println(e.toString());
            }
            return result;
        }
    }

    @Override
    public Data remove(String passw, Data data) throws WrongPasswordException, EmptyDataBoardException {
        if(passw == null || data == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controlla che sia presente almeno un dato*/
        int empty = 0;
        for (String cat: categorizedData.keySet()) {
            if(categorizedData.get(cat).size() == 0) empty++;
        }
        if(empty == categorizedData.size()) throw new EmptyDataBoardException();

        String cat;
        if((cat =getCategory(data)) == null){
            /*il dato non è presente*/
            return null;
        }
        else{
            int index = categorizedData.get(cat).indexOf(data);
            Data deleted = categorizedData.get(cat).get(index);
            /*rimuove i like del dato data*/
            likes.get(data).clear();
            likes.remove(data);
            /*rimuove il dato dalla lista dei dati della sua categoria*/
            categorizedData.get(cat).remove(data);
            System.out.println("Data deleted!");
            return deleted;
        }
    }

    @Override
    public List<E> getDataCategory(String passw, String category) throws WrongPasswordException {
        if(passw == null || category == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");


        if(categories.contains(category)){  //se la categoria è presente
            List<E> dataList = new ArrayList<>();
            for (E elem: categorizedData.get(category)) {
                try {
                    dataList.add((E)elem.clone());
                }catch(CloneNotSupportedException e){
                    System.out.println(e.toString());
                }
            }
            if(dataList.isEmpty()){
                System.out.println("No data in the category: " + category);
            }
            return dataList;
        }
        return null;
    }

    @Override
    public Iterator<E> getIterator(String passw) throws WrongPasswordException {
        if(passw == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        List<E> orderedData = new ArrayList<>();
        List<Integer> nLikes = new ArrayList<>();

        for (Data d: likes.keySet()) {
            int n = getLikes(d);
            orderedData.add((E)d);
            nLikes.add(n);
        }

        /*se la lista di dati è vuota*/
        if(orderedData.size() == 0) return null;

        /*ordino le liste*/
        sortLists(orderedData, nLikes);

        /*rendo la lista non modificabile e invoco il metodo di java iterator()
        * in questo modo la remove dell'iteratore non avrà alcun effetto sulla lista*/
        return Collections.unmodifiableList(orderedData).iterator();
    }

    @Override
    public void insertLike(String friend, Data data) {
        if(friend == null || data == null) throw new NullPointerException();

        String cat = getCategory(data);
        if(cat == null) throw new NoSuchElementException("the data you're looking for is not part of the DataBoard");

        //il dato è presente. Controllo che friend abbia l'accesso alla sua categoria
        if(!friendsForCategories.get(cat).contains(friend)) throw new IllegalArgumentException(friend + " does not have access to the data");

        //aggiungo il like di friend al dato data, solo se questo non era già presente
        if(!likes.get(data).contains(friend))
            likes.get(data).add(friend);

    }

    @Override
    public Iterator<E> getFriendIterator(String friend) {
        if(friend == null) throw new NullPointerException();

        List<E> friendData = new ArrayList<>();
        for (String cat: categories) {
            if(hasAccess(friend, cat)){
                friendData.addAll(categorizedData.get(cat));
            }
        }

        /*friend has no access to anything*/
        if(friendData.size() == 0) return null;

        /*rendo la lista non modificabile e invoco il metodo di java iterator()
         * in questo modo la remove dell'iteratore non avrà alcun effetto sulla lista*/
        return Collections.unmodifiableList(friendData).iterator();
    }


    /*restituisce la categoria del dato data, null se data non è nella DataBoard*/
    private String getCategory(Data data){
        // REQUIRES: data != null
        // THROWS: se data = null, lancia NullPointerException
        // EFFECTS: restituisce la categoria del dato data, se esiste.
        //          altrimenti restituisce null.

        if(data == null) throw new NullPointerException();

        for (String c: categorizedData.keySet()) {
            if(categorizedData.get(c).contains(data)) return c;
        }
        return null;
    }

    /*restituisce il numero di likes di un dato*/
    public int getLikes(Data d){
        //REQUIRES: d != null
        //          d è nella DataBoard
        //THROWS: se d = null, lancia NullPointerException
        //        se d non è nella DataBoard, lancia IllegalArgumentException
        //EFFECTS: restituisce il numero di likes di data

        if(d == null) throw new NullPointerException();
        if(getCategory(d) == null) throw new IllegalArgumentException("data is not in the Databoard");
        return likes.get(d).size();
    }

    /*restituisce true se friend è nella lista di amici che hanno accesso a quella categoria, false altrimenti*/
    private boolean hasAccess(String friend, String category){
        //REQUIRES: friend != null
        //          category != null
        //THROWS: se friend = null OR category = null, lancia NullPointerException
        //EFFECTS: restituisce true se friend ha accesso ai dati della categoria category
        //         false altrimenti

        if(friend == null || category == null) throw new NullPointerException();

        for (String f: friendsForCategories.get(category)) {
            if(f.equals(friend)) return true;
        }
        return false;
    }

    /*ordina la lista di dati in base al numero di likes corrispondente a quel dato*/
    private void sortLists(List<E> dataList, List<Integer> countersList){
        for(int i = 0; i<dataList.size(); i++){
            int max = i; //Partiamo dall' i-esimo elemento
            for(int j = i+1; j < dataList.size(); j++) {

                //Qui avviene la selezione, ogni volta
                //che nell' iterazione troviamo un elemento piú grande di max
                //facciamo puntare max all' elemento trovato
                if(countersList.get(max) < countersList.get(j)) {
                    max = j;
                }
            }

            //Se max è diverso dall' elemento di partenza
            //allora avviene lo scambio
            if(max!=i) {
                Integer tmp1 = countersList.get(max);
                E tmp2 = dataList.get(max);

                countersList.set(max, countersList.get(i));
                countersList.set(i, tmp1);

                dataList.set(max, dataList.get(i));
                dataList.set(i, tmp2);
            }

        }
    }

    /*mostra la lista di categorie della DataBoard*/
    public void displayCategories(){
        System.out.println("categories: " + categories.toString());
    }


    /*mostra i dati presenti nella DataBoard, divisi per categorie*/
    public void displayCategorizedData() {
        for (String category: categorizedData.keySet()) {
            System.out.println(category + ":");

            for (Data b : categorizedData.get(category)) {
                System.out.print("\t> ");
                b.display();
            }
        }
    }

    /*mostra gli amici che hanno con cui sono state condivise le varie categorie*/
    public void displayFriendsForCategories(){
        System.out.println("friendsForCategories: " + friendsForCategories.toString());
    }

    /*mostra gli amici che hanno messo un like a ogni dato*/
    public void displayLikesForData(Data data){
        System.out.println("likes: " + likes.get(data).toString());
    }

    
}
