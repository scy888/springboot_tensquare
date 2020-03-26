package com.tensquare.friend.serivce;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.hibernate.dialect.FirebirdDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    /**
     * 添加好友
     * @param userId
     * @param friendid
     */
    @Transactional
    public void like(String userId, String friendid) {
        //1. 防止重复添加, userid(列名)=userId, friendid(列名)=friendid
        int cnt = friendDao.count(userId, friendid);
        if(cnt > 0){
            throw new RuntimeException("已经添加过这个好友了");
        }
        //2. 判断对方是否也把你加为好友
        //   条件userid(列名)=friendid, friendid(列名)=userId
        cnt = friendDao.count(friendid, userId);

        Friend friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        //3. 如果有记录
        if(cnt > 0){
        //4. 修改对方中好友记录中的islike=1
            friendDao.updateIsLike(friendid, userId, "1");
            friend.setIslike("1");
        }

        //5. 插入我的好友记录 userId, friendId, islike的值=3.是否有记录
        friendDao.save(friend);
    }

    /**
     * 添加非好友，此处不是删除好友
     * @param userId
     * @param friendid
     */
    public void addNoFriend(String userId, String friendid) {
        // 防止重复点击 略

        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendid);

        noFriendDao.save(noFriend);

    }
     @Transactional
    public void deleteFriend(String userId, String friendId) {
     //删除好友记录中属于我的记录
        friendDao.deleteFriend(userId,friendId);
     //更改对方好友记录里的islike=0
        friendDao.updateLike(friendId,userId,"0");
     //添加非好友记录
        this.addNoFriend(userId,friendId);
    }
}
