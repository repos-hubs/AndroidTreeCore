package com.testtreecore;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.testtreecore.Account;
import com.testtreecore.User;

import com.testtreecore.AccountDao;
import com.testtreecore.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig userDaoConfig;

    private final AccountDao accountDao;
    private final UserDao userDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        accountDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
