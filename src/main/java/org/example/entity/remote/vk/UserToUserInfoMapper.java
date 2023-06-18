package org.example.entity.remote.vk;

import org.example.entity.UserInfo;

public class UserToUserInfoMapper {
    public static UserInfo map(User user) {
        var info = new UserInfo(user.getId());
        info.setFirstName(user.getFirst_name());
        info.setLastName(user.getLast_name());
        info.setBirthDate(user.getBdate());
        if (user.getCity() != null) {
            info.setCity(user.getCity().getTitle());
        }
        info.setContacts(user.getMobile_phone());
        return info;
    }
}
