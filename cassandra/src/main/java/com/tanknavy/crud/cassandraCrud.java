package com.tanknavy.crud;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Author: Alex Cheng 7/18/2020 8:17 PM
 */
public class cassandraCrud {

    private Session session;

    @Before
    public void conn() throws UnknownHostException {
        //Cluster.Builder cluster = Cluster.builder().addContactPoints("spark3").withPort(9042).build();
        Cluster cluster = Cluster.builder().
                addContactPoints(Arrays.asList(InetAddress.getByName("spark1"), InetAddress.getByName("spark3"))).withPort(9042).build();

        session = cluster.connect("test_ks");
        //System.out.println(session.toString());
    }


    @Test
    public void insert(){

        session.execute("insert into employee_uuid(id,fname,lname) values(uuid(),'carl','cheng')");

        session.close();

    }

    @Test
    public void query(){
        ResultSet results = session.execute("select * from employee_uuid");
        for (Row result : results) {
            //System.out.println(result.getColumnDefinitions());
            System.out.println(result.toString());
            //System.out.println(result.getUUID("id") + result.getString("fname") + result.getString("lname"));
        }

        session.close();
    }
}
