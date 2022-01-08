package be.ehb.gdt.kaai.appfram.petstore.service;

import be.ehb.gdt.kaai.appfram.petstore.models.AppUser;

public interface IUserService {
    AppUser saveUser(AppUser user, String password);

    AppUser getUser(String username);

    Iterable<AppUser> getUsers();

    void assignRole(String username, String role);
}
