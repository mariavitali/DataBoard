import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyBoard2<E extends Data> implements DataBoard {
    /*
    * AF -> <this.password, this.categories, this.data, {friends.get(i).get(j) | 0 <= i < categories.size() && 0 <= j < friends.get(i).size()},
    *           categorizeData: String -> String, friendAccess: String -> List<String>, likes: E -> List<String> >
    *           where:
    *               - categorizeData: String -> String such that for all i , 0 <= i < data.size() ==> categorizeData(data.get(i)) = dataCategories.get(i)
    *               - friendAccess: String -> List<String> such that for all String c . ∃i, 0<=i<categories.size() , c.equals(categories.get(i)) ==> friendAccess(i) = friends.get(i);
    *               - likes: E -> List<String> such that for all d of type E . ∃i, 0<=i<data.size() , d.equals(data.get(i)) ==> likes(d) = likes.get(i);
    *
    * RI -> password != null && categories != null && data != null && dataCategories != null && friends != null && likesForData != null
    *       && data.size() = dataCategories.size() = likesForData.size() = m
    *       && categories.size() = friends.size() = n
    *       && for all i, 0 <= i < n, categories.get(i) != null && friends.get(i) != null
    *       && for all i, 0 <= i < m, data.get(i) != null && dataCategories.get(i) != null && likesForData.get(i) != null
    *       && for all i,j 0 <= i < n, 0 <= j < friends.get(i).size(), friends.get(i).get(j) != null
    *       && for all i,j 0 <= i < m, 0 <= j < likesForData.get(i).size() , likesForData.get(i).get(j) != null
    *       && for all i,j , 0 <= i,j < n . i!= j ==> !categories.get(i).equals(categories.get(j))
    *       && for all i,j , 0 <= i < n && 0 <= j,k < friends.get(i).size() . i!= k ==> !friends.get(i).get(j).equals(friends.get(i).get(k))
    *       && for all i,j , 0 <= i,j < m . i!= j ==> !data.get(i).equals(data.get(j))
    *       && for all i, 0 <= i < m, dataCategories.get(i) is in categories
    *       && ogni amico può mettere like a un dato a cui ha accesso una sola volta
    *          cioè for all i , 0 <= i < m . for all j , 0 <= j < likesForData.get(i).size() , #(likesForData.get(i).get(j)) = 1
    *       && l'insieme di amici che hanno messo like a un dato è un sottoinsieme dell'insieme di amici che hanno accesso
    *           alla categoria a cui appartiene quel dato
    *
    * */



    private String password;
    private List<E> data; //lung
    private List<String> dataCategories; //lung
    private List<String> categories; //length
    private List<List<String>> friends; //length
    private List<List<String>> likesForData; //lung

    public MyBoard2(String passw){
        if(passw == null) throw new NullPointerException();
        this.password = passw;
        data = new ArrayList<>();
        dataCategories = new ArrayList<>();
        categories = new ArrayList<>();
        friends = new ArrayList<>();
        likesForData = new ArrayList<>();
    }


    @Override
    public void createCategory(String category, String passw) throws WrongPasswordException {
        if(category == null || passw == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controllo se la categoria category è già presente nella lista*/
        if(categories.contains(category)) throw new IllegalArgumentException(category + " is already in the category list.");

        /*aggiungo la categoria*/
        categories.add(category);

        /*crea una nuova lista di amici che avranno accesso a questa categoria*/
        friends.add(categories.indexOf(category), new ArrayList<>());

        System.out.println("Category " + category + " created!");
    }

    @Override
    public void removeCategory(String category, String passw) throws WrongPasswordException {
        if(category == null || passw == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*scorre l'array dataCategories e quando trova la categoria da eliminare, elimina quella e il dato associato allo stesso indice nell'array data*/
        for(int i = 0; i < dataCategories.size(); i++){
            if(category.equals(dataCategories.get(i))){

                /*elimina i dati della categoria category*/
                data.remove(i);
                dataCategories.remove(i);

                /*elimina i likes dei dati della categoria category*/
                likesForData.get(i).clear();
                likesForData.remove(i);
            }
        }


        /*elimina la lista di amici che ha accesso ai dati della categoria category*/
        int index = categories.indexOf(category);
        if(index >= 0) {
            friends.get(index).clear();
            friends.remove(index);

            /*rimuove category dalla lista delle categorie*/
            categories.remove(index);
            System.out.println("category " + category + " removed");
        }

    }

    @Override
    public void addFriend(String category, String passw, String friend) throws WrongPasswordException {
        if(category == null || passw == null || friend == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        if(!categories.contains(category)) throw new IllegalArgumentException(category + " is not a category in this DataBoard");

        int index = categories.indexOf(category);
        if(!friends.get(index).contains(friend)){
            friends.get(index).add(friend);
            System.out.println("New friend " + friend + " added to " + category);
        }
    }

    @Override
    public void removeFriend(String category, String passw, String friend) throws WrongPasswordException {
        if(category == null || passw == null || friend == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        if(!categories.contains(category)) throw new IllegalArgumentException(category + " is not a category in this Board");

        int index = categories.indexOf(category);

        /*se friend è presente nella lista di amici con cui i dati di category sono condivisi, lo elimina*/
        if(friends.get(index).contains(friend)) {
            //elimino i likes che ha messo l'amico friend
            for(int i=0; i < likesForData.size(); i++){
                if(likesForData.get(i).contains(friend)){
                    likesForData.get(i).remove(friend);
                }
            }
            friends.get(index).remove(friend);
            System.out.println(friend + " removed from category " + category);
        }
    }

    @Override
    public boolean put(String passw, Data data, String category) throws WrongPasswordException {
        if(passw == null || data == null || category == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        if(this.data.contains(data)) throw new IllegalArgumentException("Data is a duplicate");
        if(!categories.contains(category)) throw new IllegalArgumentException(category + " is not a category in the DataBoard");

        boolean result = this.data.add((E)data);
        dataCategories.add(this.data.indexOf(data), category);

        /*creo una nuova lista per inserire i likes al nuovo dato*/
        likesForData.add(this.data.indexOf(data), new ArrayList<>());

        return result;
    }

    @Override
    public Data get(String passw, Data data) throws WrongPasswordException, EmptyDataBoardException {
        if(passw == null || data == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controlla che sia presente almeno un dato nella DataBoard*/
        if(this.data.isEmpty()) throw new EmptyDataBoardException();

        int index;
        if((index = this.data.indexOf(data)) < 0){
            return null;
        }
        else{
            return this.data.get(index);
        }
    }

    @Override
    public Data remove(String passw, Data data) throws WrongPasswordException, EmptyDataBoardException {
        if(passw == null || data == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*controlla che sia presente almeno un dato nella DataBoard*/
        if(this.data.isEmpty()) throw new EmptyDataBoardException();

        int index;
        if((index = this.data.indexOf(data)) < 0){
            return null;
        }
        else{
            /*elimino il dato e i likes associati a quel dato*/
            Data removed = this.data.get(index);
            likesForData.get(index).clear();
            likesForData.remove(index);
            this.data.remove(index);
            System.out.println("Data deleted!");
            return removed;
        }
    }

    @Override
    public List<E> getDataCategory(String passw, String category) throws WrongPasswordException {

        if(passw == null || category == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        if(!categories.contains(category)) return null;

        List<E> tmp = new ArrayList<>();

        for(int i = 0; i < dataCategories.size(); i++){
            if(category.equals(dataCategories.get(i))){
                try {
                    tmp.add((E)data.get(i).clone());
                }catch(CloneNotSupportedException e){
                    e.getStackTrace();
                }
            }
        }
        if(tmp.size() == 0)
            System.out.println(category + " is empty");
        return tmp;
    }

    @Override
    public Iterator<E> getIterator(String passw) throws WrongPasswordException {
        if(passw == null) throw new NullPointerException();
        if(!passw.equals(password)) throw new WrongPasswordException("Incorrect password");

        /*no data in the DataBoard*/
        if(data.size() == 0) return null;

        List<E> orderedData = new ArrayList<>(data.size());
        List<Integer> counters = new ArrayList<>(data.size());
        for(int i = 0; i < data.size(); i++){
            orderedData.add(i, (E)data.get(i));
            counters.add(i, likesForData.get(i).size());
        }

        sortLists(orderedData, counters);

        return new newLikesIterator(orderedData);
    }

    @Override
    public void insertLike(String friend, Data data) {
        if(friend == null || data == null) throw new NullPointerException();

        /*se friend non ha accesso alla categoria del dato data o se il dato non esiste, lancio un eccezione*/
        int index = this.data.indexOf(data);
        if(index < 0) throw new IllegalArgumentException("data is not in the DataBoard");
        String cat = dataCategories.get(index);
        int cat_index = categories.indexOf(cat);
        if(!friends.get(cat_index).contains(friend)) throw new IllegalArgumentException(friend  + " does not have access to this category");


        /*se il like di friend non era già presente nella lista di likes, viene aggiunto*/
        if(!likesForData.get(index).contains(friend)) {
            likesForData.get(index).add(friend);
        }


    }

    @Override
    public Iterator<E> getFriendIterator(String friend) {
        if(friend == null) throw new NullPointerException();
        List<E> accessible = new ArrayList<>();

        for(int i = 0; i < categories.size(); i++){
            if(friends.get(i).contains(friend)){
                String cat = categories.get(i);
                for(int j = 0; j < dataCategories.size(); j++){
                    if(cat.equals(dataCategories.get(j))){
                        accessible.add(data.get(j));
                    }
                }
            }
        }

        /*friend has no access to anything*/
        if(accessible.size() == 0) return null;

        return new newFriendIterator(accessible);
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

    /*ridefinisco un iteratore, rendendo la remove impossibile da invocare*/
    private class newLikesIterator implements Iterator{
        private List<E> orderedData;
        private int len;

        public newLikesIterator(List<E> orderedData){
            this.orderedData = orderedData;
            len = 0;
        }

        @Override
        public boolean hasNext() {
            return len < orderedData.size();
        }

        @Override
        public Object next() {
            E nextObj = orderedData.get(len);
            len++;
            return nextObj;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /*ridefinisco un iteratore, rendendo la remove impossibile da invocare*/
    private class newFriendIterator implements Iterator{
        private List<E> accessible;
        private int counter;

        public newFriendIterator(List<E> accessible){
            this.accessible = accessible;
            counter = 0;
        }

        @Override
        public boolean hasNext() {
            return counter < accessible.size();
        }

        @Override
        public Object next() {
            E nextElem = accessible.get(counter);
            counter++;
            return nextElem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /*restituisce il numero di likes di un dato*/
    public int getLikes(Data d){
        //REQUIRES: d != null
        //          d è nella DataBoard
        //THROWS: se d = null, lancia NullPointerException
        //        se d non è nella DataBoard, lancia IllegalArgumentException
        //EFFECTS: restituisce il numero di likes di data

        if(d == null) throw new NullPointerException();
        if(!data.contains(d)) throw new IllegalArgumentException("data is not in the DataBoard");
        int index = data.indexOf(d);
        return likesForData.get(index).size();
    }

    /*mostra la lista di categorie della DataBoard*/
    public void displayCategories(){
        System.out.println("categories: " + categories.toString());
    }

    /*mostra i dati presenti nella DataBoard, divisi per categorie*/
    public void displayCategorizedData(){
        for (String cat: categories) {
            System.out.println(cat + ":");
            for (int i = 0; i < dataCategories.size(); i++) {
                if(cat.equals(dataCategories.get(i))){
                    System.out.print("\t> ");
                    data.get(i).display();
                }
            }

        }
    }

    /*mostra gli amici che hanno con cui sono state condivise le varie categorie*/
    public void displayFriendsForCategories(){
        System.out.print("friendsForCategories: { ");
        for (String cat: categories) {
            int index = categories.indexOf(cat);
            System.out.print(cat + "=" + friends.get(index).toString() + " ");
        }
        System.out.println("}");
    }

    /*mostra gli amici che hanno messo un like a ogni dato*/
    public void displayLikesForData(Data data){
        int index = this.data.indexOf(data);
        System.out.println("likes: " + likesForData.get(index).toString());
    }


}
