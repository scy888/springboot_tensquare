package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> {

    @Query("select count(f) from Friend f where userid=?1 and friendid=?2")
    int count(String userId, String friendid);

    @Query("update Friend set islike='1' where userid=?1 and friendid=?2")
    @Modifying
    void updateIsLike(String friendid, String userId, String s);

    @Query("delete from Friend where userId=?1 and friendId=?2")
    @Modifying
    void deleteFriend(String userId, String friendId);

    @Query("update Friend set islike=?3 where friendId=?2 and userId=?1")
    @Modifying
    void updateLike(String friendId, String userId, String s);
}
