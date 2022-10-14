package org.parallel.jpa.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
@RequiredArgsConstructor
public class PmConnect {
    private final DataSource ds;

    public Connection getCon() {
        try {
            Connection c = ds.getConnection();
            return c;
        } catch (Exception e) {
            return null;
        }
    }

}
