package be.ehb.gdt.kaai.appfram.petstore.models.inputModels;

public class UserRole {
    private String username;
    private String role;

    public UserRole(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
