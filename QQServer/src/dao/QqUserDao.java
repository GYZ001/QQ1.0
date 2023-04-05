package dao;

import bean.Qquser;

import java.util.List;

public interface QqUserDao {
    public int save(Qquser qquser);

    public int delete(String Num);

    public int update(Qquser qquser);

    public int update(String sql);

    public List<Qquser> findBySql(String sql);

    public Qquser findById(String account);
}
