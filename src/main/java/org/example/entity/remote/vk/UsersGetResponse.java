package org.example.entity.remote.vk;

import java.util.List;

public class UsersGetResponse {
    private List<User> response;

    public List<User> getResponse() {
        return response;
    }

    public void setResponse(List<User> response) {
        this.response = response;
    }
}
