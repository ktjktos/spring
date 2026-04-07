package org.example;
import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private String login;
    private String password;
    private String role;
    private String rentedVehicleId;

    public User(String login, String password, String role) {
        this.login = login;
        this.password = DigestUtils.sha256Hex(password);
        this.role = role;
        this.rentedVehicleId = null;
    }

    public User(String login, String password, String role, String id) {
        this.login = login;
        this.password = DigestUtils.sha256Hex(password);
        this.role = role;
        this.rentedVehicleId = id;
    }


    public User(String login, String password, String role, Boolean hash) { // do wczytywania juz zahashowanych danych
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedVehicleId = null;
    }
    public User(User user){
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.rentedVehicleId = user.getRentedVehicleId();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRentedVehicleId() {
        return rentedVehicleId;
    }

    public void setRentedVehicleId(String rentedVehicleId) {
        this.rentedVehicleId = rentedVehicleId;
    }
}
