public interface Observer {
    // Subject calls notifyObserver(), which calls update for each Observer it is responsible for
    void update(String playerName, String position, boolean addedToken);  // update added or removed token
    void update(String playerName, String[] newMill);  // update new mill
}
