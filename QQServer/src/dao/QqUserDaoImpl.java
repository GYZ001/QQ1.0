package dao;

import bean.Qquser;
import db.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QqUserDaoImpl implements QqUserDao {
    @Override
    public int save(Qquser qquser) {
        if(qquser == null){
            return -1;
        }
        int result = -1;
        String account = qquser.getAccount();
        String name = qquser.getName();
        String password = qquser.getPassword();
        String state = qquser.getState();
        String ip = qquser.getIp();
        int port = qquser.getPort();
        String pic = qquser.getPic();
        String info = qquser.getInfo();
        String place1 = qquser.getPlace1();
        String place2 = qquser.getPlace2();
        String sql = "insert into QqUser(account,name,password,state,ip,port,pic,info,place1,place2) " +
                "values(" +
                "'" + account + "', " +
                "'" + name + "', " +
                "'" + password + "', " +
                "'" + state + "', " +
                "'" + ip + "', " +
                "'" + port + "', " +
                "'" + pic + "', " +
                "'" + info + "', " +
                "'" + place1 + "', " +
                "'" + place2 + "'  " +
                ")";
        System.out.println(sql);
        DBUtil db = new DBUtil();
        result = db.update(sql);
        db.close();
        return result;
    }

    @Override
    public int delete(String Num) {
        return 0;
    }

    @Override
    public int update(Qquser qquser) {
        String sql = "update qquser set state = '" + qquser.getState() + "', ip = '" + qquser.getIp() + "', port = '" + qquser.getPort() + "' where account = '" + qquser.getAccount() + "'";
        DBUtil dbUtil = new DBUtil();
        int num = dbUtil.update(sql);
        return num;
    }

    @Override
    public int update(String sql) {
        DBUtil dbUtil = new DBUtil();
        int num =dbUtil.update(sql);
        return num;
    }

    @Override
    public List<Qquser> findBySql(String sql) {
        List<Qquser> list = new ArrayList<Qquser>();
        DBUtil util = new DBUtil();
        ResultSet rs = util.query(sql);
        try {
            while(rs.next()) {
                Qquser qquser = new Qquser();
                qquser.setAccount(rs.getString(1));
                qquser.setName(rs.getString(2));
                qquser.setPassword(rs.getString(3));
                qquser.setState(rs.getString(4));
                qquser.setIp(rs.getString(5));
                qquser.setPort(rs.getInt(6));
                qquser.setPic(rs.getString(7));
                qquser.setInfo(rs.getString(8));
                qquser.setPlace1(rs.getString(9));
                qquser.setPlace2(rs.getString(10));
                list.add(qquser);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Qquser findById(String account) {
        Qquser qquser = null;
        String sql = "select * from qquser where account = '" + account + "'";
        DBUtil util = new DBUtil();
        ResultSet rs = util.query(sql);
        try {
            while(rs.next()) {
                qquser = new Qquser();
                qquser.setAccount(rs.getString(1));
                qquser.setName(rs.getString(2));
                qquser.setPassword(rs.getString(3));
                qquser.setState(rs.getString(4));
                qquser.setIp(rs.getString(5));
                qquser.setPort(rs.getInt(6));
                qquser.setPic(rs.getString(7));
                qquser.setInfo(rs.getString(8));
                qquser.setPlace1(rs.getString(9));
                qquser.setPlace2(rs.getString(10));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return qquser;
    }
}
