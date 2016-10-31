package com.cristiansanchez.mytweetapp.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by kristianss27 on 10/30/16.
 */
@Entity(
        active = true,
        generateConstructors = true,
        generateGettersSetters = true
)
public class TweetEntity{

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "BODY")
    private String body;
    @Property(nameInDb = "TWEET_ID")
    private long uId;
    @Property(nameInDb = "CREATED_AT")
    private String createdAt;

    @Property(nameInDb = "USER_NAME")
    private String name;
    @Property(nameInDb = "USER_ID")
    private long userId;
    @Property(nameInDb = "USER_SCREEN_NAME")
    private String screenName;
    @Property(nameInDb = "USER_PROFILE_IMAGE_URL")
    String profileImageUrl;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
/** Used for active entity operations. */
@Generated(hash = 1601903262)
private transient TweetEntityDao myDao;
@Generated(hash = 726314057)
public TweetEntity(Long id, String body, long uId, String createdAt,
                String name, long userId, String screenName,
                String profileImageUrl) {
        this.id = id;
        this.body = body;
        this.uId = uId;
        this.createdAt = createdAt;
        this.name = name;
        this.userId = userId;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
}
@Generated(hash = 437219682)
public TweetEntity() {
}
public Long getId() {
        return this.id;
}
public void setId(Long id) {
        this.id = id;
}
public String getBody() {
        return this.body;
}
public void setBody(String body) {
        this.body = body;
}
public long getUId() {
        return this.uId;
}
public void setUId(long uId) {
        this.uId = uId;
}
public String getCreatedAt() {
        return this.createdAt;
}
public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
}
public String getName() {
        return this.name;
}
public void setName(String name) {
        this.name = name;
}
public long getUserId() {
        return this.userId;
}
public void setUserId(long userId) {
        this.userId = userId;
}
public String getScreenName() {
        return this.screenName;
}
public void setScreenName(String screenName) {
        this.screenName = screenName;
}
public String getProfileImageUrl() {
        return this.profileImageUrl;
}
public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
        if (myDao == null) {
                throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
        if (myDao == null) {
                throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
        if (myDao == null) {
                throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 617400180)
public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTweetEntityDao() : null;
}
}
