package jms;

import db.DatabaseManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class PostAction {
    @Inject
    DatabaseManager dbManager;

    public void call(long id) {
        dbManager.saveEndTime(id, new Date());
    }
}