package org.parallel.web.modules.pm.cabinet;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.parallel.jpa.domain.PmNode;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PmCabinetQuery {
    private final EntityManager em;
    private final DruidDataSource dataSource;
    //查看某个机柜
    public JSONObject getCabinet(String cabinetId){
        JSONObject jo = new JSONObject();
        String sql;
        sql = "SELECT * FROM pm_cabinet WHERE id="+cabinetId;
        Query query = em.createNativeQuery(sql, PmCabinet.class);
        List<PmCabinet> res = query.getResultList();
        for (int i = 0; i < res.size(); i++) {
            jo.put("id",res.get(i).getId());
            jo.put("name",res.get(i).getName());
            jo.put("location",res.get(i).getLocation());
            jo.put("width",res.get(i).getWidth());
            jo.put("height",res.get(i).getHeight());
        }
//        System.out.println(jo);
        return  jo;
    }

    //查看所有机柜
    public JSONObject getCabinet(){
        JSONObject jo = new JSONObject();
        String sql = "SELECT * FROM pm_cabinet";
        Query query = em.createNativeQuery(sql,PmCabinet.class);
        List<PmCabinet> res = query.getResultList();
        for (int i = 0; i < res.size(); i++) {
            JSONObject jo1 = new JSONObject();
            jo1.put("id",res.get(i).getId());
            jo1.put("name",res.get(i).getName());
            jo1.put("location",res.get(i).getLocation());
            jo1.put("width",res.get(i).getWidth());
            jo1.put("height",res.get(i).getHeight());
            jo.put(res.get(i).getId().toString(),jo1);
        }
//        System.out.println(jo);
        return  jo;

    }

    //查看某个机柜的所有节点
    public JSONObject getCabinetNode(String cabinetId){
        JSONObject jo = new JSONObject();
        String sql = "SELECT * FROM pm_cabinet_node WHERE cabinet = "+cabinetId;
        Query query = em.createNativeQuery(sql,PmCabinetNode.class);
        List<PmCabinetNode> res = query.getResultList();
        for (int i = 0; i < res.size(); i++) {
            JSONObject jo1 = new JSONObject();
            jo1.put("id",res.get(i).getId());
            jo1.put("height",res.get(i).getHeight());
            jo1.put("x",res.get(i).getX());
            jo1.put("type",res.get(i).getType());
            jo1.put("manufacturer",res.get(i).getManufacturer());
            jo1.put("date",res.get(i).getDate());
            jo1.put("name",res.get(i).getName());
            jo1.put("cabinet",res.get(i).getCabinet());
            jo1.put("y",res.get(i).getY());
            jo1.put("width",res.get(i).getWidth());
            jo.put(res.get(i).getId().toString(),jo1);
        }
        return  jo;
    }

    //新增节点信息
    public void postCabinetNode(JSONObject jo) {
        String sql = "INSERT INTO pm_cabinet_node (id,height,x,type,manufacturer,date,name,cabinet,y,width) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql);
            for (String i : jo.keySet()) {
                JSONObject jo1 = jo.getJSONObject(i);
                System.out.println("jo1 is : " + jo1);
                pstmt.setLong(1, jo1.getLong("id"));
                pstmt.setString(2, jo1.getString("height"));
                pstmt.setString(3, jo1.getString("x"));
                pstmt.setString(4, jo1.getString("type"));
                pstmt.setString(5, jo1.getString("manufacturer"));
                pstmt.setString(6, jo1.getString("date"));
                pstmt.setString(7, jo1.getString("name"));
                pstmt.setString(8, jo1.getString("cabinet"));
                pstmt.setString(9, jo1.getString("y"));
                pstmt.setString(10, jo1.getString("width"));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //新增机柜信息
    public void postCabinet(JSONObject jo){
        String sql="INSERT INTO pm_cabinet (id,name,location,width,height) VALUES (?,?,?,?,?)";
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql);
            for (String i: jo.keySet()){
                JSONObject jo1 = jo.getJSONObject(i);
                pstmt.setLong(1,jo1.getLong("id"));
                pstmt.setString(2,jo1.getString("name"));
                pstmt.setString(3,jo1.getString("location"));
                pstmt.setString(4,jo1.getString("width"));
                pstmt.setString(5,jo1.getString("height"));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//删除机柜
    public void delCabinet(JSONObject jo){
        String sql="DELETE FROM pm_cabinet WHERE (id = ?)";
        try (Connection con = dataSource.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql);
            for (String i: jo.keySet()){
                JSONObject jo1 = jo.getJSONObject(i);
                pstmt.setLong(1,jo1.getLong("id"));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//删除节点
    public void delCabinetNode(JSONObject jo){
        String sql="DELETE FROM pm_cabinet_node WHERE (id = ?)";
        try (Connection con = dataSource.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql);
            for (String i: jo.keySet()){
                JSONObject jo1 = jo.getJSONObject(i);
                pstmt.setLong(1,jo1.getLong("id"));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
//更新机柜信息
    public void putCabinet(JSONObject jo){

        String sql="UPDATE pm_cabinet set name=?,location=?,width=?,height=?  WHERE (id = ?)";
        try (Connection con = dataSource.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, jo.getString("name"));
            pstmt.setString(2,jo.getString("location"));
            pstmt.setString(3, jo.getString("width"));
            pstmt.setString(4, jo.getString("height"));
            pstmt.setLong(5,jo.getLong("id"));
            pstmt.addBatch();
            pstmt.executeBatch();
            con.commit();
            pstmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
//更新节点信息
public void putCabinetNode(JSONObject jo){

    String sql="UPDATE pm_cabinet_node set height=?,x=?,type=?,manufacturer=?,date=?,name=?,cabinet=?,y=?,width=?  WHERE (id = ?)";
    try (Connection con = dataSource.getConnection()){
        con.setAutoCommit(false);
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, jo.getString("height"));
        pstmt.setString(2, jo.getString("x"));
        pstmt.setString(3, jo.getString("type"));
        pstmt.setString(4, jo.getString("manufacturer"));
        pstmt.setString(5,jo.getString("date"));
        pstmt.setString(6,jo.getString("name"));
        pstmt.setString(7,jo.getString("cabinet"));
        pstmt.setString(8,jo.getString("y"));
        pstmt.setString(9,jo.getString("width"));
        pstmt.setLong(10,jo.getLong("id"));
        pstmt.addBatch();
        pstmt.executeBatch();
        con.commit();
        pstmt.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
}
