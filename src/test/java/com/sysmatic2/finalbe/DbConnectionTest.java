package com.sysmatic2.finalbe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DbConnectionTest {
    @Autowired
    DataSource ds;

    @Test
    public void jdbcConnectionTest() throws Exception{
        Connection conn = ds.getConnection();

        System.out.println("conn = " + conn);
        assertNotNull(conn);
    }
}